package net.jterminal.input;

public interface Mouse {

  enum Button {
    NONE,
    LEFT,
    WHEEL,
    RIGHT
  }

  enum Action {
    PRESS,
    RELEASE,
    MOVE,
    WHEEL_UP,
    WHEEL_DOWN
  }

}
