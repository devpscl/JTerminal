#include "../include/terminput.h"
#include "../include/bufnio.h"

namespace jterminal {

ESCBuffer::ESCBuffer(const size_t &capacity) : StringBuffer(capacity) {}

ESCBuffer::ESCBuffer(uint8_t *array, size_t len) : StringBuffer(array, len) {}

bool ESCBuffer::jumpNextESC() {
  while(!isESCByte() && hasNext()) {
    skip();
  }
  return isESCByte();
}

void ESCBuffer::writeIntroducer(uint8_t type) {
  write(0x1B);
  write(type);
}

void ESCBuffer::writeParamSequence(const int *arr, size_t len) {
  for(size_t idx = 0; idx < len; idx++) {
    if(idx == 1) {
      write(0x3B);
    }
    writeString(std::to_string(arr[idx]));
  }
}

bool ESCBuffer::isESCByte(uint8_t offset) {
  return peek(offset) == 0x1B;
}

bool ESCBuffer::isPrivateModifierByte(uint8_t offset) {
  uint8_t b = peek(offset);
  return ESC_IS_PRIVATE_BYTE(b);
}

bool ESCBuffer::isParamByte(uint8_t offset) {
  if(std::isdigit(peek(offset))) {
    return true;
  }
  return false;
}

bool ESCBuffer::isEndByte(uint8_t offset) {
  return ESC_IS_END_BYTE(peek(offset));
}

bool ESCBuffer::isValidByte(uint8_t offset) {
  return ESC_IS_VALID_BYTE(peek(offset));
}

uint8_t ESCBuffer::readIntroducer() {
  if(read() != 0x1B) {
    return 0;
  }
  uint8_t type = read();
  if(type != ESC_CSI && type != ESC_SS3) {
    return 0;
  }
  return type;
}

uint8_t ESCBuffer::peekIntroducer() {
  if(peek() != 0x1B) {
    return 0;
  }
  uint8_t type = peek(1);
  if(type != ESC_CSI && type != ESC_SS3) {
    return 0;
  }
  return type;
}

size_t ESCBuffer::readParamSequence(int *arr, size_t len) {
  size_t count = 0;
  while (count < len) {
    if(!isParamByte()) {
      break;
    }
    int num = readNumberFormat32();
    arr[count++] = num;
    if(peek() == 0x3B) {
      skip();
      continue;
    }
    break;
  }
  return count;
}

size_t ESCBuffer::scanESCLength() {
  if(!isESCByte()) {
    return 0;
  }
  size_t count = 2;
  while(hasNext()) {
    if(ESC_IS_END_BYTE(peek(count))) {
      count++;
      break;
    }
    count++;
  }
  return count;
}

size_t ESCBuffer::peekEscapeSequence(void *arr, size_t arr_len) {
  size_t len = scanESCLength();
  if(len > arr_len) {
    len = arr_len;
  }
  return peek(arr, len);;
}

size_t ESCBuffer::readEscapeSequence(void *arr, size_t arr_len) {
  size_t len = scanESCLength();
  if(len > arr_len) {
    len = arr_len;
  }
  return read(arr, len);;
}

size_t ESCBuffer::peekSequenceFormat(uint8_t type, const char *format, int *data_array, size_t len, uint8_t *status) {
  size_t old_cursor = cursor_;
  size_t esc_len = readSequenceFormat(type, format, data_array, len, status);
  cursor_ = old_cursor;
  return esc_len;
}

size_t ESCBuffer::readSequenceFormat(uint8_t type, const char *format, int *data_array, size_t len, uint8_t *status) {
  if(status != nullptr) {
    *status = ESC_FORMAT_COMPILE_ERROR;
  }
  size_t start = cursor_;
  EscapeSequenceFormat esf;
  if(!compileESCFormat(type, format, &esf)) {
    return 0;
  }
  if(status != nullptr) {
    *status = ESC_FORMAT_NOT_MATCH;
  }
  if(!isESCByte()) {
    return 0;
  }
  if(readIntroducer() != esf.type) {
    return 0;
  }
  if(esf.private_param_char != 0) {
    if(!isPrivateModifierByte() || esf.private_param_char != read()) {
      return 0;
    }
  }
  size_t count = readParamSequence(data_array, len);
  if(esf.param_count != count) {
    return 0;
  }
  if(read() == esf.end_char) {
    if(status != nullptr) {
      *status = ESC_FORMAT_OK;
    }
    return cursor_ - start;
  }
  return 0;
}


}