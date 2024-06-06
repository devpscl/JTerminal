#ifndef JTERMINAL_ENGINE_INCLUDE_TERMINPUT_H_
#define JTERMINAL_ENGINE_INCLUDE_TERMINPUT_H_
#include "termdef.h"
#include <mutex>
#include <condition_variable>
#include <deque>
#include <sstream>
#include "../include/bufnio.h"

namespace jterminal {

#define MAXIMUM_PIPELINE_COUNT  8

#define KEY_UNKNOWN         (uint8_t)0
#define KEY_BACKSPACE       (uint8_t)8
#define KEY_TAB             (uint8_t)9
#define KEY_ENTER           (uint8_t)13

#define KEY_ESCAPE          (uint8_t)27
#define KEY_SPACE           (uint8_t)32
#define KEY_PAGE_UP         (uint8_t)33
#define KEY_PAGE_DOWN       (uint8_t)34
#define KEY_END             (uint8_t)35
#define KEY_HOME            (uint8_t)36
#define KEY_ARROW_LEFT      (uint8_t)37
#define KEY_ARROW_UP        (uint8_t)38
#define KEY_ARROW_RIGHT     (uint8_t)39
#define KEY_ARROW_DOWN      (uint8_t)40

#define KEY_PASTE           (uint8_t)45
#define KEY_DELETE          (uint8_t)46

#define KEY_0               (uint8_t)48
#define KEY_1               (uint8_t)49
#define KEY_2               (uint8_t)50
#define KEY_3               (uint8_t)51
#define KEY_4               (uint8_t)52
#define KEY_5               (uint8_t)53
#define KEY_6               (uint8_t)54
#define KEY_7               (uint8_t)55
#define KEY_8               (uint8_t)56
#define KEY_9               (uint8_t)57

#define KEY_A               (uint8_t)65
#define KEY_B               (uint8_t)66
#define KEY_C               (uint8_t)67
#define KEY_D               (uint8_t)68
#define KEY_E               (uint8_t)69
#define KEY_F               (uint8_t)70
#define KEY_G               (uint8_t)71
#define KEY_H               (uint8_t)72
#define KEY_I               (uint8_t)73
#define KEY_J               (uint8_t)74
#define KEY_K               (uint8_t)75
#define KEY_L               (uint8_t)76
#define KEY_M               (uint8_t)77
#define KEY_N               (uint8_t)78
#define KEY_O               (uint8_t)79
#define KEY_P               (uint8_t)80
#define KEY_Q               (uint8_t)81
#define KEY_R               (uint8_t)82
#define KEY_S               (uint8_t)83
#define KEY_T               (uint8_t)84
#define KEY_U               (uint8_t)85
#define KEY_V               (uint8_t)86
#define KEY_W               (uint8_t)87
#define KEY_X               (uint8_t)88
#define KEY_Y               (uint8_t)89
#define KEY_Z               (uint8_t)90

#define KEY_F1              (uint8_t)112
#define KEY_F2              (uint8_t)113
#define KEY_F3              (uint8_t)114
#define KEY_F4              (uint8_t)115
#define KEY_F5              (uint8_t)116
#define KEY_F6              (uint8_t)117
#define KEY_F7              (uint8_t)118
#define KEY_F8              (uint8_t)119
#define KEY_F9              (uint8_t)120
#define KEY_F10             (uint8_t)121
#define KEY_F11             (uint8_t)122
#define KEY_F12             (uint8_t)123

#define INPUT_PRIO_LOW                0
#define INPUT_PRIO_NORMAL             1
#define INPUT_PRIO_HIGH               2
#define INPUT_PRIO_HIGHEST_SINGLETON  0xFF

#define MOUSE_ACTION_PRESS      1
#define MOUSE_ACTION_RELEASE    2
#define MOUSE_ACTION_MOVE       3
#define MOUSE_ACTION_WHEEL_UP   4
#define MOUSE_ACTION_WHEEL_DOWN 5

#define MOUSE_NONE              0
#define MOUSE_LEFT_BUTTON       1
#define MOUSE_WHEEL_BUTTON      2
#define MOUSE_RIGHT_BUTTON      3
#define MOUSE_WHEEL_UP          4
#define MOUSE_WHEEL_DOWN        5


#define KS_NONE     0
#define KS_SHIFT    1
#define KS_CONTROL  2

enum class InputType {
  Keyboard,
  Mouse,
  Window,
  Unknown
};

struct InputEvent {
  InputType type = InputType::Unknown;
  uint32_t symbol = 0;
  struct {
    uint8_t key = KEY_UNKNOWN;
    uint8_t state = KS_NONE;
  } keyboard;
  struct {
    uint8_t action = MOUSE_ACTION_PRESS;
    uint8_t button = MOUSE_NONE;
    pos_t position;
  } mouse;
  struct {
    dim_t old_size;
    dim_t new_size;
  } window;
};

class InputPipeline {
  uint8_t priority_;
  uint32_t timeout_millis_;
  std::mutex mutex_;
  std::condition_variable condition_variable_;
  std::stringbuf buf_;
  size_t buf_size_ = 0xFFFF;

  void write(const uint8_t* bytes, size_t len);

  friend class Terminal;

 public:

  explicit InputPipeline(uint8_t priority = INPUT_PRIO_LOW, uint32_t timeout_millis = 0xFFFF);

  ~InputPipeline();

  void setBufferSize(size_t size);

  size_t available();

  [[nodiscard]] uint8_t priority() const;

  size_t read(uint8_t* bytes, size_t len);

  void detach();

};

bool translateInput(InputEvent* event, StringBuffer* buffer);

bool translateInput(InputEvent* event, const uint64_t& hash);

uint64_t hashInput(const uint8_t* bytes, size_t len);

}

#endif //JTERMINAL_ENGINE_INCLUDE_TERMINPUT_H_
