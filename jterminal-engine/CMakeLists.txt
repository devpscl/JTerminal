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
        src/escape_buffer.cpp
        src/hash_input_translator.cpp
        src/input_hasher.cpp
        src/terminal_unix.cpp
        src/terminal_win32.cpp
        src/queued_buffer.cpp
        src/escape_input_translator.cpp
        src/input_length_scanner.cpp
)
set(NATIVE_SOURCE_FILES_WIN
        src/terminal_win32.cpp
)
set(NATIVE_SOURCE_FILES_UNIX
        src/terminal_unix.cpp
)
set(JNI_FILES )
set(TEST_FILES test/main.cpp)
#####################################
# Include and build
#####################################
IF(WIN32)
    message("JTerminal target os: Windows")
    set(SOURCE_FILES ${SOURCE_FILES} ${NATIVE_SOURCE_FILES_WIN})
    add_compile_definitions(TERMINAL_WIN)
ELSEIF(UNIX)
    message("JTerminal target os: UNIX (linux,macos)")
    set(SOURCE_FILES ${SOURCE_FILES} ${NATIVE_SOURCE_FILES_UNIX})
    add_compile_definitions(TERMINAL_UNIX)
ENDIF()

IF(DEBUG_MODE)
    message("Debug mode is enabled!")
    add_executable(jterminal_engine ${TEST_FILES} ${HEADER_FILES} ${SOURCE_FILES} ${JNI_FILES})
ELSE()
    add_library(jterminal_engine ${HEADER_FILES} ${SOURCE_FILES} ${JNI_FILES})
ENDIF ()


