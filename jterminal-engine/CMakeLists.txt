cmake_minimum_required(VERSION 3.28)
project(jterminal_engine)

set(CMAKE_CXX_STANDARD 17)

set(HEADER_FILES
        include/termdef.h
        include/terminal.h
        include/terminput.h
        include/bufnio.h
)
set(SOURCE_FILES
        src/terminal_win32.cpp
        src/input_pipeline.cpp
        src/memory_buffer.cpp
)
set(JNI_FILES )

IF(DEFINED debug)
    set(TEST_FILES test/main.cpp)
    add_executable(jterminal_engine ${TEST_FILES} ${HEADER_FILES} ${SOURCE_FILES} ${JNI_FILES})
ELSE()
    add_library(jterminal_engine ${HEADER_FILES} ${SOURCE_FILES} ${JNI_FILES})
ENDIF ()


