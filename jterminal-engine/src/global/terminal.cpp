#include "../../include/terminal.h"
#include <algorithm>

namespace jterminal {

InputStreamPtr Terminal::newSingletonInputStream(size_t capacity) {
  const auto ptr = new InputStream(0xFF, capacity);
  input_stream_vector_.push_back(ptr);
  sortInputStreamVector();
  return ptr;
}

void Terminal::sortInputStreamVector() {
  std::ranges::sort(input_stream_vector_.begin(), input_stream_vector_.end(),
                    [](InputStream* a, InputStream* b) {
                      if(a == nullptr || b == nullptr) {
                        return false;
                      }
                      return a->priority_ > b->priority_;
                    });
}

void Terminal::writeInput(uint8_t* bytes, size_t len) {
  if(input_stream_vector_.empty()) {
    return;
  }
  for (auto &pl : input_stream_vector_) {
    if(pl == nullptr) {
      continue;
    }
    pl->write(bytes, len);
    if(pl->priority_ == 0xFF) {
      break;
    }
  }
}

void Terminal::setFlags(uint8_t flags) {
  flags_ = flags;
  update();
}

void Terminal::addFlags(uint8_t flags) {
  setFlags(flags_ | flags);
}

void Terminal::removeFlags(uint8_t flags) {
  setFlags(flags_ & ~flags);
}

void Terminal::getFlags(uint8_t* flags_ptr) {
  *flags_ptr = flags_;
}

void Terminal::update() {
  writeFlags(flags_, Window::cursor_flags_);
}

void Terminal::reset(bool clear_screen) {
  flags_ = FLAG_DEFAULT;
  Window::cursor_flags_ = CURSOR_FLAG_VISIBLE | CURSOR_FLAG_BLINKING;
  write("\033[0m");
  update();
  if(clear_screen) {
    clear();
  }
}

void Terminal::beep() {
  write("\a");
}

void Terminal::setBuffer(uint8_t buffer) {
  if(buffer_ != buffer) {
    buffer_ = buffer;
    if(buffer_) {
      write(ESC_ENABLE_ALT_BUFFER);
    } else {
      write(ESC_DISABLE_ALT_BUFFER);
    }
  }
}

uint8_t Terminal::getBuffer() {
  return buffer_;
}

InputStreamPtr Terminal::newInputStream(InputStreamPriority prio, size_t capacity) {
  const auto ptr = new InputStream(static_cast<uint8_t>(prio), capacity);
  input_stream_vector_.push_back(ptr);
  sortInputStreamVector();
  return ptr;
}

void Terminal::disposeInputStream(InputStreamPtr input_stream) {
  for(size_t idx = 0; idx < input_stream_vector_.size(); idx++) {
    if(input_stream_vector_[idx] == input_stream) {
      input_stream_vector_.erase(input_stream_vector_.begin() + idx);
      break;
    }
  }
  delete input_stream;
}

bool Terminal::isEnabled() {
  return enabled_ && !closed_;
}

bool Terminal::isClosed() {
  return closed_;
}

Settings Terminal::getSettings() {
  return settings_;
}

void Terminal::joinFutureClose() {
  if(closed_) {
    return;
  }
  input_thread_->join();
}

}