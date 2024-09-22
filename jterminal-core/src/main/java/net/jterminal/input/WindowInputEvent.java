package net.jterminal.input;

import net.jterminal.util.TerminalDimension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WindowInputEvent extends InputEvent {

  private final TerminalDimension oldDimension;
  private final TerminalDimension newDimension;

  public WindowInputEvent(@NotNull String raw, TerminalDimension oldDimension,
      TerminalDimension newDimension) {
    super(raw);
    this.oldDimension = oldDimension;
    this.newDimension = newDimension;
  }

  public WindowInputEvent(byte[] raw, int oldX, int oldY, int newX, int newY) {
    this(new String(raw), new TerminalDimension(oldX, oldY),
        new TerminalDimension(newX, newY));
  }

  @Override
  public final @NotNull Type type() {
    return Type.WINDOW;
  }

  public @NotNull TerminalDimension oldDimension() {
    return oldDimension;
  }

  public @NotNull TerminalDimension newDimension() {
    return newDimension;
  }

}
