include_directories(${GTEST_INCLUDE_DIRS} ${COMMON_INCLUDES})
INCLUDE_DIRECTORIES(..src/)

add_executable(test_api "envelope_test.cpp")
target_link_libraries(test_api CMF ${GTEST_BOTH_LIBRARIES} pthread ${Boost_LIBRARIES})
add_test(test_api test_api)
