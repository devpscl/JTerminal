#include "../include/bufnio.h"
#include <mutex>
#include <stdexcept>

namespace jterminal {

template<typename T>
QueuedBuffer<T>::QueuedBuffer(size_t capacity) {
  array_ = new T[capacity];
  len_ = capacity;
}

template<typename T>
QueuedBuffer<T>::~QueuedBuffer() {
  if(array_ != nullptr) {
    delete[] array_;
  }
}

template<typename T>
size_t QueuedBuffer<T>::writeNB(void *ptr, size_t len) {
  std::unique_lock lock(global_sync_mutex_);
  if(array_ == nullptr) {
    throw std::runtime_error("queued buffer is closed");
  }
  T* arr = reinterpret_cast<T*>(ptr);
  size_t free_ = free();
  len = free_ > len ? len : free_;
  size_t tmp_head_index = head_index_;
  for (int idx = 0; idx < len; idx++) {
    array_[tmp_head_index % (len_ + 1)] = arr[idx];
    tmp_head_index++;
  }
  head_index_ = tmp_head_index;
  notifyRead();
  return len;
}

template<typename T>
size_t QueuedBuffer<T>::readNB(void *ptr, size_t len) {
  if(ptr == nullptr && len == 0) {
    return 0;
  }
  std::unique_lock lock(global_sync_mutex_);
  if(array_ == nullptr) {
    throw std::runtime_error("queued buffer is closed");
  }
  T* arr = reinterpret_cast<T*>(ptr);
  size_t avail = available();
  len = avail > len ? len : avail;
  size_t tmp_tail_index = tail_index_;
  for (int idx = 0; idx < len; idx++) {
    arr[idx] = array_[tmp_tail_index % (len_ + 1)];
    tmp_tail_index++;
  }
  tail_index_ = tmp_tail_index;
  notifyWrite();
  return len;
}

template<typename T>
size_t QueuedBuffer<T>::peek(void *ptr, size_t len) {
  std::unique_lock lock(global_sync_mutex_);
  if(array_ == nullptr) {
    throw std::runtime_error("queued buffer is closed");
  }
  T* arr = reinterpret_cast<T*>(ptr);
  size_t avail = available();
  len = avail > len ? len : avail;
  size_t tmp_tail_index = tail_index_;
  for (int idx = 0; idx < len; idx++) {
    arr[idx] = array_[tmp_tail_index % (len_ + 1)];
    tmp_tail_index++;
  }
  return len;
}

template<typename T>
bool QueuedBuffer<T>::isEmpty() {
  return head_index_ == tail_index_;
}

template<typename T>
bool QueuedBuffer<T>::isFull() {
  return head_index_ + 1 == tail_index_;
}

template<typename T>
size_t QueuedBuffer<T>::free() {
  if(head_index_ >= tail_index_) {
    return len_ - (head_index_ - tail_index_);
  }
  return len_ - (head_index_ + len_ - tail_index_);
}

template<typename T>
size_t QueuedBuffer<T>::available() {
  if(head_index_ >= tail_index_) {
    return (head_index_ - tail_index_);
  }
  return (head_index_ + len_ - tail_index_);
}

template<typename T>
size_t QueuedBuffer<T>::size() {
  return len_;
}

template<typename T>
void QueuedBuffer<T>::clear() {
  head_index_ = 0;
  tail_index_ = 0;
}

template <typename T>
bool QueuedBuffer<T>::isClosed() {
  return array_ == nullptr;
}

template <typename T>
void QueuedBuffer<T>::close() {
  std::unique_lock lock(global_sync_mutex_);
  delete[] array_;
  array_ = nullptr;
  clear();
}

}
