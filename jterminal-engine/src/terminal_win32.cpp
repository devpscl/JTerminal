#include "../include/terminal.h"
#include <Windows.h>
#include <conio.h>
#include <iostream>
#include <codecvt>
#include <locale>

#define STDOUT_HANDLE GetStdHandle(STD_OUTPUT_HANDLE)
#define STDIN_HANDLE GetStdHandle(STD_INPUT_HANDLE)

namespace jterminal {

void Terminal::create() {
  flags_ = FLAG_DEFAULT;
}

void Terminal::dispose() {
  disposed_ = true;
}

bool Terminal::isValid() {
  return !disposed_;
}

bool Terminal::isDisposed() {
  return disposed_;
}

void Terminal::attachInputPipeline(InputPipeline *input_pipeline) {

}

void Terminal::detachInputPipeline(InputPipeline *input_pipeline) {

}

void Terminal::setFlags(uint8_t flags) {
  flags_ = flags;
  update();
}

void Terminal::getFlags(uint8_t *flags_ptr) {
  *flags_ptr = flags_;
}

void Terminal::clear() {
  system("cls");
}

void Terminal::update() {
  DWORD mode = 0;
  mode |= ENABLE_EXTENDED_FLAGS;
  if(flags_ & FLAG_LINE_INPUT) {
    mode |= ENABLE_LINE_INPUT;
  }
  if(flags_ & FLAG_ECHO) {
    mode |= ENABLE_ECHO_INPUT;
  }
  if(flags_ & FLAG_SIGNAL_INPUT) {
    mode |= ENABLE_WINDOW_INPUT;
  }
  if(flags_ & FLAG_ENHANCED_INPUT) {
    mode |= ENABLE_PROCESSED_INPUT;
    mode |= ENABLE_VIRTUAL_TERMINAL_INPUT;
  }
  if(flags_ & FLAG_MOUSE_INPUT) {
    mode |= ENABLE_PROCESSED_INPUT;
    mode |= ENABLE_MOUSE_INPUT;
  }
  SetConsoleMode(STDIN_HANDLE, mode);

  mode = ENABLE_PROCESSED_OUTPUT | ENABLE_VIRTUAL_TERMINAL_PROCESSING;
  SetConsoleMode(STDOUT_HANDLE, mode);
}

void Terminal::reset(bool clear_screen) {
  flags_ = 0;
  update();
  if(clear_screen) {
    clear();
  }
}

void Terminal::write(uint8_t *bytes, size_t len) {
  char* arr = reinterpret_cast<char*>(bytes);
  std::cout.write(const_cast<const char*>(arr), len);
}

void Terminal::write(const char *cstr) {
  std::cout << cstr;
}

void Terminal::beep() {
  write("\a");
}

void Terminal::Window::setTitle(const char *cstr) {
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  std::wstring wstr = converter.from_bytes(std::string(cstr));
  SetConsoleTitleW(wstr.c_str());
}

std::string Terminal::Window::getTitle() {
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  wchar_t buf[128];
  uint8_t len = GetConsoleTitleW(buf, 128);
  std::wstring wstr(buf, len);
  std::string str = converter.to_bytes(wstr);
  return str;
}

void Terminal::Window::setDimension(const dim_t &dim) {

}

void Terminal::Window::setCursor(const pos_t &pos) {
  auto x = static_cast<SHORT>(pos.x);
  auto y = static_cast<SHORT>(pos.y);
  SetConsoleCursorPosition(STDOUT_HANDLE, {x, y});
}

void Terminal::Window::getDimension(dim_t *dim_ptr) {
  CONSOLE_SCREEN_BUFFER_INFO buffer_info;
  GetConsoleScreenBufferInfo(STDOUT_HANDLE, &buffer_info);
  uint16_t width = buffer_info.dwSize.X;
  uint16_t height = buffer_info.dwSize.Y;
  dim_ptr->width = width;
  dim_ptr->height = height;
}

bool Terminal::Window::requestCursorPosition(pos_t *pos_ptr) {
  InputPipeline pipeline(INPUT_PRIO_HIGHEST_SINGLETON, 500);
  attachInputPipeline(&pipeline);
  //TODO pipeline read cursor escape code
  detachInputPipeline(&pipeline);
  return false;
}

void Terminal::Window::setVisible(bool state) {
  HWND console_window = GetConsoleWindow();
  ShowWindow(console_window, state ? SW_SHOW : SW_HIDE);
}

bool Terminal::Window::isVisible() {
  HWND console_window = GetConsoleWindow();
  return IsWindowVisible(console_window);
}

void Terminal::Window::setGraphicUpdate(bool state) {
  HWND console_window = GetConsoleWindow();
  if(state) {
    LockWindowUpdate(nullptr);
  } else {
    LockWindowUpdate(console_window);
  }
}

void Terminal::Window::setRect(uint8_t x, uint8_t y, uint8_t width, uint8_t height) {

}

void Terminal::Window::setRectSize(uint8_t width, uint8_t height) {

}

void Terminal::Window::setRectPos(uint8_t x, uint8_t y) {

}

void Terminal::Window::getRect(uint8_t *x, uint8_t *y, uint8_t *width, uint8_t *height) {

}

bool Terminal::Window::isOnFocus() {
  HWND console_window = GetConsoleWindow();
  return GetForegroundWindow() == console_window;
}

}