#ifdef TERMINAL_WIN

#include "../../include/terminal.h"
#include <Windows.h>
#include <codecvt>
#include <locale>

namespace jterminal {

void Window::setTitle(const char* cstr) {
  if(cstr == nullptr) {
    cstr = TERMINAL_DEFAULT_TITLE;
  }
  title_ = std::string(cstr);
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  std::wstring wstr = converter.from_bytes(title_);
  SetConsoleTitleW(wstr.c_str());
}

std::string Window::getTitle() {
  return title_;
}

void Window::setDimension(const dim_t& dim) {
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
  CONSOLE_SCREEN_BUFFER_INFO buffer_info;
  GetConsoleScreenBufferInfo(STDOUT_HANDLE, &buffer_info);
  uint16_t height = buffer_info.srWindow.Bottom - buffer_info.srWindow.Top  + 1;
  uint16_t width = buffer_info.srWindow.Right  - buffer_info.srWindow.Left + 1;

  dim_ptr->width = width;
  dim_ptr->height = height;
}

bool Window::requestCursorPosition(pos_t* pos_ptr) {
  uint8_t buf[8];
  InputStreamPtr input_stream = Terminal::newSingletonInputStream(24);
  Terminal::write(ESC_CURSOR_REQUEST);
  Settings settings = Terminal::getSettings();
  int ms = settings.mode == TERMINAL_MODE_PERFORMANCE ? 10 : 500;
  size_t len = input_stream->read(buf, 8, std::chrono::milliseconds(ms));
  Terminal::disposeInputStream(input_stream);

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
  Terminal::update();
}

uint8_t Window::getCursorFlags() {
  return cursor_flags_;
}

}
#endif