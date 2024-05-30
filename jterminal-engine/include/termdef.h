#ifndef JTERMINAL_ENGINE_INCLUDE_TERMTYPES_H_
#define JTERMINAL_ENGINE_INCLUDE_TERMTYPES_H_
#include <cinttypes>

namespace jterminal {

class Terminal;

#if defined(WIN32) || defined(WIN64)
#define TERMINAL_WIN
#elif defined(__linux__) || defined(__APPLE__) || defined(__unix__)
#define TERMINAL_UNIX
#endif

#define FLAG_LINE_INPUT         0x01
#define FLAG_ECHO               0x02
#define FLAG_MOUSE_INPUT        0x04
#define FLAG_ENHANCED_INPUT     0x08
#define FLAG_SIGNAL_INPUT       0x10
#ifdef TERMINAL_UNIX
#define FLAG_ALTERNATIVE_BUFFER 0x80
#endif

#define FLAG_DEFAULT (FLAG_ECHO | FLAG_LINE_INPUT | FLAG_SIGNAL_INPUT)

struct Pos;
struct Dim;

typedef Pos pos_t;
typedef Dim dim_t;

struct Pos {
  uint16_t x;
  uint16_t y;

  Pos(uint16_t x, uint16_t y) : x(x), y(y) {}

  Pos() : Pos(0, 0) {}
};

struct Dim {
  uint16_t width;
  uint16_t height;

  Dim(uint16_t width, uint16_t height) : width(width), height(height) {}

  Dim() : Dim(0, 0) {}
};

bool operator==(const Pos& a, const Pos& b) {
  return a.x == b.x && a.y == b.y;
}

bool operator!=(const Pos& a, const Pos& b) {
  return a.x != b.x || a.y != b.y;
}

bool operator==(const Dim& a, const Dim& b) {
  return a.width == b.width && a.height == b.height;
}

bool operator!=(const Dim& a, const Dim& b) {
  return a.width != b.width || a.height != b.height;
}

}

#endif //JTERMINAL_ENGINE_INCLUDE_TERMTYPES_H_
