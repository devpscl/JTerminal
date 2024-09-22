#ifdef TERMINAL_WIN

#include "../../include/terminal.h"
#include <Windows.h>
#include <codecvt>
#include <locale>

namespace jterminal {

Window::Window(Terminal* instance) {
  instance_ = instance;
}

void Window::setup() {
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  std::wstring wstr = converter.from_bytes(title_);
  SetConsoleTitleW(wstr.c_str());
}

bool Window::isActive() const {
  return TermEngine::isActive(instance_);
}

void Window::setTitle(const char* cstr) {
  if(cstr == nullptr) {
    cstr = TERMINAL_DEFAULT_TITLE;
  }
  title_ = std::string(cstr);
  if(!isActive()) {
    return;
  }
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  std::wstring wstr = converter.from_bytes(title_);
  SetConsoleTitleW(wstr.c_str());
}

std::string Window::getTitle() {
  return title_;
}

void Window::setDimension(const dim_t& dim) {
  if(!isActive()) {
    return;
  }
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

void Window::setCursor(const pos_t& pos) {
  auto x = static_cast<SHORT>(pos.x - 1);
  auto y = static_cast<SHORT>(pos.y - 1);
  SetConsoleCursorPosition(STDOUT_HANDLE, {x, y});
}

void Window::getDimension(dim_t* dim_ptr) {
  TermEngine::readConsoleDim(dim_ptr);
}

bool Window::requestCursorPosition(pos_t* pos_ptr) {
  if(!isActive()) {
    return false;
  }
  uint8_t buf[8];
  InputStreamPtr input_stream = instance_->newSingletonInputStream(24);
  TermEngine::write(ESC_CURSOR_REQUEST);
  Settings settings = TermEngine::getSettings();
  int ms = settings.mode == TERMINAL_MODE_PERFORMANCE ? 10 : 500;
  size_t len = input_stream->read(buf, 8, std::chrono::milliseconds(ms));
  instance_->disposeInputStream(input_stream);

  if(len == -1 || len == 0) {
    return false;
  }
  ESCBuffer buffer(buf, len);
  if(!buffer.skipToNextSequence()) {
    return false;
  }
  CSISequenceString sequence_string;
  if(!buffer.readSequence(&sequence_string)) {
    return false;
  }
  if(sequence_string.endSymbol() == 'R' && sequence_string.paramCount() == 2) {
    pos_ptr->x = sequence_string[1];
    pos_ptr->y = sequence_string[0];
    return true;
  }
  return false;
}

void Window::setCursorFlags(uint8_t flags) {
  cursor_flags_ = flags;
  instance_->update();
}

uint8_t Window::getCursorFlags() {
  return cursor_flags_;
}

void Window::setVisible(bool state) {
  HWND console_window = GetConsoleWindow();
  ShowWindow(console_window, state ? SW_SHOW : SW_HIDE);
}

bool Window::isVisible() {
  HWND console_window = GetConsoleWindow();
  return IsWindowVisible(console_window);
}

void Window::setGraphicUpdate(bool state) {
  HWND console_window = GetConsoleWindow();
  if(state) {
    LockWindowUpdate(nullptr);
  } else {
    LockWindowUpdate(console_window);
  }
}

void Window::setRect(uint8_t x, uint8_t y, uint8_t width, uint8_t height) {
  HWND window = GetConsoleWindow();
  MoveWindow(window, x, y, width, height, false);
}

void Window::setRectSize(uint8_t width, uint8_t height) {
  HWND window = GetConsoleWindow();
  RECT rect;
  GetWindowRect(window, &rect);
  int x = rect.left;
  int y = rect.top;
  MoveWindow(window, x, y, width, height, false);
}

void Window::setRectPos(uint8_t x, uint8_t y) {
  HWND window = GetConsoleWindow();
  RECT rect;
  GetWindowRect(window, &rect);
  int width = rect.right;
  int height = rect.bottom;
  MoveWindow(window, x, y, width, height, false);
}

void Window::getRect(uint8_t* x, uint8_t* y, uint8_t* width, uint8_t* height) {
  HWND window = GetConsoleWindow();
  RECT rect;
  GetWindowRect(window, &rect);
  *x = rect.left;
  *y = rect.top;
  *width = (rect.right - rect.left);
  *height = (rect.bottom - rect.top);
}

bool Window::isOnFocus() {
  HWND console_window = GetConsoleWindow();
  return GetForegroundWindow() == console_window;
}

}
#endif