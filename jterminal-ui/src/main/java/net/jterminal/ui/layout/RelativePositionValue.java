package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.layout.Layout.PositionValue;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class RelativePositionValue implements PositionValue {

  private final @Nullable Component source;
  private final @NotNull Anchor anchor;
  private final boolean dockingAnchor;

  public RelativePositionValue(@Nullable Component source, @NotNull Anchor anchor, boolean dockingAnchor) {
    this.source = source;
    this.anchor = anchor;
    this.dockingAnchor = dockingAnchor;
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
    int value;
    if(anchor == Anchor.RIGHT) {
      value = source.currentPosition().x() + source.currentDimension().width();
      return dockingAnchor ? value : value - 1;
    }
    value = source.currentPosition().x();
    return dockingAnchor ? value - 1 : value;
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
    int value;
    if(anchor == Anchor.BOTTOM) {
      value = source.currentPosition().y() + source.currentDimension().height();
      return dockingAnchor ? value : value - 1;
    }
    value = source.currentPosition().y();
    return dockingAnchor ? value - 1 : value;
  }
}
