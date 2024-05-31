cmake_minimum_required(VERSION 3.22)
project(jterminal_engine)

set(CMAKE_CXX_STANDARD 17)

#####################################
# Define files
#####################################

set(HEADER_FILES
        include/termdef.h
        include/terminal.h
        include/terminput.h
        include/bufnio.h
        include/escseq.h
)
set(SOURCE_FILES
        src/input_pipeline.cpp
        src/memory_buffer.cpp
        src/string_buffer.cpp
)
set(NATIVE_SOURCE_FILES_WIN
        src/terminal_win32.cpp
)
set(NATIVE_SOURCE_FILES_UNIX

)
set(JNI_FILES )
set(TEST_FILES test/main.cpp)
#####################################
# Include and build
#####################################
IF(WIN32)
    message("JTerminal target os: Windows")
    set(SOURCE_FILES ${SOURCE_FILES} ${NATIVE_SOURCE_FILES_WIN})
ELSEIF(UNIX)
    message("JTerminal target os: UNIX (linux,macos)")
    set(SOURCE_FILES ${SOURCE_FILES} ${NATIVE_SOURCE_FILES_UNIX})
ENDIF()

IF(DEBUG_MODE)
    message("Debug mode is enabled!")
    add_executable(jterminal_engine ${TEST_FILES} ${HEADER_FILES} ${SOURCE_FILES} ${JNI_FILES})
ELSE()
    add_library(jterminal_engine ${HEADER_FILES} ${SOURCE_FILES} ${JNI_FILES})
ENDIF ()


