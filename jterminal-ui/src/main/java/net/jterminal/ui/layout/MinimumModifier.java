package net.jterminal.ui.layout;

import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

final class MinimumModifier implements Layout.Modifier{

  private final int min;

  public MinimumModifier(int min) {
    this.min = min;
  }

  @Override
  public int get(@NotNull TermDim dim, int value, @NotNull Axis axis) {
    return Math.max(min, value);
  }
}
