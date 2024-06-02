#include <codecvt>
#include <cstring>
#include <locale>
#include "../include/bufnio.h"

namespace jterminal {

StringBuffer::StringBuffer(const size_t &capacity) : MemoryBuffer(capacity) {}

StringBuffer::StringBuffer(uint8_t *array, size_t len) : MemoryBuffer(array, len) {}

StringBuffer::StringBuffer(const std::string& str) : MemoryBuffer(str.c_str(), str.length()) {}

bool StringBuffer::equal(std::string str, uint8_t flags) {
  if(flags & EN_FLAG_TO_BUF_END) {
    if(available() != str.length()) {
      return false;
    }
  }
  size_t count = 0;
  if(flags & EN_FLAG_IGNORE_CASE) {
    for(const char& ch : str) {
      if(cursor_ + count >= len_) {
        return false;
      }
      if(std::tolower(ch) != std::tolower(peek(count++))) {
        return false;
      }
    }
  } else {
    for(const char& ch : str) {
      if(cursor_ + count >= len_) {
        return false;
      }
      if(ch != peek(count++)) {
        return false;
      }
    }
  }
  if(flags & EN_FLAG_SKIP_IF_TRUE) {
    skip(count);
  }
  return true;
}

bool StringBuffer::equal(uint8_t ch, uint8_t flags) {
  uint8_t peek_val = peek();
  if(flags & EN_FLAG_IGNORE_CASE) {
    if(std::tolower(ch) != std::tolower(peek_val)) {
      return false;
    }
  } else {
    if(ch != peek_val) {
      return false;
    }
  }
  if(flags & EN_FLAG_TO_BUF_END) {
    if(available() != 1) {
      return false;
    }
  }
  if(flags & EN_FLAG_SKIP_IF_TRUE) {
    skip();
  }
  return true;
}

bool StringBuffer::writeString(const char *str) {
  return write(str, strlen(str));
}

bool StringBuffer::writeString(const std::string& str) {
  return write(str.c_str(), str.length());
}

int StringBuffer::readNumberFormat32() {
  int value = 0;
  while(std::isdigit(peek())) {
    uint8_t digit_byte = read() - 0x30;
    value = value * 10 + digit_byte;
  }
  return value;
}

short StringBuffer::readNumberFormat16() {
  short value = 0;
  while(std::isdigit(peek())) {
    uint8_t digit_byte = read() - 0x30;
    value = value * 10 + digit_byte;
  }
  return value;
}

size_t StringBuffer::peekWideCharLength() {
  size_t avail = available();
  if(avail == 0) {
    return 0;
  }
  uint8_t first = peek();
  if(first & 0x80) {
    if(((first & 0xE0) == 0xC0) && avail >= 2) {
      return 2;
    }
    if (((first & 0xF0) == 0xE0) && avail >= 3) {
      return 3;
    }
    if (((first & 0xF8) == 0xF0) && avail >= 4) {
      return 4;
    }
  }
  return 1;
}

wchar_t StringBuffer::readWideChar() {
  size_t len = peekWideCharLength();
  if(len <= 1) {
    return read();
  }
  char* bytes;
  bytes = reinterpret_cast<char*>(malloc(len));
  read(bytes, len);
  std::string utf_bytes(bytes, len);
  std::wstring_convert<std::codecvt_utf8<wchar_t>, wchar_t> converter;
  std::wstring wstr = converter.from_bytes(utf_bytes);
  free(bytes);
  return wstr[0];
}

wchar_t StringBuffer::peekWideChar() {
  size_t len = peekWideCharLength();
  if(len <= 1) {
    return peek();
  }
  char* bytes;
  bytes = reinterpret_cast<char*>(malloc(len));
  peek(bytes, len);
  std::string utf_bytes(bytes, len);
  std::wstring_convert<std::codecvt_utf8<wchar_t>, wchar_t> converter;
  std::wstring wstr = converter.from_bytes(utf_bytes);
  free(bytes);
  return wstr[0];
}

std::string StringBuffer::str() const {
  char copied_arr[len_];
  copyTo(copied_arr, len_);
  return {copied_arr, len_};
}

}