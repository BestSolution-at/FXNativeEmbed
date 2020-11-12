cmake_minimum_required(VERSION 3.1)
project(swing-embed)

find_package(JNI REQUIRED)
list(APPEND CommonIncludeDirectories ${JNI_INCLUDE_DIRS})

# configuring msvc to include the runtime
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} /MT")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} /MT")


add_subdirectory(WindowsNative)
