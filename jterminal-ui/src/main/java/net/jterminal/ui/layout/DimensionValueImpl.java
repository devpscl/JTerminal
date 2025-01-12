package net.jterminal.ui.layout;

import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

final class DimensionValueImpl implements DimensionValue {

  private final int width;
  private final int height;

  public DimensionValueImpl(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public int width(@NotNull TermDim currentDim, @NotNull TermPos pos) {
    return width;
  }

  @Override
  public int height(@NotNull TermDim currentDim, @NotNull TermPos pos) {
    return height;
  }

}
