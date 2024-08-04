#include "../include/terminput.h"

namespace jterminal {

size_t scanInputLength(uint8_t* buf, size_t len) {
  ESCBuffer buffer(buf, len);
  if(buffer.isESCByte()) {
    size_t esc_len = buffer.scanESCLength();
    if(esc_len != 0) {
      return esc_len;
    }
  }
  return buffer.scanWideCharLength();
}

}