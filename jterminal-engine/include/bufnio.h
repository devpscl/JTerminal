#ifndef JTERMINAL_ENGINE_INCLUDE_BUFIO_H_
#define JTERMINAL_ENGINE_INCLUDE_BUFIO_H_
#include "termdef.h"
#include <string>

#define EN_FLAG_IGNORE_CASE   0x1
#define EN_FLAG_SKIP_IF_TRUE  0x2
#define EN_FLAG_TO_BUF_END    0x4

namespace jterminal {


template <typename T>
class MemoryBuffer {
 protected:
  T* array_;
  size_t len_;
  size_t cursor_ = 0;
  bool local_arr_ = false;
 public:

  explicit MemoryBuffer(const size_t& capacity);

  MemoryBuffer(void* array, size_t len, bool copy = false);

  MemoryBuffer(const void* array, size_t len);

  ~MemoryBuffer();

  T* ptr(bool offset_by_cursor = false);

  [[nodiscard]] size_t capacity() const;

  [[nodiscard]] size_t available() const;

  [[nodiscard]] size_t cursor() const;

  void cursor(const size_t& index);

  bool write(T element);

  bool write(const void* ptr, size_t len);

  T read();

  T peek(size_t offset = 0);

  size_t read(void* ptr, size_t len);

  size_t peek(void* ptr, size_t len, size_t offset = 0);

  [[nodiscard]] bool hasNext() const;

  void skip(size_t count = 1);

  size_t copyTo(void* array, size_t len) const;

  void erase();

  explicit operator bool() {
    return available();
  }

  template<class C>
  C& downcast() {
    return static_cast<C&>(*this);
  }

};

class StringBuffer : public MemoryBuffer {
 public:
  StringBuffer(const size_t& capacity);

  StringBuffer(uint8_t * array, size_t len);

  StringBuffer(const std::string& str);

  bool equal(std::string str, uint8_t flags = 0);

  bool equal(uint8_t ch, uint8_t flags = 0);

  bool writeString(const char* str);

  bool writeString(const std::string& str);

  int readNumberFormat32();

  short readNumberFormat16();

  [[nodiscard]] std::string str() const;

};

}

#endif //JTERMINAL_ENGINE_INCLUDE_BUFIO_H_
