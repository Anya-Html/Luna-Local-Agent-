#include <jni.h>
#include <llama.h>

#include <algorithm>
#include <mutex>
#include <string>
#include <vector>

namespace {
std::once_flag backend_once;

void ensure_backend() {
    std::call_once(backend_once, [] { llama_backend_init(); });
}

std::string jstring_to_utf8(JNIEnv * env, jstring value) {
    if (value == nullptr) return {};
    const char * chars = env->GetStringUTFChars(value, nullptr);
    if (chars == nullptr) return {};
    std::string result(chars);
    env->ReleaseStringUTFChars(value, chars);
    return result;
}
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_anya_luna_NativeLocalModel_nativeAvailable(JNIEnv*, jobject) {
    ensure_backend();
    return JNI_TRUE;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_anya_luna_NativeLocalModel_nativeGenerate(
        JNIEnv* env, jobject, jstring model_path, jstring prompt, jint max_tokens) {
    if (model_path == nullptr || prompt == nullptr || max_tokens < 1 || max_tokens > 4096) {
        return env->NewStringUTF("LOCAL_MODEL_INVALID_ARGUMENT");
    }

    ensure_backend();
    const std::string path = jstring_to_utf8(env, model_path);
    const std::string input = jstring_to_utf8(env, prompt);
    if (path.empty() || input.empty()) return env->NewStringUTF("LOCAL_MODEL_INVALID_ARGUMENT");

    llama_model_params model_params = llama_model_default_params();
    model_params.n_gpu_layers = 0;
    llama_model* model = llama_model_load_from_file(path.c_str(), model_params);
    if (model == nullptr) return env->NewStringUTF("LOCAL_MODEL_LOAD_FAILED");

    const llama_vocab* vocab = llama_model_get_vocab(model);
    const int n_prompt = -llama_tokenize(vocab, input.c_str(), input.size(), nullptr, 0, true, true);
    if (n_prompt <= 0) {
        llama_model_free(model);
        return env->NewStringUTF("LOCAL_MODEL_TOKENIZE_FAILED");
    }

    std::vector<llama_token> tokens(static_cast<size_t>(n_prompt));
    if (llama_tokenize(vocab, input.c_str(), input.size(), tokens.data(), tokens.size(), true, true) < 0) {
        llama_model_free(model);
        return env->NewStringUTF("LOCAL_MODEL_TOKENIZE_FAILED");
    }

    llama_context_params ctx_params = llama_context_default_params();
    ctx_params.n_ctx = 2048;
    ctx_params.n_batch = std::min<uint32_t>(512, static_cast<uint32_t>(ctx_params.n_ctx));
    ctx_params.n_ubatch = ctx_params.n_batch;
    ctx_params.n_threads = 4;
    ctx_params.n_threads_batch = 4;

    if (static_cast<uint32_t>(n_prompt) >= ctx_params.n_ctx) {
        llama_model_free(model);
        return env->NewStringUTF("LOCAL_MODEL_PROMPT_TOO_LONG");
    }

    llama_context* ctx = llama_init_from_model(model, ctx_params);
    if (ctx == nullptr) {
        llama_model_free(model);
        return env->NewStringUTF("LOCAL_MODEL_CONTEXT_FAILED");
    }

    llama_batch batch = llama_batch_get_one(tokens.data(), tokens.size());
    if (llama_decode(ctx, batch) != 0) {
        llama_free(ctx);
        llama_model_free(model);
        return env->NewStringUTF("LOCAL_MODEL_DECODE_FAILED");
    }

    llama_sampler_chain_params sampler_params = llama_sampler_chain_default_params();
    llama_sampler* sampler = llama_sampler_chain_init(sampler_params);
    if (sampler == nullptr) {
        llama_free(ctx);
        llama_model_free(model);
        return env->NewStringUTF("LOCAL_MODEL_SAMPLER_FAILED");
    }
    llama_sampler_chain_add(sampler, llama_sampler_init_greedy());

    std::string output;
    output.reserve(static_cast<size_t>(max_tokens) * 4);
    for (int i = 0; i < max_tokens; ++i) {
        const llama_token token = llama_sampler_sample(sampler, ctx, -1);
        if (llama_vocab_is_eog(vocab, token)) break;

        char piece[512];
        const int n = llama_token_to_piece(vocab, token, piece, sizeof(piece), 0, true);
        if (n < 0) break;
        output.append(piece, static_cast<size_t>(n));

        llama_sampler_accept(sampler, token);
        batch = llama_batch_get_one(&token, 1);
        if (llama_decode(ctx, batch) != 0) break;
    }

    llama_sampler_free(sampler);
    llama_free(ctx);
    llama_model_free(model);

    if (output.empty()) return env->NewStringUTF("LOCAL_MODEL_EMPTY_OUTPUT");
    return env->NewStringUTF(output.c_str());
}
