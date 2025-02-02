package net.jterminal.ui.layout;

import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

final class FillDimensionValue implements DimensionValue {

  private final float modifier;

  public FillDimensionValue(float modifier) {
    this.modifier = modifier;
  }

  @Override
  public int width(@NotNull TermDim currentDim, @NotNull TermPos pos) {
    int width = Math.round(currentDim.width() * modifier);
    return width + TermPos.AXIS_ORIGIN - pos.x();
  }

  @Override
  public int height(@NotNull TermDim currentDim, @NotNull TermPos pos) {
    int height = Math.round(currentDim.height() * modifier);
    return height + TermPos.AXIS_ORIGIN - pos.y();
  }

}
