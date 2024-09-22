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

#if TERMINAL_WIN
#define STDOUT_HANDLE GetStdHandle(STD_OUTPUT_HANDLE)
#define STDIN_HANDLE GetStdHandle(STD_INPUT_HANDLE)
#endif


class Window;
class Terminal;

using InputStreamPtr  = InputStream*;
using WindowPtr       = Window*;
using TerminalPtr     = Terminal*;

class TermEngine {

  inline static TerminalPtr                 instance_;
  inline static Settings                    settings_;
  inline static std::condition_variable     input_thread_cv_;
  inline static std::mutex                  input_thread_mutex_;
  inline static std::thread*                input_thread_;
  inline static uint8_t                     buffer_channel_ = BUFFER_MAIN;
  inline static bool                        enabled_ = false;
  inline static bool                        closed_ = false;
  inline static uint8_t                     flags_memory_ = FLAG_DEFAULT;

  static void threadInputLoop();

#if defined(TERMINAL_WIN)

  inline static std::condition_variable     window_thread_cv_;
  inline static std::mutex                  window_thread_mutex_;
  inline static std::thread*                window_thread_;

  static void threadWindowInput();

  static void signalSigAbrt(int);

#elif defined(TERMINAL_UNIX)

  static void signalWindowInput(int);



  static void signalSigQuit(int);

  static void signalSigTstp(int);

#endif

  static void signalSigInt(int);

 public:

  static void create(Settings settings = {});

  static void set(TerminalPtr instance);

  static void get(TerminalPtr instance);

  static void write(const char* cstr);

  static void write(void *bytes, size_t len);

  static void shutdown();

  static void resetAll();

  static void writeFlags(uint8_t flags, uint8_t cursor_flags);

  static void setBufferChannel(uint8_t buf_ch);

  static bool isEnabled();

  static bool isClosed();

  static bool isActive(Terminal* instance);

  static void readConsoleDim(dim_t* dim);

  static Settings getSettings();

  static void waitForClose();

};

class Terminal {
  volatile uint8_t            flags_ = FLAG_DEFAULT;
  uint8_t                     buffer_ = BUFFER_MAIN;

  std::vector<InputStreamPtr> input_stream_vector_;
  WindowPtr                   window_;

  InputStreamPtr newSingletonInputStream(size_t capacity = 1024);

  void sortInputStreamVector();

  void writeInput(uint8_t *bytes, size_t len);

  friend class Window;
  friend class TermEngine;
  friend class InputStream;

 public:

  Terminal();

  ~Terminal();

  void setFlags(uint8_t flags);

  void getFlags(uint8_t* flags_ptr);

  void clear();

  void update();

  void reset(bool clear_screen);

  void beep();

  [[nodiscard]] uint8_t getBuffer() const;

  bool isActive();

  InputStreamPtr newInputStream(InputStreamPriority prio, size_t capacity = 1024);

  void disposeInputStream(InputStreamPtr input_stream);

  WindowPtr getWindow();

};

class Window {

  uint8_t                     cursor_flags_ = CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING;
  std::string                 title_ = TERMINAL_DEFAULT_TITLE;
  TerminalPtr                 instance_;

  explicit Window(TerminalPtr instance);

  void setup();

  friend class TermEngine;
  friend class Terminal;

public:

  [[nodiscard]] bool isActive() const;

  void setTitle(const char* cstr);

  std::string getTitle();

  void setDimension(const dim_t& dim);

  void setCursor(const pos_t& pos);

  void getDimension(dim_t* dim_ptr);

  bool requestCursorPosition(pos_t* pos_ptr);

  void setCursorFlags(uint8_t flags);

  uint8_t getCursorFlags();

#ifdef TERMINAL_WIN

  void setVisible(bool state);

  bool isVisible();

  void setGraphicUpdate(bool state);

  void setRect(uint8_t x, uint8_t y, uint8_t width, uint8_t height);

  void setRectSize(uint8_t width, uint8_t height);

  void setRectPos(uint8_t x, uint8_t y);

  void getRect(uint8_t* x, uint8_t* y, uint8_t* width, uint8_t* height);

  bool isOnFocus();

#endif

};

}

#endif //JTERMINAL_ENGINE_INCLUDE_JTERMINAL_H_