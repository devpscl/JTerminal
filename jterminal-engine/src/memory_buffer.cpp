#include "../include/bufnio.h"

namespace jterminal {

template<typename T>
MemoryBuffer<T>::MemoryBuffer(const size_t& capacity) {
  len_ = capacity;
  array_ = new T[capacity];
  local_arr_ = true;
}

template<typename T>
MemoryBuffer<T>::MemoryBuffer(void *array, size_t len, bool copy) {
  auto* cast_array = reinterpret_cast<T*>(array);
  len_ = len;
  if(copy) {
    array_ = new T[len];
    for(size_t idx = 0; idx < len; idx++) {
      array_[idx] = cast_array[idx];
    }
    local_arr_ = true;
    return;
  }
  array_ = cast_array;
}

template<typename T>
MemoryBuffer<T>::MemoryBuffer(const void *array, size_t len) {
  const auto* cast_array = reinterpret_cast<const T*>(array);
  len_ = len;
  array_ = new T[len];
  for(size_t idx = 0; idx < len; idx++) {
    array_[idx] = cast_array[idx];
  }
  local_arr_ = true;
}

template<typename T>
MemoryBuffer<T>::~MemoryBuffer() {
  if(!local_arr_) {
    return;
  }
  delete[] array_;
}

template<typename T>
T *MemoryBuffer<T>::ptr(bool offset_by_cursor) {
  if(offset_by_cursor) {
    return array_ + cursor_;
  }
  return array_;
}

template<typename T>
size_t MemoryBuffer<T>::capacity() const {
  return len_;
}

template<typename T>
size_t MemoryBuffer<T>::available() const {
  return len_ - cursor_;
}

template<typename T>
size_t MemoryBuffer<T>::cursor() const {
  return cursor_;
}

template<typename T>
void MemoryBuffer<T>::cursor(const size_t &index) {
  cursor_ = index < len_ ? index : len_;
}

template<typename T>
bool MemoryBuffer<T>::write(T element) {
  if(available() == 0) {
    return false;
  }
  array_[cursor_++] = element;
  return true;
}

template<typename T>
bool MemoryBuffer<T>::write(const void* ptr, size_t len) {
  const auto* elements = reinterpret_cast<const uint8_t*>(ptr);
  for(size_t idx = 0; idx < len; idx++) {
    if(!write(elements[idx])) {
      return false;
    }
  }
  return true;
}

template<typename T>
T MemoryBuffer<T>::read() {
  if(available() == 0) {
    return -1;
  }
  return array_[cursor_++];
}

template<typename T>
T MemoryBuffer<T>::peek(size_t offset) {
  size_t index = cursor_ + offset;
  if(index >= len_) {
    return -1;
  }
  return array_[index];
}

template<typename T>
size_t MemoryBuffer<T>::read(void *ptr, size_t len) {
  auto* arr = reinterpret_cast<T*>(ptr);
  size_t count = 0;
  while(available() && count < len) {
    arr[count] = read();
    count++;
  }
  return count;
}

template<typename T>
size_t MemoryBuffer<T>::peek(void *ptr, size_t len, size_t offset) {
  auto* arr = reinterpret_cast<T*>(ptr);
  size_t count = 0;
  while(((cursor_ + count) < len_) && count < len) {
    arr[count] = peek(count + offset);
    count++;
  }
  return count;
}

template<typename T>
bool MemoryBuffer<T>::hasNext() const {
  return cursor_ < len_;
}

template<typename T>
void MemoryBuffer<T>::skip(size_t count) {
  cursor(cursor_ + count);
}

template<typename T>
size_t MemoryBuffer<T>::copyTo(void *array, size_t len) const {
  auto* byte_arr = reinterpret_cast<uint8_t*>(array);
  size_t out_len = len_ > len ? len : len_;
  for(size_t idx = 0; idx < out_len; idx++) {
    byte_arr[idx] = array_[idx];
  }
  return out_len;
}

template<typename T>
void MemoryBuffer<T>::erase() {
  len_ = cursor_;
}

template<typename T>
size_t MemoryBuffer<T>::size() const {
  return len_;
}

template<typename T>
void MemoryBuffer<T>::reset() {
  cursor_ = 0;
}

}