package net.jterminal.ui.graphics;

import net.jterminal.ui.util.PosDimUtil;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TerminalState {

  public enum CursorType {
    STATIC,
    BLINKING,
    BLOCK
  }

  private TerminalPosition cursorPosition;
  private CursorType cursorType;

  public TerminalState() {
    this(null, CursorType.STATIC);
  }

  public TerminalState(@Nullable TerminalPosition cursorPosition,
      @NotNull CursorType cursorType) {
    this.cursorPosition = cursorPosition;
    this.cursorType = cursorType;
  }

  public void cursorPosition(@Nullable TerminalPosition cursorPosition) {
    this.cursorPosition = cursorPosition;
  }

  public @Nullable TerminalPosition cursorPosition() {
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

  public @NotNull TerminalState offset(@NotNull TerminalPosition offset) {
    return new TerminalState(cursorPosition == null ? null
        : PosDimUtil.add(cursorPosition, offset), cursorType);
  }

}
