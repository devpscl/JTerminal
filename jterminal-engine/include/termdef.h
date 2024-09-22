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

#define MATH_MAX(a,b) (((a) > (b)) ? (a) : (b))
#define MATH_MIN(a,b) (((a) < (b)) ? (a) : (b))

#define FLAG_DEFAULT (FLAG_ECHO | FLAG_LINE_INPUT | FLAG_SIGNAL_INPUT)

#define INPUT_BUFFER_SIZE 128

#define TERMINAL_MODE_PERFORMANCE 0
#define TERMINAL_MODE_EFFICIENCY  1

#define TERMINAL_DEFAULT_TITLE "Terminal"

#define BUFFER_MAIN         0
#define BUFFER_SECONDARY    1

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

  template <typename T>
  Pos(T x, T y) : x(x), y(y) {}

  Pos() : x(1), y(1) {}
};

struct Dim {
  uint16_t width;
  uint16_t height;

  template <typename T>
  Dim(T width, T height) : width(width), height(height) {}

  Dim() : width(0), height(0) {}
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
