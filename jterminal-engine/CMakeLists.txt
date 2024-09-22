cmake_minimum_required(VERSION 3.22)
project(jterminal_engine)

set(CMAKE_CXX_STANDARD 20)

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
        src/input_stream.cpp
        src/memory_buffer.cpp
        src/string_buffer.cpp
        src/escape_buffer.cpp
        src/hash_input_translator.cpp
        src/input_hasher.cpp
        src/queued_buffer.cpp
        src/escape_input_translator.cpp
        src/input_length_scanner.cpp
)
set(NATIVE_SOURCE_FILES_GLOBAL
    src/global/terminal.cpp
)
set(NATIVE_SOURCE_FILES_WIN
        src/win32/terminal_win32.cpp
        src/win32/termengine_win32.cpp
        src/win32/window_win32.cpp
)
set(NATIVE_SOURCE_FILES_UNIX
        src/unix/terminal_unix.cpp
        src/unix/termengine_unix.cpp
        src/unix/window_unix.cpp
)
set(JNI_FILES
        jni/include/AbstractNativeTerminal.h
        jni/include/NativeTerminalInputStream.h
        jni/src/AbstractNativeTerminal.cpp
        jni/src/NativeTerminalInputStream.cpp
        jni/include/jni_helper.h)
set(TEST_FILES test/main.cpp)

#####################################
# Include and build
#####################################
message("Java home: ${JAVA_HOME}")
IF(WIN32)
    message("JTerminal target os: Windows")
    set(SOURCE_FILES ${SOURCE_FILES} ${NATIVE_SOURCE_FILES_GLOBAL} ${NATIVE_SOURCE_FILES_WIN})
    add_compile_definitions(TERMINAL_WIN)
    include_directories("${JAVA_HOME}/include" "${JAVA_HOME}/include/win32" "${JAVA_HOME}/include/win32/bridge")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -static")
ELSEIF(UNIX)
    message("JTerminal target os: UNIX (linux,macos)")
    set(SOURCE_FILES ${SOURCE_FILES} ${NATIVE_SOURCE_FILES_GLOBAL} ${NATIVE_SOURCE_FILES_UNIX})
    add_compile_definitions(TERMINAL_UNIX)
    include_directories("${JAVA_HOME}/include" "${JAVA_HOME}/include/linux")
ENDIF()

IF(DEBUG_MODE)
    message("Debug mode is enabled!")
    add_executable(jterminal_engine ${TEST_FILES} ${HEADER_FILES} ${SOURCE_FILES} ${JNI_FILES})
ELSE()
    add_library(jterminal_engine SHARED ${HEADER_FILES} ${SOURCE_FILES} ${JNI_FILES})
ENDIF ()
set_target_properties(${PROJECT_NAME} PROPERTIES PREFIX "")
set_target_properties(${PROJECT_NAME} PROPERTIES OUTPUT_NAME "termengine")


