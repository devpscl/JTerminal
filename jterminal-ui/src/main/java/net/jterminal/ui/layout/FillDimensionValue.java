package net.jterminal.ui.layout;

import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

final class FillDimensionValue implements DimensionValue {

  @Override
  public int width(@NotNull TermDim currentDim, @NotNull TermPos pos) {
    return currentDim.width() + TermPos.AXIS_ORIGIN - pos.x();
  }

  @Override
  public int height(@NotNull TermDim currentDim, @NotNull TermPos pos) {
    return currentDim.height() + TermPos.AXIS_ORIGIN - pos.y();
  }

}
