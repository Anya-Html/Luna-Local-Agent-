cmake_minimum_required(VERSION 3.22.1)
project(luna_native)

# Luna's native backend is intentionally fail-closed: the app only claims
# llama.cpp support when an exact, reviewed llama.cpp checkout is supplied.
set(LLAMA_CPP_DIR "" CACHE PATH "Path to the pinned llama.cpp checkout")

if(NOT LLAMA_CPP_DIR OR NOT EXISTS "${LLAMA_CPP_DIR}/CMakeLists.txt")
    message(FATAL_ERROR "Luna native build requires LLAMA_CPP_DIR pointing to the pinned llama.cpp checkout (see llama.cpp.PIN)")
endif()

set(GGML_NATIVE OFF CACHE BOOL "" FORCE)
set(GGML_OPENMP OFF CACHE BOOL "" FORCE)
set(GGML_LLAMAFILE OFF CACHE BOOL "" FORCE)
set(LLAMA_OPENSSL OFF CACHE BOOL "" FORCE)
set(GGML_CPU_KLEIDIAI ON CACHE BOOL "" FORCE)

add_subdirectory("${LLAMA_CPP_DIR}" llama_cpp EXCLUDE_FROM_ALL)

add_library(luna_native SHARED native_luna.cpp)
target_compile_features(luna_native PRIVATE cxx_std_17)

target_link_libraries(luna_native PRIVATE llama)

find_library(log-lib log)
target_link_libraries(luna_native PRIVATE ${log-lib})
