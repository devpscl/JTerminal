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

#define FLAG_LINE_INPUT           0x01
#define FLAG_ECHO                 0x02
#define FLAG_MOUSE_INPUT          0x04
#define FLAG_MOUSE_EXTENDED_INPUT 0x08
#define FLAG_EXTENDED_INPUT       0x10
#define FLAG_SIGNAL_INPUT         0x20
#define FLAG_WINDOW_INPUT         0x40

#define CURSOR_FLAG_VISIBLE       0x01
#define CURSOR_FLAG_BLINKING      0x02

#ifdef TERMINAL_UNIX
#define FLAG_ALTERNATIVE_BUFFER 0x80
#endif

#define FLAG_DEFAULT (FLAG_ECHO | FLAG_LINE_INPUT | FLAG_SIGNAL_INPUT)

#define INPUT_BUFFER_SIZE 128

#define TERMINAL_MODE_PERFORMANCE 0
#define TERMINAL_MODE_EFFICIENCY  1

#define TERMINAL_DEFAULT_TITLE "Terminal"


struct Pos;
struct Dim;

typedef Pos pos_t;
typedef Dim dim_t;

struct Settings {
  uint8_t mode = TERMINAL_MODE_PERFORMANCE;
  uint32_t input_buffer_size = 4096;
};

struct Pos {
  uint16_t x;
  uint16_t y;

  Pos(uint16_t x, uint16_t y) : x(x), y(y) {}

  Pos(int16_t x, int16_t y) : x(x), y(y) {}

  Pos(uint32_t x, uint32_t y) : x(x), y(y) {}

  Pos() : x(1), y(1) {}
};

struct Dim {
  uint16_t width;
  uint16_t height;

  Dim(uint16_t width, uint16_t height) : width(width), height(height) {}

  Dim() : Dim(1, 1) {}
};

inline bool operator==(const Pos& a, const Pos& b) {
  return a.x == b.x && a.y == b.y;
}

inline bool operator!=(const Pos& a, const Pos& b) {
  return a.x != b.x || a.y != b.y;
}

inline bool operator==(const Dim& a, const Dim& b) {
  return a.width == b.width && a.height == b.height;
}

inline bool operator!=(const Dim& a, const Dim& b) {
  return a.width != b.width || a.height != b.height;
}

}

#endif //JTERMINAL_ENGINE_INCLUDE_TERMTYPES_H_
