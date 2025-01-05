package net.jterminal.input;

import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class WindowInputEvent extends InputEvent {

  private final TermDim oldDimension;
  private final TermDim newDimension;

  public WindowInputEvent(@NotNull String raw, TermDim oldDimension,
      TermDim newDimension) {
    super(raw);
    this.oldDimension = oldDimension;
    this.newDimension = newDimension;
  }

  public WindowInputEvent(byte[] raw, int oldX, int oldY, int newX, int newY) {
    this(new String(raw), new TermDim(oldX, oldY),
        new TermDim(newX, newY));
  }

  @Override
  public final @NotNull Type type() {
    return Type.WINDOW;
  }

  public @NotNull TermDim oldDimension() {
    return oldDimension;
  }

  public @NotNull TermDim newDimension() {
    return newDimension;
  }

}
