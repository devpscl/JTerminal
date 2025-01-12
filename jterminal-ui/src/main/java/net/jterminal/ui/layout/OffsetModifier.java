package net.jterminal.ui.layout;

import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

final class OffsetModifier implements Layout.Modifier {

  private final int offset;

  public OffsetModifier(int offset) {
    this.offset = offset;
  }

  @Override
  public int get(@NotNull TermDim dim, int value, @NotNull Axis axis) {
    return value + offset;
  }
}
