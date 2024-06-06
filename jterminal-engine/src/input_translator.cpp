#include "../include/terminput.h"


bool jterminal::translateInput(jterminal::InputEvent *event, jterminal::StringBuffer* buffer) {
  if(!buffer->hasNext()) {
    return false;
  }
  auto& esc_buffer = buffer->downcast<ESCBuffer>();
  event->type = InputType::Unknown;
  event->symbol = buffer->peekWideChar();
  size_t len = esc_buffer.scanESCLength();
  if(len == 0) {
    len = esc_buffer.peekWideCharLength();
  }
  uint8_t bytes[len];
  esc_buffer.peek(bytes, len);
  uint64_t hashcode = hashInput(bytes, len);
  if(translateInput(event, hashcode)) {
    esc_buffer.skip(len);
    event->type = InputType::Keyboard;
    return true;
  }
  int param_data[4];
  uint8_t esf_status;
  size_t esc_len = esc_buffer.peekSequenceFormat(ESC_CSI, "<#;#;#M", param_data,
                                                 4, &esf_status);
  if(esf_status == ESC_FORMAT_OK) {
    esc_buffer.skip(esc_len);
    event->mouse.position = pos_t{param_data[1], param_data[2]};
    switch (param_data[0]) {
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
        return false;
    }
    event->type = InputType::Mouse;
    return true;
  }
  esc_len = esc_buffer.peekSequenceFormat(ESC_CSI, "<#;#;#m", param_data,
                                          4, &esf_status);
  if(esf_status == ESC_FORMAT_OK) {
    esc_buffer.skip(esc_len);
    event->mouse.position = pos_t{param_data[1], param_data[2]};
    switch (param_data[0]) {
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
        return false;
    }
    event->type = InputType::Mouse;
    return true;
  }
  esc_len = esc_buffer.peekSequenceFormat(ESC_CSI, ":#;#;#;#W", param_data,
                                          4, &esf_status);
  if(esf_status == ESC_FORMAT_OK) {
    esc_buffer.skip(esc_len);
    event->window.new_size.width = param_data[0];
    event->window.new_size.height = param_data[1];
    event->window.old_size.width = param_data[2];
    event->window.old_size.width = param_data[3];
    event->type == InputType::Window;
    return true;
  }
  return false;
}