cmake_minimum_required(VERSION 3.22.1)
project(luna_native)

# Prefer the repository submodule pinned in .gitmodules. An explicit
# LLAMA_CPP_DIR override remains available for Android Studio/CI builds.
# CMakeLists.txt lives at app/src/main/cpp, so four parents reach the repo root.
set(LLAMA_CPP_DIR "${CMAKE_CURRENT_LIST_DIR}/../../../../third_party/llama.cpp" CACHE PATH "Path to the pinned llama.cpp checkout")

if(NOT EXISTS "${LLAMA_CPP_DIR}/CMakeLists.txt")
    message(FATAL_ERROR "Luna native build requires the pinned third_party/llama.cpp submodule. Clone with --recurse-submodules or set LLAMA_CPP_DIR.")
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
