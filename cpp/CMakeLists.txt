#Specify the version of CMAKE >=
cmake_minimum_required(VERSION 2.8)
#Name your project here
SET(CMAKE_C_COMPILER "/usr/bin/clang")
SET(CMAKE_CXX_COMPILER "/usr/bin/clang++")
SET(CMAKE_CXX_FLAGS "-std=c++11")
project(CMF)


FIND_LIBRARY(AMPQCLIENT_H_PATH SimpleAmqpClient)
FIND_PACKAGE(Boost)

SET(CMF_LIB_SRCS
        include/CMF/EnvelopeReceiver.h
        include/CMF/EnvelopeSender.h
        include/CMF/Registration.h
        include/CMF/Envelope.h
        src/CMF/Envelope.cpp
        src/CMF/Example.cpp
)
ADD_EXECUTABLE(CMF ${CMF_LIB_SRCS})
target_link_libraries(CMF ${AMPQCLIENT_H_PATH})
