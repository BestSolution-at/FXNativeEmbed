cmake_minimum_required(VERSION 3.1)
project(WindowsNative CXX)


add_library(WindowsNative SHARED src/windows_native.cpp)

#target_include_directories(WindowsNative PUBLIC include)
target_include_directories(WindowsNative PRIVATE  ${CommonIncludeDirectories})

set_property(TARGET WindowsNative PROPERTY POSITION_INDEPENDENT_CODE ON)
#target_link_libraries(WindowUtil )
