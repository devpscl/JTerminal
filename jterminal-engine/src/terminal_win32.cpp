#include "../include/terminal.h"
#include <Windows.h>
#include <conio.h>
#include <iostream>
#include <codecvt>
#include <locale>
#include <sstream>
#include <bits/stdc++.h>
#include <thread>
#include "../include/escseq.h"
#include "../include/bufnio.h"

using strstream = std::stringstream;

#define STDOUT_HANDLE GetStdHandle(STD_OUTPUT_HANDLE)
#define STDIN_HANDLE GetStdHandle(STD_INPUT_HANDLE)

namespace jterminal {

void Terminal::sendInput(uint8_t *bytes, size_t len) {
  if(pipelines_.empty()) {
    return;
  }
  for (auto &pl : pipelines_) {
    if(pl == nullptr) {
      continue;
    }
    pl->write(bytes, len);
    if(pl->priority_ == INPUT_PRIO_HIGHEST_SINGLETON) {
      break;
    }
  }
}

void Terminal::threadRead() {
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  HANDLE handle = STDIN_HANDLE;
  wchar_t buf[INPUT_BUFFER_SIZE + 1];
  unsigned long input_count;
  std::unique_lock<std::mutex> lock(input_thread_mutex_);
  while(!disposed_) {
    while(!(flags_ & FLAG_ENHANCED_INPUT)) {
      input_thread_cv_.wait(lock);
    }
    ReadConsoleW(handle, buf,INPUT_BUFFER_SIZE,
                 &input_count, nullptr);
    buf[input_count] = '\0';
    std::string str = converter.to_bytes(buf);
    size_t len = str.length();
    const char* arr = str.c_str();
    uint8_t data[len];
    for(size_t idx = 0; idx < len; idx++) {
      data[idx] = arr[idx];
    }
    sendInput(data, len);
  }
}

void Terminal::create() {
  flags_ = FLAG_DEFAULT;
  update();
  input_thread_ = new std::thread(threadRead);
}

void Terminal::dispose() {
  disposed_ = true;
  input_thread_->detach();
}

bool Terminal::isValid() {
  return !disposed_;
}

bool Terminal::isDisposed() {
  return disposed_;
}

void Terminal::attachInputPipeline(InputPipeline *input_pipeline) {
  if(pipelines_.size() >= MAXIMUM_PIPELINE_COUNT) {
    return;
  }
  pipelines_.push_back(input_pipeline);
  std::sort(pipelines_.begin(), pipelines_.end(),
            [](InputPipeline* a, InputPipeline* b) {
              if(a == nullptr || b == nullptr) {
                return false;
              }
              return a->priority_ > b->priority_;
            });
}

void Terminal::detachInputPipeline(InputPipeline *input_pipeline) {
  for(uint8_t idx = 0; idx < pipelines_.size(); idx++) {
    if(pipelines_[idx] == input_pipeline) {
      pipelines_.erase(pipelines_.begin() + idx);
      return;
    }
  }
}

void Terminal::setFlags(uint8_t flags) {
  flags_ = flags;
  input_thread_cv_.notify_all();
  update();
}

void Terminal::getFlags(uint8_t *flags_ptr) {
  *flags_ptr = flags_;
}

void Terminal::clear() {
  system("cls");
  update();
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
  SetConsoleMode(STDIN_HANDLE, mode);

  mode = ENABLE_PROCESSED_OUTPUT | ENABLE_VIRTUAL_TERMINAL_PROCESSING;
  SetConsoleMode(STDOUT_HANDLE, mode);

  strstream ec_buf;
  ec_buf << (cursor_flags_ & CURSOR_FLAG_VISIBLE ? ESC_ENABLE_CURSOR : ESC_DISABLE_CURSOR);
  ec_buf << (cursor_flags_ & CURSOR_FLAG_BLINKING
            ? ESC_ENABLE_CURSOR_BLINKING : ESC_DISABLE_CURSOR_BLINKING);
  ec_buf << (flags_ & FLAG_MOUSE_EXTENDED_INPUT ? ESC_ENABLE_MOUSE_EXTENDED :
             ((flags_ & FLAG_MOUSE_INPUT) ? ESC_ENABLE_MOUSE : ESC_DISABLE_MOUSE));
  write(ec_buf.str().c_str());
}

void Terminal::reset(bool clear_screen) {
  flags_ = FLAG_DEFAULT;
  cursor_flags_ = CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING;
  input_thread_cv_.notify_all();
  write("\033[0m");
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
  if(cstr == nullptr) {
    cstr = "JTerminal";
  }
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
  auto x = static_cast<SHORT>(dim.width);
  auto y = static_cast<SHORT>(dim.height);
  HANDLE handle = STDOUT_HANDLE;
  CONSOLE_SCREEN_BUFFER_INFO buffer_info;
  if(!GetConsoleScreenBufferInfo(handle, &buffer_info)) {
    throw std::runtime_error("Unable to get console buffer");
  }
  COORD size = {x, y};
  SetConsoleScreenBufferSize(handle, size);
  _SMALL_RECT rect{0, 0, (SHORT)(x - 1), (SHORT)(y - 1)};
  SetConsoleWindowInfo(handle, true, &rect);
  SetConsoleScreenBufferSize(handle, size);
}

void Terminal::Window::setCursor(const pos_t &pos) {
  auto x = static_cast<SHORT>(pos.x - 1);
  auto y = static_cast<SHORT>(pos.y - 1);
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
  uint8_t buf[8];
  attachInputPipeline(&pipeline);
  write(ESC_CURSOR_REQUEST);
  size_t len = pipeline.read(buf, 8);
  if(len == -1) {
    detachInputPipeline(&pipeline);
    return false;
  }
  StringBuffer strbuf(buf, len);
  if(strbuf.equal("\u001B[", EN_FLAG_SKIP_IF_TRUE)) {
    short y = strbuf.readNumberFormat16();
    strbuf.skip();
    short x = strbuf.readNumberFormat16();

    if(strbuf.equal('R', EN_FLAG_SKIP_IF_TRUE))  {
      detachInputPipeline(&pipeline);
      pos_ptr->x = x;
      pos_ptr->y = y;
      return true;
    }
  }
  detachInputPipeline(&pipeline);
  return false;
}

void Terminal::Window::setCursorFlags(uint8_t flags) {
  cursor_flags_ = flags;
  update();
}

uint8_t Terminal::Window::getCursorFlags() {
  return cursor_flags_;
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
  HWND window = GetConsoleWindow();
  MoveWindow(window, x, y, width, height, false);
}

void Terminal::Window::setRectSize(uint8_t width, uint8_t height) {
  HWND window = GetConsoleWindow();
  RECT rect;
  GetWindowRect(window, &rect);
  int x = rect.left;
  int y = rect.top;
  MoveWindow(window, x, y, width, height, false);
}

void Terminal::Window::setRectPos(uint8_t x, uint8_t y) {
  HWND window = GetConsoleWindow();
  RECT rect;
  GetWindowRect(window, &rect);
  int width = rect.right;
  int height = rect.bottom;
  MoveWindow(window, x, y, width, height, false);
}

void Terminal::Window::getRect(uint8_t *x, uint8_t *y, uint8_t *width, uint8_t *height) {
  HWND window = GetConsoleWindow();
  RECT rect;
  GetWindowRect(window, &rect);
  *x = rect.left;
  *y = rect.top;
  *width = (rect.right - rect.left);
  *height = (rect.bottom - rect.top);
}

bool Terminal::Window::isOnFocus() {
  HWND console_window = GetConsoleWindow();
  return GetForegroundWindow() == console_window;
}
}