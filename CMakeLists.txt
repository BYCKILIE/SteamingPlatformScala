cmake_minimum_required(VERSION 3.26)

get_filename_component(PROJECT_DIR ${CMAKE_CURRENT_SOURCE_DIR} NAME)
project(${PROJECT_DIR})

set(CMAKE_CXX_STANDARD 17)

file(GLOB HEADER_FILES "*.h" "*.hpp")
file(GLOB SOURCE_FILES "*.cpp")

include_directories(C:/opencv/build/include)
if (CMAKE_BUILD_TYPE MATCHES "Debug")
    link_directories(C:/opencv/build/x64/vc16/lib)
elseif (CMAKE_BUILD_TYPE MATCHES "Release")
    link_directories(C:/opencv/build/x64/vc16/lib)
endif ()

add_executable(${PROJECT_NAME} ${HEADER_FILES} ${SOURCE_FILES})

target_include_directories(${PROJECT_NAME} PRIVATE ${PROJECT_NAME})

if (CMAKE_BUILD_TYPE MATCHES "Debug")
    target_link_libraries(${PROJECT_NAME} opencv_world490d.lib)
elseif (CMAKE_BUILD_TYPE MATCHES "Release")
    target_link_libraries(${PROJECT_NAME} opencv_world490.lib)
endif ()