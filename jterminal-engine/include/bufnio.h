#ifndef JTERMINAL_ENGINE_INCLUDE_BUFIO_H_
#define JTERMINAL_ENGINE_INCLUDE_BUFIO_H_
#include "termdef.h"
#include <string>

#define EN_FLAG_IGNORE_CASE   0x1
#define EN_FLAG_SKIP_IF_TRUE  0x2
#define EN_FLAG_TO_BUF_END    0x4

namespace jterminal {


class MemoryBuffer {
 protected:
  uint8_t* array_;
  size_t len_;
  size_t cursor_ = 0;
  bool local_arr_ = false;
 public:

  MemoryBuffer(const size_t& capacity);

  MemoryBuffer(uint8_t* array, size_t len, bool copy = false);

  MemoryBuffer(const char* array, size_t len);

  ~MemoryBuffer();

  uint8_t* ptr();

  [[nodiscard]] size_t capacity() const;

  [[nodiscard]] size_t available() const;

  [[nodiscard]] size_t cursor() const;

  void cursor(const size_t& index);

  bool write(uint8_t element);

  bool write(const void* ptr, size_t len);

  bool writeUInt8(uint8_t value);

  bool writeUInt16(uint16_t value);

  bool writeUInt32(uint32_t value);

  uint8_t read();

  uint8_t readUInt8();

  int8_t readInt8();

  uint16_t readUInt16();

  int16_t readInt16();

  uint32_t readUInt32();

  int32_t readInt32();

  uint8_t peek(size_t offset = 0);

  size_t read(uint8_t * ptr, size_t len);

  bool hasNext() const;

  void skip(size_t count = 1);

  size_t copyTo(void* array, size_t len) const;

  void erase();

  operator bool() {
    return available();
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
