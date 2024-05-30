#include "../include/terminput.h"
#include <chrono>
#include <mutex>

namespace jterminal {

InputPipeline::InputPipeline(uint8_t priority, uint32_t timeout_millis) {
  priority_ = priority;
  timeout_millis_ = timeout_millis;
}

void InputPipeline::write(const uint8_t *bytes, size_t len) {
  std::unique_lock<std::mutex> lock(mutex_);
  buf.sputn(reinterpret_cast<const char*>(bytes), len);
  condition_variable_.notify_one();
}

size_t InputPipeline::available() {
  return buf.in_avail();
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
    size_t out_len = buf.sgetn(reinterpret_cast<char*>(bytes), len);
    return out_len;
  }
  condition_variable_.wait_for(lock,std::chrono::milliseconds(timeout_millis_),
                               [this](){
    return available();
  });
  size_t out_len = buf.sgetn(reinterpret_cast<char*>(bytes), len);
  return out_len;
}

}