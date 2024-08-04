#ifndef JTERMINAL_ENGINE_INCLUDE_ESCSEQ_H_
#define JTERMINAL_ENGINE_INCLUDE_ESCSEQ_H_
#include "termdef.h"
#include <utility>
#include <vector>
#include <string>

#define ESC_RESET                   "\u001B[0m"
#define ESC_ENABLE_CURSOR           "\u001B[?25h"
#define ESC_DISABLE_CURSOR          "\u001B[?25l"
#define ESC_ENABLE_MOUSE            "\u001B[?1015h\x1B[?1006h\x1B[?1000h"
#define ESC_ENABLE_MOUSE_EXTENDED   "\u001B[?1015h\x1B[?1006h\x1B[?1003h"
#define ESC_DISABLE_MOUSE           "\u001B[?1000l\x1B[?1003l"
#define ESC_ENABLE_CURSOR_BLINKING  "\u001B[?12h"
#define ESC_DISABLE_CURSOR_BLINKING "\u001B[?12l"
#define ESC_TITLE_START             "\u001B]0;"
#define ESC_TITLE_END               "\007"
#define ESC_ENABLE_ALT_BUFFER       "\u001B[?1049h"
#define ESC_DISABLE_ALT_BUFFER      "\u001B[?1049l"
#define ESC_CURSOR_REQUEST          "\u001B[6n"
#define ESC_BCOLOR_BLACK            "\u001B[40m"
#define ESC_BCOLOR_RED              "\u001B[41m"
#define ESC_BCOLOR_GREEN            "\u001B[42m"
#define ESC_BCOLOR_YELLOW           "\u001B[43m"
#define ESC_BCOLOR_BLUE             "\u001B[44m"
#define ESC_BCOLOR_MAGENTA          "\u001B[45m"
#define ESC_BCOLOR_CYAN             "\u001B[46m"
#define ESC_BCOLOR_WHITE            "\u001B[47m"
#define ESC_BCOLOR_BLACK            "\u001B[40m"
#define ESC_BCOLOR_RED_B            "\u001B[41m"
#define ESC_BCOLOR_GREEN_B          "\u001B[42m"
#define ESC_BCOLOR_YELLOW_B         "\u001B[43m"
#define ESC_BCOLOR_BLUE_B           "\u001B[44m"
#define ESC_BCOLOR_MAGENTA_B        "\u001B[45m"
#define ESC_BCOLOR_CYAN_B           "\u001B[46m"
#define ESC_BCOLOR_WHITE_B          "\u001B[47m"
#define ESC_FCOLOR_BLACK            "\u001B[30m"
#define ESC_FCOLOR_RED              "\u001B[31m"
#define ESC_FCOLOR_GREEN            "\u001B[32m"
#define ESC_FCOLOR_YELLOW           "\u001B[33m"
#define ESC_FCOLOR_BLUE             "\u001B[34m"
#define ESC_FCOLOR_MAGENTA          "\u001B[35m"
#define ESC_FCOLOR_CYAN             "\u001B[36m"
#define ESC_FCOLOR_WHITE            "\u001B[37m"
#define ESC_FCOLOR_BLACK            "\u001B[30m"
#define ESC_FCOLOR_RED_B            "\u001B[31m"
#define ESC_FCOLOR_GREEN_B          "\u001B[32m"
#define ESC_FCOLOR_YELLOW_B         "\u001B[33m"
#define ESC_FCOLOR_BLUE_B           "\u001B[34m"
#define ESC_FCOLOR_MAGENTA_B        "\u001B[35m"
#define ESC_FCOLOR_CYAN_B           "\u001B[36m"
#define ESC_FCOLOR_WHITE_B          "\u001B[37m"

#define ESC                   0x1B
#define CSI_TYPE              0x5B

#define ESC_IS_VALID_BYTE(b) (b >= 0x20 && b <= 0x7E)
#define ESC_IS_PRIVATE_BYTE(b) ((b >= 0x70 && b <= 0x7E) || (b >= 0x3C && b <= 0x3F))
#define ESC_IS_END_BYTE(b) (b >= 0x40 && b <= 0x7E)
#define ESC_IS_PARAM_BYTE(b) (b >= 0x30 && b <= 0x3F)
#define ESC_IS_INTERMEDIATE_BYTE(b) (b >= 0x20 && b <= 0x2F)
#define ESC_IS_CODE(b) (b >= 0x41 && b <= 0x60)

#define CSI_SEQUENCE(end,...) CSISequence(0,{__VA_ARGS__},end)
#define CSI_SEQUENCE_PRIVATE(prv_char,end,...) CSISequence(prv_char,{__VA_ARGS__},end)

class CSISequence {
  char private_char_;
  std::vector<uint32_t> value_vector_;
  char end_symbol_;

 public:

  CSISequence(char private_char, const std::vector<uint32_t> &value_vector, char end_symbol) : private_char_(
      private_char), value_vector_(value_vector), end_symbol_(end_symbol) {}

  CSISequence() : CSISequence(false, {}, 0) {}

  [[nodiscard]] char privateChar() const {
    return private_char_;
  }

  [[nodiscard]] char endSymbol() const {
    return end_symbol_;
  }

  uint32_t operator [](size_t index) {
    return value_vector_[index];
  };

  uint32_t param(size_t index) {
    return value_vector_[index];
  }

  size_t paramCount() {
    return value_vector_.size();
  }

  operator bool() {
    return end_symbol_ != 0;
  }

};

class CSISequenceString : public CSISequence {
  std::string str_;

 public:

  CSISequenceString() : CSISequenceString(0, {}, 0, "") {};

  CSISequenceString(char private_char,
                    const std::vector<uint32_t> &value_vector,
                    char end_symbol,
                    std::string str) : CSISequence(private_char, value_vector, end_symbol), str_(std::move(str)) {}

  std::string str() {
    return str_;
  }

  size_t length() {
    return str_.length();
  }

};

#endif //JTERMINAL_ENGINE_INCLUDE_ESCSEQ_H_
