package net.jterminal.text.command;

import org.jetbrains.annotations.NotNull;

public interface ScreenCommand extends AnsiCommand {

  static @NotNull ScreenCommand scrollUp() {
    return new ScreenCommandImpl("D");
  }

  static @NotNull ScreenCommand scrollDown() {
    return new ScreenCommandImpl("M");
  }

  static @NotNull ScreenCommand clearUp() {
    return new ScreenCommandImpl("[1J");
  }

  static @NotNull ScreenCommand clearDown() {
    return new ScreenCommandImpl("[0J");
  }

  static @NotNull ScreenCommand clear() {
    return new ScreenCommandImpl("[2J");
  }

  static @NotNull ScreenCommand clearLine() {
    return new ScreenCommandImpl("[2K");
  }

  static @NotNull ScreenCommand clearLeft() {
    return new ScreenCommandImpl("[1K");
  }

  static @NotNull ScreenCommand clearRight() {
    return new ScreenCommandImpl("[0K");
  }

}
