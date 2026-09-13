cmake_minimum_required(VERSION 3.22.1)
project(luna_native)

# Native boundary for the future llama.cpp integration.
# llama.cpp itself is intentionally not vendored here; it should be pinned
# as a reviewed source dependency before production builds.

add_library(luna_native SHARED native_luna.cpp)

target_compile_features(luna_native PRIVATE cxx_std_17)

find_library(log-lib log)
target_link_libraries(luna_native ${log-lib})
