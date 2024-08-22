#include "../include/terminput.h"
#include <chrono>
#include <mutex>
#include "../include/terminal.h"

namespace jterminal {

InputPipeline::InputPipeline(uint8_t priority, size_t capacity) {
  priority_ = priority;
  buf_ = new QueuedBuffer<uint8_t>(capacity);
}

InputPipeline::~InputPipeline() {
  detach();
  delete buf_;
}

void InputPipeline::write(void *bytes, size_t len) {
  buf_->writeNB(bytes, len);
}

size_t InputPipeline::available() {
  return buf_->available();
}

uint8_t InputPipeline::priority() const {
  return priority_;
}

size_t InputPipeline::peek(void *bytes, size_t len) {
  return buf_->peek(bytes, len);
}

size_t InputPipeline::read(void *bytes, size_t len) {
  std::unique_lock<std::mutex> lock(read_sync_mutex_M);
  return buf_->read(bytes, len);
}

void InputPipeline::readInput(InputEvent *input_event) {
  std::unique_lock<std::mutex> lock(read_sync_mutex_M);
  buf_->read(nullptr, 0);

  uint8_t bytes[32];
  size_t peek_len = peek(bytes, 32);
  size_t input_length = scanInputLength(bytes, peek_len);
  input_length = buf_->read(bytes, input_length);

  translateInput(input_event, bytes, input_length);
}

size_t InputPipeline::peekInput(InputEvent *input_event) {
  uint8_t bytes[32];
  size_t peek_len = peek(bytes, 32);
  size_t input_length = scanInputLength(bytes, peek_len);
  translateInput(input_event, bytes, input_length);
  return input_length;
}

void InputPipeline::detach() {
  Terminal::detachInputPipeline(this);
}

}