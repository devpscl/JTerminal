package net.jterminal.input;

public interface Keyboard {

  enum State {
    NONE,
    SHIFT,
    CONTROL
  }

  int KEY_UNKNOWN         = 0;
  int KEY_BACKSPACE       = 8;
  int KEY_TAB             = 9;
  int KEY_ENTER           = 13;
  int KEY_ESCAPE          = 27;
  int KEY_SPACE           = 32;
  int KEY_PAGE_UP         = 33;
  int KEY_PAGE_DOWN       = 34;
  int KEY_END             = 35;
  int KEY_HOME            = 36;
  int KEY_ARROW_LEFT      = 37;
  int KEY_ARROW_UP        = 38;
  int KEY_ARROW_RIGHT     = 39;
  int KEY_ARROW_DOWN      = 40;
  int KEY_PASTE           = 45;
  int KEY_DELETE          = 46;
  int KEY_0               = 48;
  int KEY_1               = 49;
  int KEY_2               = 50;
  int KEY_3               = 51;
  int KEY_4               = 52;
  int KEY_5               = 53;
  int KEY_6               = 54;
  int KEY_7               = 55;
  int KEY_8               = 56;
  int KEY_9               = 57;
  int KEY_A               = 65;
  int KEY_B               = 66;
  int KEY_C               = 67;
  int KEY_D               = 68;
  int KEY_E               = 69;
  int KEY_F               = 70;
  int KEY_G               = 71;
  int KEY_H               = 72;
  int KEY_I               = 73;
  int KEY_J               = 74;
  int KEY_K               = 75;
  int KEY_L               = 76;
  int KEY_M               = 77;
  int KEY_N               = 78;
  int KEY_O               = 79;
  int KEY_P               = 80;
  int KEY_Q               = 81;
  int KEY_R               = 82;
  int KEY_S               = 83;
  int KEY_T               = 84;
  int KEY_U               = 85;
  int KEY_V               = 86;
  int KEY_W               = 87;
  int KEY_X               = 88;
  int KEY_Y               = 89;
  int KEY_Z               = 90;
  int KEY_F1              = 112;
  int KEY_F2              = 113;
  int KEY_F3              = 114;
  int KEY_F4              = 115;
  int KEY_F5              = 116;
  int KEY_F6              = 117;
  int KEY_F7              = 118;
  int KEY_F8              = 119;
  int KEY_F9              = 120;
  int KEY_F10             = 121;
  int KEY_F11             = 122;
  int KEY_F12             = 123;

}
