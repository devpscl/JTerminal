package net.jterminal.ui.graphics;

import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TerminalState {

  public enum CursorType {
    STATIC,
    BLINKING,
    BLOCK
  }

  private TermPos cursorPosition;
  private CursorType cursorType;

  public TerminalState() {
    this(null, CursorType.STATIC);
  }

  public TerminalState(@Nullable TermPos cursorPosition,
      @NotNull CursorType cursorType) {
    this.cursorPosition = cursorPosition;
    this.cursorType = cursorType;
  }

  public void cursorPosition(@Nullable TermPos cursorPosition) {
    this.cursorPosition = cursorPosition;
  }

  public @Nullable TermPos cursorPosition() {
    if(cursorPosition == null) {
      return null;
    }
    return cursorPosition.clone();
  }

  public void cursorType(@NotNull CursorType cursorType) {
    this.cursorType = cursorType;
  }

  public @NotNull CursorType cursorType() {
    return cursorType;
  }

  public @NotNull TerminalState origin(@NotNull TermPos offset) {
    return new TerminalState(cursorPosition == null ? null
        : cursorPosition.addShift(offset), cursorType);
  }

}
