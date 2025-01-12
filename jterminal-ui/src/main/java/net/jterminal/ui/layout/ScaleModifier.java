package net.jterminal.ui.layout;

import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

final class ScaleModifier implements Layout.Modifier {

  private final float scale;

  public ScaleModifier(float scale) {
    this.scale = scale;
  }

  @Override
  public int get(@NotNull TermDim dim, int value, @NotNull Axis axis) {
    return (int) Math.floor(value * scale);
  }
}
