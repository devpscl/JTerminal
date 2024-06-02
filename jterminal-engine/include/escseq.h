#ifndef JTERMINAL_ENGINE_INCLUDE_ESCSEQ_H_
#define JTERMINAL_ENGINE_INCLUDE_ESCSEQ_H_
#include "termdef.h"
#include <string_view>

#define ESC_RESET                   "\u001B[0m"
#define ESC_ENABLE_CURSOR           "\u001B[?25h"
#define ESC_DISABLE_CURSOR          "\u001B[?25l"
#define ESC_ENABLE_MOUSE            "\u001B[?1015h\x1B[?1006h\x1B[?1000h"
#define ESC_ENABLE_MOUSE_EXTENDED   "\u001B[?1015h\x1B[?1006h\x1B[?1003h"
#define ESC_DISABLE_MOUSE           "\u001B[?1000l\x1B[?1003l"
#define ESC_ENABLE_CURSOR_BLINKING  "\u001B[?12h"
#define ESC_DISABLE_CURSOR_BLINKING "\u001B[?12l"
#define ESC_TITLE_START             "\u001B[0;"
#define ESC_TITLE_END               "\x7"
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

#define ESC_SS3          0x4F
#define ESC_CSI          0x5B

#define ESC_BYTE_UNKNOWN      0
#define ESC_BYTE_PARAM        1
#define ESC_BYTE_END          2
#define ESC_BYTE_INTERMEDIATE 3

#define ESC_IS_VALID_BYTE(b) (b >= 0x20 && b <= 0x7E)
#define ESC_IS_PRIVATE_BYTE(b) ((b >= 0x70 && b <= 0x7E) || (b >= 0x3C && b <= 0x3F))
#define ESC_IS_END_BYTE(b) (b >= 0x40 && b <= 0x7E)
#define ESC_IS_PARAM_BYTE(b) (b >= 0x30 && b <= 0x3F)
#define ESC_IS_INTERMEDIATE_BYTE(b) (b >= 0x20 && b <= 0x2F)

#define ESC_FORMAT_NOT_MATCH      0
#define ESC_FORMAT_COMPILE_ERROR  1
#define ESC_FORMAT_OK             2

struct EscapeSequenceFormat {
  uint8_t type = 0;
  uint8_t private_param_char = 0;
  uint8_t param_count = 0;
  uint8_t end_char = 0;
};

constexpr bool compileESCFormat(uint8_t type, const char* str, EscapeSequenceFormat* escape_sequence_format) {
  escape_sequence_format->type = type;
  escape_sequence_format->param_count = 0;
  size_t cursor = 0;
  std::string_view sv(str);
  if(sv.empty()) {
    return false;
  }
  char next = sv[cursor++];
  escape_sequence_format->private_param_char = ESC_IS_PRIVATE_BYTE(next) ? next : 0;

  while(sv[cursor] == '#') {
    escape_sequence_format->param_count++;
    cursor++;
    if(sv[cursor] == ';') {
      cursor++;
      continue;
    }
    break;
  }
  if((sv.length() - cursor) == 1) {
    char last = sv[cursor];
    if(ESC_IS_END_BYTE(last)) {
      escape_sequence_format->end_char = last;
      return true;
    }
  }
  return false;
}

#endif //JTERMINAL_ENGINE_INCLUDE_ESCSEQ_H_
