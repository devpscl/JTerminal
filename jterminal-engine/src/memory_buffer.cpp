#include "../include/bufnio.h"

namespace jterminal {


MemoryBuffer::MemoryBuffer(const size_t& capacity) {
  len_ = capacity;
  array_ = new uint8_t[capacity];
  local_arr_ = true;
}

MemoryBuffer::MemoryBuffer(uint8_t *array, size_t len, bool copy) {
  len_ = len;
  if(copy) {
    array_ = new uint8_t[len];
    for(size_t idx = 0; idx < len; idx++) {
      array_[idx] = array[idx];
    }
    local_arr_ = true;
    return;
  }
  array_ = array;
}

MemoryBuffer::MemoryBuffer(const char* array, size_t len) {
  len_ = len;
  array_ = new uint8_t[len];
  for(size_t idx = 0; idx < len; idx++) {
    array_[idx] = array[idx];
  }
  local_arr_ = true;
}

MemoryBuffer::~MemoryBuffer() {
  if(!local_arr_) {
    return;
  }
  delete[] array_;
}

uint8_t *MemoryBuffer::ptr() {
  return array_;
}

size_t MemoryBuffer::capacity() const {
  return len_;
}

size_t MemoryBuffer::available() const {
  return len_ - cursor_;
}

size_t MemoryBuffer::cursor() const {
  return cursor_;
}

void MemoryBuffer::cursor(const size_t &index) {
  cursor_ = index < len_ ? index : len_;
}

bool MemoryBuffer::write(uint8_t element) {
  if(available() == 0) {
    return false;
  }
  array_[cursor_++] = element;
  return true;
}

bool MemoryBuffer::write(const void* ptr, size_t len) {
  const auto* elements = reinterpret_cast<const uint8_t*>(ptr);
  for(size_t idx = 0; idx < len; idx++) {
    if(!write(elements[idx])) {
      return false;
    }
  }
  return true;
}

bool MemoryBuffer::writeUInt8(uint8_t value) {
  return write(value);
}

bool MemoryBuffer::writeUInt16(uint16_t value) {
  if(available() < 2) {
    return false;
  }
  write(static_cast<uint8_t>(value >> 8));
  write(static_cast<uint8_t>(value));
  return true;
}

bool MemoryBuffer::writeUInt32(uint32_t value) {
  if(available() < 4) {
    return false;
  }
  write(static_cast<uint8_t>(value >> 24));
  write(static_cast<uint8_t>(value >> 16));
  write(static_cast<uint8_t>(value >> 8));
  write(static_cast<uint8_t>(value));
  return true;
}

uint8_t MemoryBuffer::read() {
  if(available() == 0) {
    return -1;
  }
  return array_[cursor_++];
}

uint8_t MemoryBuffer::readUInt8() {
  return read();
}

int8_t MemoryBuffer::readInt8() {
  return static_cast<int8_t>(read());
}

uint16_t MemoryBuffer::readUInt16() {
  uint16_t value = 0;
  value |= readUInt8();
  value <<= 8;
  value |= readUInt8();
  return value;
}

int16_t MemoryBuffer::readInt16() {
  int16_t value = 0;
  value |= readInt8();
  value <<= 8;
  value |= readInt8();
  return value;
}

uint32_t MemoryBuffer::readUInt32() {
  uint32_t value = 0;
  value |= readUInt8();
  value <<= 8;
  value |= readUInt8();
  value <<= 8;
  value |= readUInt8();
  value <<= 8;
  value |= readUInt8();
  return value;
}

int32_t MemoryBuffer::readInt32() {
  int32_t value = 0;
  value |= readInt8();
  value <<= 8;
  value |= readInt8();
  value <<= 8;
  value |= readInt8();
  value <<= 8;
  value |= readInt8();
  return value;
}

uint8_t MemoryBuffer::peek(size_t offset) {
  size_t index = cursor_ + offset;
  if(index >= len_) {
    return -1;
  }
  return array_[index];
}

size_t MemoryBuffer::read(uint8_t *ptr, size_t len) {
  size_t count = 0;
  while(hasNext() && count < len) {
    ptr[cursor_ +  count] = read();
  }
  return count;
}

bool MemoryBuffer::hasNext() const {
  return cursor_ < len_;
}

void MemoryBuffer::skip(size_t count) {
  cursor(cursor_ + count);
}

size_t MemoryBuffer::copyTo(void *array, size_t len) const {
  auto* byte_arr = reinterpret_cast<uint8_t*>(array);
  size_t out_len = len_ > len ? len : len_;
  for(size_t idx = 0; idx < out_len; idx++) {
    byte_arr[idx] = array_[idx];
  }
  return out_len;
}

void MemoryBuffer::erase() {
  len_ = cursor_;
}

}