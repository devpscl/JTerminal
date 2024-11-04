#ifndef JTERMINAL_ENGINE_INCLUDE_JTERMINAL_H_
#define JTERMINAL_ENGINE_INCLUDE_JTERMINAL_H_
#include "termdef.h"
#include "terminput.h"
#include <string>
#include <vector>
#include <thread>
#include <sstream>

namespace jterminal {

using strstream = std::stringstream;

using InputStreamPtr = InputStream*;

#if TERMINAL_WIN
#define STDOUT_HANDLE GetStdHandle(STD_OUTPUT_HANDLE)
#define STDIN_HANDLE GetStdHandle(STD_INPUT_HANDLE)
#endif

class Terminal {
  inline static volatile uint8_t              flags_ = FLAG_DEFAULT;
  inline static uint8_t                       buffer_ = BUFFER_MAIN;
  inline static std::vector<InputStreamPtr>   input_stream_vector_;
  inline static Settings                      settings_;
  inline static std::condition_variable       input_thread_cv_;
  inline static std::mutex                    input_thread_mutex_;
  inline static std::thread*                  input_thread_;
  inline static bool                          enabled_ = false;
  inline static bool                          closed_ = false;

#if defined(TERMINAL_WIN)

  inline static std::condition_variable     window_thread_cv_;
  inline static std::mutex                  window_thread_mutex_;
  inline static std::thread*                window_thread_;

  static void threadWindowInput();

  static void signalSigAbrt(int);

  static void renewStdin();

#elif defined(TERMINAL_UNIX)

  static void signalWindowInput(int);

  static void signalSigQuit(int);

  static void signalSigTstp(int);

  static void setNonblocking(bool non_block);

#endif

  static void signalSigInt(int);

  static void sortInputStreamVector();

  static void writeInput(uint8_t *bytes, size_t len);

  static void threadInputLoop();

  static void writeFlags(uint8_t flags, uint8_t cursor_flags);

  static void resetAll();

  static InputStreamPtr newSingletonInputStream(size_t capacity = 1024);

  friend class InputStream;
  friend class Window;

public:

  static void create(Settings settings = {});

  static void write(const char* cstr);

  static void write(void* bytes, size_t len);

  static void shutdown();

  static void setFlags(uint8_t flags);

  static void addFlags(uint8_t flags);

  static void removeFlags(uint8_t flags);

  static void getFlags(uint8_t* flags);

  static void clear();

  static void update();

  static void reset(bool clear_screen = false);

  static void beep();

  static void setBuffer(uint8_t buffer);

  static uint8_t getBuffer();

  static InputStreamPtr newInputStream(InputStreamPriority prio, size_t capacity = 1024);

  static void disposeInputStream(InputStreamPtr input_stream);

  static size_t readLine(void* bytes, size_t len);

  static bool isEnabled();

  static bool isClosed();

  static Settings getSettings();

  static void joinFutureClose();

};

class Window {

  inline static uint8_t                 cursor_flags_ = CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING;
  inline static std::string             title_ = TERMINAL_DEFAULT_TITLE;

  friend class Terminal;

public:

  static void setTitle(const char* cstr);

  static std::string getTitle();

  static void setDimension(const dim_t& dim);

  static void setCursor(const pos_t& pos);

  static void getDimension(dim_t* dim_ptr);

  static bool requestCursorPosition(pos_t* pos_ptr);

  static void setCursorFlags(uint8_t flags);

  static uint8_t getCursorFlags();

};

}

#endif //JTERMINAL_ENGINE_INCLUDE_JTERMINAL_H_