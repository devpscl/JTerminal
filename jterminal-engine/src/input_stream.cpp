#include "../include/terminput.h"
#include <chrono>
#include <mutex>

namespace jterminal {
InputStream::InputStream(Terminal* instance, uint8_t priority, size_t capacity) {
  buf_ = new QueuedBuffer<uint8_t>(capacity);
  priority_ = priority;
  instance_ = instance;
}

InputStream::~InputStream() {
  delete buf_;
}

void InputStream::write(void *bytes, size_t len) {
  if(buf_->isClosed()) {
    return;
  }
  buf_->writeNB(bytes, len);
}

size_t InputStream::available() const {
  return buf_->available();
}

size_t InputStream::peek(void *bytes, size_t len) const {
  return buf_->peek(bytes, len);
}

size_t InputStream::read(void *bytes, size_t len) {
  std::unique_lock lock(read_sync_mutex_M);
  return buf_->read(bytes, len);
}

void InputStream::readInput(InputEvent *input_event) {
  std::unique_lock lock(read_sync_mutex_M);
  buf_->read(nullptr, 0);

  uint8_t bytes[32];
  size_t peek_len = peek(bytes, 32);
  size_t input_length = scanInputLength(bytes, peek_len);
  input_length = buf_->read(bytes, input_length);

  translateInput(input_event, bytes, input_length);
}

size_t InputStream::peekInput(InputEvent *input_event) const {
  uint8_t bytes[32];
  size_t peek_len = peek(bytes, 32);
  size_t input_length = scanInputLength(bytes, peek_len);
  translateInput(input_event, bytes, input_length);
  return input_length;
}

int InputStream::read() {
  std::unique_lock lock(read_sync_mutex_M);
  uint8_t ret;
  if(buf_->readNB(&ret, 1)) {
    return ret;
  }
  return -1;
}

void InputStream::close() const {
  buf_->close();
}

void InputStream::reset() const {
  buf_->clear();
}

Terminal* InputStream::handle() {
  return instance_;
}

}