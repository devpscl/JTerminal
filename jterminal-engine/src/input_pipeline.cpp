#include "../include/terminput.h"
#include <chrono>
#include <mutex>
#include "../include/terminal.h"

namespace jterminal {

InputPipeline::InputPipeline(uint8_t priority, uint32_t timeout_millis) {
  priority_ = priority;
  timeout_millis_ = timeout_millis;
}

InputPipeline::~InputPipeline() {
  detach();
}

void InputPipeline::setBufferSize(size_t size) {
  buf_size_ = size;
}

void InputPipeline::write(const uint8_t *bytes, size_t len) {
  size_t current_size = buf_.in_avail();
  size_t remaining_size = current_size >= buf_size_ ? 0 : buf_size_ - current_size;

  size_t real_len = len >= remaining_size ? remaining_size : len;
  if(real_len == 0) {
    return;
  }
  std::unique_lock<std::mutex> lock(mutex_);
  buf_.sputn(reinterpret_cast<const char*>(bytes), real_len);
  condition_variable_.notify_one();
}

size_t InputPipeline::available() {
  return buf_.in_avail();
}

uint8_t InputPipeline::priority() const {
  return priority_;
}

size_t InputPipeline::read(uint8_t *bytes, size_t len) {
  std::unique_lock<std::mutex> lock(mutex_);
  if(timeout_millis_ == 0xFFFF) {
    condition_variable_.wait(lock, [this](){
      return available();
    });
    size_t out_len = buf_.sgetn(reinterpret_cast<char*>(bytes), len);
    return out_len;
  }
  condition_variable_.wait_for(lock,std::chrono::milliseconds(timeout_millis_),
                               [this](){
    return available();
  });
  if(!buf_.in_avail()) {
    return -1;
  }
  size_t out_len = buf_.sgetn(reinterpret_cast<char*>(bytes), len);
  return out_len;
}

void InputPipeline::detach() {
  Terminal::detachInputPipeline(this);
}

}