#include "../include/terminput.h"
#include "../include/bufnio.h"

namespace jterminal {

ESCBuffer::ESCBuffer(const size_t &capacity) : StringBuffer(capacity) {}

ESCBuffer::ESCBuffer(uint8_t *array, size_t len) : StringBuffer(array, len) {}

bool ESCBuffer::skipToNextSequence() {
  while(hasNext()) {
    if(isESCByte()) {
      return true;
    }
    skip();
  }
  return false;
}

void ESCBuffer::writeIntroducer(uint8_t type) {
  write(ESC);
  write(type);
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
  return read();
}

uint8_t ESCBuffer::peekIntroducer() {
  if(peek() != 0x1B) {
    return 0;
  }
  uint8_t type = peek(1);
  return type;
}

size_t ESCBuffer::scanESCLength() {
  if(!isESCByte() || available() < 3) {
    return 0;
  }
  if(!ESC_IS_CODE(peek(1))) {
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

bool ESCBuffer::peekSequence(CSISequenceString *sequence_string) {
  if(!isESCByte()) {
    return false;
  }
  uint8_t introducer = peekIntroducer();
  if(introducer != CSI_TYPE) {
    return false;
  }
  size_t count = 2;
  char private_symbol = 0;
  if(ESC_IS_PRIVATE_BYTE(peek(count))) {
    private_symbol = static_cast<char>(peek(count++));
  }
  std::vector<uint32_t> param_vector;
  size_t param_len = 0;
  while (isParamByte(count)) {
    int num = peekNumberFormat32(count, &param_len);
    if(param_len == 0) {
      break;
    }
    count += param_len;
    param_vector.push_back(num);
    if(peek(count) == 0x3B) {
      count++;
      continue;
    }
    break;
  }
  char end_symbol = static_cast<char>(peek(count));
  if(!ESC_IS_END_BYTE(end_symbol)) {
    return false;
  }
  char buf[count];
  size_t buf_len = peek(buf, count);
  std::string str(buf, buf_len);
  CSISequenceString csiss(private_symbol, param_vector, end_symbol, str);
  *sequence_string = csiss;
  return true;
}

bool ESCBuffer::readSequence(CSISequenceString *sequence_string) {
  if(peekSequence(sequence_string)) {
    skip(sequence_string->length());
    return true;
  }
  return false;
}

void ESCBuffer::writeSequence(CSISequence &sequence) {
  writeIntroducer(CSI_TYPE);
  if(sequence.privateChar()) {
    write(sequence.privateChar());
  }
  for(size_t idx = 0; idx < sequence.paramCount(); idx++) {
    if(idx >= 1) {
      write(0x3B);
    }
    writeString(std::to_string(sequence[idx]));
  }
  write(sequence.endSymbol());
}


}