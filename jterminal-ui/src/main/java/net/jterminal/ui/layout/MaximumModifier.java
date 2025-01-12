package net.jterminal.ui.layout;

import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

final class MaximumModifier implements Layout.Modifier{

  private final int max;

  public MaximumModifier(int max) {
    this.max = max;
  }

  @Override
  public int get(@NotNull TermDim dim, int value, @NotNull Axis axis) {
    return Math.min(max, value);
  }
}
