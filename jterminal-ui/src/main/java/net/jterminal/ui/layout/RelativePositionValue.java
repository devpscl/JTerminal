package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.layout.Layout.PositionValue;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class RelativePositionValue implements PositionValue {

  private final @Nullable Component source;
  private final @NotNull Anchor anchor;

  public RelativePositionValue(@Nullable Component source, @NotNull Anchor anchor) {
    this.source = source;
    this.anchor = anchor;
  }

  @Override
  public int x(@NotNull TermDim currentDim) {
    if(source == null) {
      if(anchor == Anchor.TOP || anchor == Anchor.BOTTOM || anchor == Anchor.LEFT) {
        return 1;
      }
      return currentDim.width();
    }
    source.recalculateProperties();
    if(anchor == Anchor.TOP || anchor == Anchor.BOTTOM) {
      return 1;
    }
    if(anchor == Anchor.RIGHT) {
      return source.currentPosition().x() + source.currentDimension().width();
    }
    return source.currentPosition().x();
  }

  @Override
  public int y(@NotNull TermDim currentDim) {
    if(source == null) {
      if(anchor == Anchor.LEFT || anchor == Anchor.RIGHT || anchor == Anchor.TOP) {
        return 1;
      }
      return currentDim.height();
    }
    source.recalculateProperties();
    if(anchor == Anchor.LEFT || anchor == Anchor.RIGHT) {
      return 1;
    }
    if(anchor == Anchor.BOTTOM) {
      return source.currentPosition().y() + source.currentDimension().height();
    }
    return source.currentPosition().y();
  }
}
