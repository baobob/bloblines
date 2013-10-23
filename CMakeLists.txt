cmake_minimum_required(VERSION 2.8)
project(bloodlines)

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++0x -Wall -Wextra -Werror")


add_executable(bloodlines
  main.cpp
)
