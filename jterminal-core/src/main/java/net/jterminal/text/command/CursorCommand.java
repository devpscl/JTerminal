package net.jterminal.text.command;

import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public interface CursorCommand extends AnsiCommand {

  static @NotNull CursorCommand move(@NotNull TermPos pos) {
    return new CursorCommandImpl("[" + pos.y() + ";" + pos.x() + "H");
  }

  static @NotNull CursorCommand home() {
    return new CursorCommandImpl("[H");
  }

  static @NotNull CursorCommand column(int col) {
    return new CursorCommandImpl("[" + col + "G");
  }

  static @NotNull CursorCommand lineUp(int count) {
    return new CursorCommandImpl("[" + count + "F");
  }

  static @NotNull CursorCommand lineDown(int count) {
    return new CursorCommandImpl("[" + count + "E");
  }

  static @NotNull CursorCommand nextLine() {
    return new CursorCommandImpl("E");
  }

  static @NotNull CursorCommand moveUp(int count) {
    return new CursorCommandImpl("[" + count + "A");
  }

  static @NotNull CursorCommand moveDown(int count) {
    return new CursorCommandImpl("[" + count + "B");
  }

  static @NotNull CursorCommand moveRight(int count) {
    return new CursorCommandImpl("[" + count + "C");
  }

  static @NotNull CursorCommand moveLeft(int count) {
    return new CursorCommandImpl("[" + count + "D");
  }

  static @NotNull CursorCommand blinking(boolean state) {
    if(state) {
      return new CursorCommandImpl("[?12h");
    }
    return new CursorCommandImpl("[?12l");
  }

  static @NotNull CursorCommand hide() {
    return new CursorCommandImpl("[?25l");
  }

  static @NotNull CursorCommand show() {
    return new CursorCommandImpl("[?25h");
  }

  static @NotNull CursorCommand savePosition() {
    return new CursorCommandImpl("[s");
  }

  static @NotNull CursorCommand restorePosition() {
    return new CursorCommandImpl("[u");
  }

}
