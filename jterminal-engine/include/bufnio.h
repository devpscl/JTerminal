#ifndef JTERMINAL_ENGINE_INCLUDE_BUFIO_H_
#define JTERMINAL_ENGINE_INCLUDE_BUFIO_H_
#include "termdef.h"
#include "escseq.h"
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

  [[nodiscard]] size_t size() const;

  void reset();

  explicit operator bool() {
    return available();
  }

  template<class C>
  C& downcast() {
    return static_cast<C&>(*this);
  }

};

class StringBuffer : public MemoryBuffer<uint8_t> {
 public:
  explicit StringBuffer(const size_t& capacity);

  StringBuffer(uint8_t* array, size_t len);

  explicit StringBuffer(const std::string& str);

  bool equal(std::string str, uint8_t flags = 0);

  bool equal(uint8_t ch, uint8_t flags = 0);

  bool writeString(const char* str);

  bool writeString(const std::string& str);

  int readNumberFormat32();

  short readNumberFormat16();

  size_t peekWideCharLength();

  wchar_t readWideChar();

  wchar_t peekWideChar();

  [[nodiscard]] std::string str() const;
};

class ESCBuffer : public StringBuffer {
 public:
  explicit ESCBuffer(const size_t& capacity);

  ESCBuffer(uint8_t* array, size_t len);

  bool jumpNextESC();

  void writeIntroducer(uint8_t type);

  void writeParamSequence(std::initializer_list<int> list);

  void writeParamSequence(const int* arr, size_t len);

  bool isESCByte(uint8_t offset = 0);

  bool isPrivateModifierByte(uint8_t offset = 0);

  bool isParamByte(uint8_t offset = 0);

  bool isEndByte(uint8_t offset = 0);

  bool isValidByte(uint8_t offset = 0);

  uint8_t readIntroducer();

  uint8_t peekIntroducer();

  size_t readParamSequence(int* arr, size_t len);

  size_t scanESCLength();

  size_t peekEscapeSequence(void* arr, size_t arr_len);

  size_t readEscapeSequence(void* arr, size_t arr_len);

  size_t peekSequenceFormat(uint8_t type, const char* format, int* data_array,
                            size_t len, uint8_t* status = nullptr);

  size_t readSequenceFormat(uint8_t type, const char* format, int* data_array,
                            size_t len, uint8_t* status = nullptr);

};

template class MemoryBuffer<wchar_t>;
template class MemoryBuffer<uint8_t>;
template class MemoryBuffer<int8_t>;
template class MemoryBuffer<uint16_t>;
template class MemoryBuffer<int16_t>;
template class MemoryBuffer<uint32_t>;
template class MemoryBuffer<int32_t>;

}

#endif //JTERMINAL_ENGINE_INCLUDE_BUFIO_H_
