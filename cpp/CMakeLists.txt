#Specify the version of CMAKE >=
cmake_minimum_required(VERSION 2.8)
#Name your project here
SET(CMAKE_C_COMPILER "/usr/bin/clang")
SET(CMAKE_CXX_COMPILER "/usr/bin/clang++")
project(CMF)


FIND_LIBRARY(AMPQCLIENT_H_PATH SimpleAmqpClient)
SET(CMF_LIB_SRCS
	src/hello.cpp
)
ADD_EXECUTABLE(CMF ${CMF_LIB_SRCS})
