#ifdef TERMINAL_WIN
#include "../../include/terminal.h"
#include <csignal>
#include <Windows.h>
#include <conio.h>
#include <iostream>
#include <codecvt>
#include <locale>

namespace jterminal {

void TermEngine::threadInputLoop() {
  std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
  HANDLE handle = STDIN_HANDLE;
  wchar_t buf[INPUT_BUFFER_SIZE + 1];
  unsigned long input_count;
  std::unique_lock lock(input_thread_mutex_);

  while(!closed_) {
    while(!(flags_memory_ & FLAG_EXTENDED_INPUT) || instance_ == nullptr) {

      input_thread_cv_.wait(lock);
    }
    ReadConsoleW(handle, buf, INPUT_BUFFER_SIZE,
                 &input_count, nullptr);
    buf[input_count] = 0;
    Terminal* instance = instance_;
    if(instance == nullptr) {
      continue;
    }
    std::string str = converter.to_bytes(buf);
    size_t len = str.length();
    const char* arr = str.c_str();
    uint8_t data[len];
    for(size_t idx = 0; idx < len; idx++) {
      data[idx] = arr[idx];
    }
    instance->writeInput(data, len);
  }
}

void TermEngine::threadWindowInput() {
  std::unique_lock lock(window_thread_mutex_);
  uint16_t delay_millis = settings_.mode == TERMINAL_MODE_EFFICIENCY ? 1000 : 200;
  dim_t old_dim;
  dim_t current_dim;
  readConsoleDim(&old_dim);
  ESCBuffer esc_buffer(24);
  while(!closed_) {
    while(!(flags_memory_ & FLAG_WINDOW_INPUT) || instance_ == nullptr) {
      window_thread_cv_.wait(lock);
    }
    std::this_thread::sleep_for(std::chrono::milliseconds(delay_millis));
    readConsoleDim(&current_dim);
    Terminal* instance = instance_;
    if(instance == nullptr) {
      continue;
    }
    if(current_dim != old_dim) {
      esc_buffer.reset();
      CSISequence sequence = CSI_SEQUENCE_PRIVATE('=','W',current_dim.width, current_dim.height,
                                                  old_dim.width, old_dim.height);
      esc_buffer.writeSequence(sequence);
      old_dim = current_dim;
      instance->writeInput(esc_buffer.ptr(), esc_buffer.cursor());
    }
  }
}

void TermEngine::signalSigInt(int signum) {
  shutdown();
  exit(signum);
}

void TermEngine::signalSigAbrt(int signum) {
  shutdown();
  abort();
}

void TermEngine::create(Settings settings) {
  settings_ = settings;
  enabled_ = true;
  input_thread_ = new std::thread(threadInputLoop);
  window_thread_ = new std::thread(threadWindowInput);
  signal(SIGINT, signalSigInt);
  signal(SIGABRT, signalSigAbrt);
}

void TermEngine::set(Terminal *instance) {
  instance_ = instance;
  if(instance == nullptr) {
    resetAll();
    return;
  }
  instance->update();
  instance->window_->setup();
}

void TermEngine::get(Terminal *instance) {
  *instance = *instance_;
}

void TermEngine::write(const char* cstr) {
  fwrite(cstr, strlen(cstr), 1, stdout);
  fflush(stdout);
}

void TermEngine::write(void* bytes, size_t len) {
  fwrite(bytes, len, 1, stdout);
  fflush(stdout);
}

void TermEngine::shutdown() {
  closed_ = true;
  resetAll();
  input_thread_->detach();
  window_thread_->detach();
}

void TermEngine::resetAll() {
  input_thread_cv_.notify_all();
  write("\033[0m");
  writeFlags(FLAG_DEFAULT, CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING);
  setBufferChannel(BUFFER_MAIN);
}

void TermEngine::writeFlags(uint8_t flags, uint8_t cursor_flags) {
  flags_memory_ = flags;
  DWORD mode = 0;
  mode |= ENABLE_EXTENDED_FLAGS;
  if(flags & FLAG_LINE_INPUT) {
    mode |= ENABLE_LINE_INPUT;
  }
  if(flags & FLAG_ECHO) {
    mode |= ENABLE_ECHO_INPUT;
  }
  if(flags & FLAG_SIGNAL_INPUT) {
    mode |= ENABLE_WINDOW_INPUT;
  }
  if(flags & FLAG_EXTENDED_INPUT) {
    mode |= ENABLE_PROCESSED_INPUT;
    mode |= ENABLE_VIRTUAL_TERMINAL_INPUT;
  }
  SetConsoleMode(STDIN_HANDLE, mode);

  mode = ENABLE_PROCESSED_OUTPUT | ENABLE_VIRTUAL_TERMINAL_PROCESSING;
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

void TermEngine::setBufferChannel(uint8_t buf_ch) {
  if(buffer_channel_ != buf_ch) {
    buffer_channel_ = buf_ch;
    if(buf_ch) {
      write(ESC_ENABLE_ALT_BUFFER);
    } else {
      write(ESC_DISABLE_ALT_BUFFER);
    }
  }
}

bool TermEngine::isEnabled() {
  return enabled_;
}

bool TermEngine::isClosed() {
  return closed_;
}

bool TermEngine::isActive(Terminal* instance) {
  return instance_ == instance;
}

void TermEngine::readConsoleDim(dim_t* dim) {
  CONSOLE_SCREEN_BUFFER_INFO buffer_info;
  GetConsoleScreenBufferInfo(STDOUT_HANDLE, &buffer_info);
  uint16_t width = buffer_info.dwSize.X;
  uint16_t height = buffer_info.dwSize.Y;
  dim->width = width;
  dim->height = height;
}

Settings TermEngine::getSettings() {
  return settings_;
}

void TermEngine::waitForClose() {
  input_thread_->join();
}



}
#endif