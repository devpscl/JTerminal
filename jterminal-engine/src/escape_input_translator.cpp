#include "../include/terminput.h"

namespace jterminal {

void translateInput(InputEvent* event, uint8_t* bytes, size_t len) {
  event->raw = std::string(bytes, bytes + len);
  event->keyboard.wide_char = 0;
  event->type = InputType::Unknown;
  ESCBuffer buffer(bytes, len);
  if(!buffer.hasNext()) {
    event->keyboard.wide_char = 0;
    event->keyboard.key = KEY_UNKNOWN;
    event->keyboard.state = KS_NONE;
    event->type = InputType::Keyboard;
    return;
  }
  uint64_t hashcode = hashInput(bytes, len);
  if(translateInput(event, hashcode)) {
    event->keyboard.wide_char = buffer.readWideChar();
    event->type = InputType::Keyboard;
    return;
  }
  CSISequenceString sequence;
  if(!buffer.readSequence(&sequence)) {
    wchar_t wch = buffer.readWideChar();
    event->keyboard.wide_char = wch;
    event->keyboard.key = KEY_UNKNOWN;
    event->keyboard.state = KS_NONE;
    event->type = InputType::Keyboard;
    return;
  }
  if(sequence.endSymbol() == 'M' && sequence.paramCount() == 3 && sequence.privateChar() == '<') {
    event->mouse.position = pos_t{sequence[1], sequence[2]};
    switch (sequence[0]) {
      case 0:
        event->mouse.button = MOUSE_LEFT_BUTTON;
        event->mouse.action = MOUSE_ACTION_PRESS;
        break;
      case 1:
        event->mouse.button = MOUSE_WHEEL_BUTTON;
        event->mouse.action = MOUSE_ACTION_PRESS;
        break;
      case 2:
        event->mouse.button = MOUSE_RIGHT_BUTTON;
        event->mouse.action = MOUSE_ACTION_PRESS;
        break;
      case 64:
        event->mouse.button = MOUSE_WHEEL_BUTTON;
        event->mouse.action = MOUSE_ACTION_WHEEL_UP;
        break;
      case 65:
        event->mouse.button = MOUSE_WHEEL_BUTTON;
        event->mouse.action = MOUSE_ACTION_WHEEL_DOWN;
        break;
      case 32:
        event->mouse.button = MOUSE_LEFT_BUTTON;
        event->mouse.action = MOUSE_ACTION_MOVE;
        break;
      case 33:
        event->mouse.button = MOUSE_WHEEL_BUTTON;
        event->mouse.action = MOUSE_ACTION_MOVE;
        break;
      case 34:
        event->mouse.button = MOUSE_RIGHT_BUTTON;
        event->mouse.action = MOUSE_ACTION_MOVE;
        break;
      default:
        return;
    }
    event->type = InputType::Mouse;
    return;
  }
  if(sequence.endSymbol() == 'm' && sequence.paramCount() == 3 && sequence.privateChar() == '<') {
    event->mouse.position = pos_t{sequence[1], sequence[2]};
    switch (sequence[0]) {
      case 0:
        event->mouse.button = MOUSE_LEFT_BUTTON;
        event->mouse.action = MOUSE_ACTION_RELEASE;
        break;
      case 1:
        event->mouse.button = MOUSE_WHEEL_BUTTON;
        event->mouse.action = MOUSE_ACTION_RELEASE;
        break;
      case 2:
        event->mouse.button = MOUSE_RIGHT_BUTTON;
        event->mouse.action = MOUSE_ACTION_RELEASE;
        break;
      case 35:
        event->mouse.button = MOUSE_NONE;
        event->mouse.action = MOUSE_ACTION_MOVE;
        break;
      default:
        return;
    }
    event->type = InputType::Mouse;
    return;
  }
  if(sequence.endSymbol() == 'W' && sequence.paramCount() == 4 && sequence.privateChar() == '=') {
    event->window.new_size.width = sequence[0];
    event->window.new_size.height = sequence[1];
    event->window.old_size.width = sequence[2];
    event->window.old_size.height = sequence[3];
    event->type = InputType::Window;
    return;
  }

}

}