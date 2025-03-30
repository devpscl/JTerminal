
#ifdef TERMINAL_WIN
#include "../../include/terminal.h"
#include <csignal>
#include <Windows.h>
#include <conio.h>
#include <iostream>
#include <codecvt>
#include <locale>
#include <io.h>

namespace jterminal {

void Terminal::threadWindowInput() {
  std::unique_lock lock(window_thread_mutex_);
  uint16_t delay_millis = settings_.mode == TERMINAL_MODE_EFFICIENCY ? 1000 : 200;
  dim_t old_dim;
  dim_t current_dim;
  Window::getDimension(&current_dim);
  ESCBuffer esc_buffer(24);
  while(!closed_) {
    while(!(flags_ & FLAG_WINDOW_INPUT)) {
      window_thread_cv_.wait(lock);
    }
    std::this_thread::sleep_for(std::chrono::milliseconds(delay_millis));
    Window::getDimension(&current_dim);
    if(current_dim != old_dim) {
      if(old_dim.width == 0 && old_dim.height == 0) {
        old_dim = current_dim;
        continue;
      }
      esc_buffer.reset();
      CSISequence sequence = CSI_SEQUENCE_PRIVATE('=','W',current_dim.width, current_dim.height,
                                                  old_dim.width, old_dim.height);
      esc_buffer.writeSequence(sequence);
      old_dim = current_dim;
      writeInput(esc_buffer.ptr(), esc_buffer.cursor());
    }
  }
}

void Terminal::threadInputLoop() {
  HANDLE handle = STDIN_HANDLE;
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  wchar_t buf[INPUT_BUFFER_SIZE + 1];
  INPUT_RECORD input_buf[INPUT_BUFFER_SIZE + 1];
  DWORD event_count;
  unsigned long input_count;
  std::unique_lock lock(input_thread_mutex_);
  while(!closed_) {
    while(flags_ & FLAG_LINE_INPUT || !(flags_ & FLAG_EXTENDED_INPUT)) {
      input_thread_cv_.wait(lock);
    }
    if(WaitForSingleObject(handle, 100)) {
      continue;
    }
    if(flags_ & FLAG_LINE_INPUT || !(flags_ & FLAG_EXTENDED_INPUT)) {
      continue;
    }
    input_count = 0;
    ReadConsoleInputW(handle, input_buf, INPUT_BUFFER_SIZE, &event_count);
    for(int idx = 0; idx < event_count; idx++) {
      if(input_buf[idx].EventType == KEY_EVENT) {
        buf[idx] = input_buf[idx].Event.KeyEvent.uChar.UnicodeChar;
        input_count++;
      }
    }

    //ReadConsoleW(handle, buf, INPUT_BUFFER_SIZE,
    //             &input_count, nullptr);
    //input_count = winRead(buf, INPUT_BUFFER_SIZE);
    buf[input_count] = 0;
    std::string str = converter.to_bytes(buf);
    size_t len = str.length();
    const char* arr = str.c_str();
    uint8_t data[len];
    for(size_t idx = 0; idx < len; idx++) {
      data[idx] = arr[idx];
    }
    writeInput(data, len);
  }

}

void Terminal::renewStdin() {
  CloseHandle(STDIN_HANDLE);
  HANDLE hStdin = CreateFileW(L"CONIN$", GENERIC_READ | GENERIC_WRITE, 0,
        nullptr, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL,
        nullptr);
  SetStdHandle(STD_INPUT_HANDLE, hStdin);
}

void Terminal::writeFlags(uint8_t flags, uint8_t cursor_flags) {
  DWORD mode = 0;
  mode |= ENABLE_EXTENDED_FLAGS;
  if(flags & FLAG_LINE_INPUT) {
    mode |= ENABLE_LINE_INPUT | ENABLE_AUTO_POSITION | ENABLE_INSERT_MODE | ENABLE_QUICK_EDIT_MODE;
  }
  if(flags & FLAG_ECHO) {
    mode |= ENABLE_ECHO_INPUT;
  }
  if(flags & FLAG_SIGNAL_INPUT) {
    mode |= ENABLE_WINDOW_INPUT;
  }
  if(flags & FLAG_EXTENDED_INPUT) {
    mode |= ENABLE_PROCESSED_INPUT;
    if(!(flags & FLAG_LINE_INPUT)) {
      mode |= ENABLE_VIRTUAL_TERMINAL_INPUT;
    }
  }
  SetConsoleMode(STDIN_HANDLE, mode);

  mode = ENABLE_PROCESSED_OUTPUT | ENABLE_VIRTUAL_TERMINAL_PROCESSING | ENABLE_WRAP_AT_EOL_OUTPUT;
  SetConsoleMode(STDOUT_HANDLE, mode);

  strstream ec_buf;
  ec_buf << (cursor_flags & CURSOR_FLAG_VISIBLE ? ESC_ENABLE_CURSOR : ESC_DISABLE_CURSOR);
  ec_buf << (cursor_flags & CURSOR_FLAG_BLINKING
             ? ESC_ENABLE_CURSOR_BLINKING : ESC_DISABLE_CURSOR_BLINKING);
  ec_buf << (flags & FLAG_MOUSE_EXTENDED_INPUT ? ESC_ENABLE_MOUSE_EXTENDED :
             flags & FLAG_MOUSE_INPUT ? ESC_ENABLE_MOUSE : ESC_DISABLE_MOUSE);
  write(ec_buf.str().c_str());
  input_thread_cv_.notify_all();
  window_thread_cv_.notify_all();
}

void Terminal::resetAll() {
  input_thread_cv_.notify_all();
  write("\033[0m");
  writeFlags(FLAG_DEFAULT, CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING);
  setBuffer(BUFFER_MAIN);
}

void Terminal::signalSigInt(int signum) {
  shutdown();
  exit(signum);
}

void Terminal::signalSigAbrt(int signum) {
  shutdown();
  abort();
}

void Terminal::create(Settings settings) {
  settings_ = settings;
  enabled_ = true;
  input_thread_ = new std::thread(threadInputLoop);
  window_thread_ = new std::thread(threadWindowInput);
  signal(SIGINT, signalSigInt);
  signal(SIGABRT, signalSigAbrt);
  update();
  SetConsoleCP(CP_UTF8);
  SetConsoleOutputCP(CP_UTF8);
}

void Terminal::write(const char* cstr) {
  fwrite(cstr, strlen(cstr), 1, stdout);
  fflush(stdout);
}

void Terminal::write(void* bytes, size_t len) {
  fwrite(bytes, len, 1, stdout);
  fflush(stdout);
}

void Terminal::shutdown() {
  if(closed_) {
    return;
  }
  closed_ = true;
  resetAll();
  input_thread_->detach();
  window_thread_->detach();
}

void Terminal::clear() {
  system("cls");
  update();
}

size_t Terminal::readLine(void* vptr, size_t len) {
  if(!(flags_ & FLAG_LINE_INPUT)) {
    return 0;
  }
  auto* bytes = static_cast<uint8_t*>(vptr);
  HANDLE handle = STDIN_HANDLE;
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  unsigned long input_count;
  wchar_t buf[1024];
  ReadConsoleW(handle, buf, 1023, &input_count, nullptr);
  buf[input_count] = 0;
  std::string str = converter.to_bytes(buf);
  size_t length = str.length() > len ? len : str.length();
  const char* arr = str.c_str();
  for(size_t idx = 0; idx < length; idx++) {
    bytes[idx] = arr[idx];
  }
  return length;
}

}
#endif