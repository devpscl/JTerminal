#include "../include/terminput.h"

uint64_t jterminal::hashInput(const uint8_t *bytes, size_t len) {
  if(len == 0) {
    return 0;
  }
  uint64_t hash = 0;
  for(int idx = 0; idx < len; idx++) {
    uint8_t b = bytes[idx];
    hash = 31 * hash + b;
  }
  return hash;
}