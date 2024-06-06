#ifndef JTERMINAL_ENGINE_INCLUDE_JTERMINAL_H_
#define JTERMINAL_ENGINE_INCLUDE_JTERMINAL_H_
#include "termdef.h"
#include "terminput.h"
#include <string>
#include <vector>
#include <thread>

namespace jterminal {

class Terminal {
  inline static volatile bool               disposed_ = false;
  inline static volatile uint8_t            flags_ = 0;
  inline static std::vector<InputPipeline*> pipelines_;
  inline static uint8_t                     cursor_flags_ = CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING;
  inline static uint8_t                     option_byte_ = 0;

  inline static std::condition_variable     input_thread_cv_;
  inline static std::mutex                  input_thread_mutex_;
  inline static std::thread*                input_thread_;
  inline static std::condition_variable     window_thread_cv_;
  inline static std::mutex                  window_thread_mutex_;
  inline static std::thread*                window_thread_;

  static void sendInput(uint8_t* bytes, size_t len);

  static void threadRead();

#if defined(TERMINAL_WIN)

  static void threadWindowEvent();

#elif defined(TERMINAL_UNIX)

  static void signalWindowEvent(int);

#endif

 public:

  static void create(uint8_t mode = TERMINAL_MODE_PERFORMANCE);

  static void dispose();

  static bool isValid();

  static bool isDisposed();

  static void attachInputPipeline(InputPipeline* input_pipeline);

  static void detachInputPipeline(InputPipeline* input_pipeline);

  static void setFlags(uint8_t flags);

  static void getFlags(uint8_t* flags_ptr);

  static void clear();

  static void update();

  static void reset(bool clear_screen);

  static void write(uint8_t* bytes, size_t len);

  static void write(const char* cstr);

  static void beep();

  union Window {

    static void setTitle(const char* cstr);

    static std::string getTitle();

    static void setDimension(const dim_t& dim);

    static void setCursor(const pos_t& pos);

    static void getDimension(dim_t* dim_ptr);

    static bool requestCursorPosition(pos_t* pos_ptr);

    static void setCursorFlags(uint8_t flags);

    static uint8_t getCursorFlags();

#ifdef TERMINAL_WIN

    static void setVisible(bool state);

    static bool isVisible();

    static void setGraphicUpdate(bool state);

    static void setRect(uint8_t x, uint8_t y, uint8_t width, uint8_t height);

    static void setRectSize(uint8_t width, uint8_t height);

    static void setRectPos(uint8_t x, uint8_t y);

    static void getRect(uint8_t* x, uint8_t* y, uint8_t* width, uint8_t* height);

    static bool isOnFocus();

#endif

  };

};

}

#endif //JTERMINAL_ENGINE_INCLUDE_JTERMINAL_H_
