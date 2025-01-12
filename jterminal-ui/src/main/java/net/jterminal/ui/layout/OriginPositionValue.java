package net.jterminal.ui.layout;

import net.jterminal.ui.layout.Layout.PositionValue;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

final class OriginPositionValue implements PositionValue  {

  @Override
  public int x(@NotNull TermDim currentDim) {
    return 1;
  }

  @Override
  public int y(@NotNull TermDim currentDim) {
    return 1;
  }
}
