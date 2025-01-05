package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class RelativeLayout implements Layout {

  @Override
  public void addComponent(@NotNull Component component) {

  }

  @Override
  public void removeComponent(@NotNull Component component) {

  }

  @Override
  public @NotNull TermPos move(@NotNull Component component,
      @NotNull TermDim containerSize,
      @NotNull TermDim originalContainerSize) {
    int difWidth = containerSize.width() - originalContainerSize.width();
    int difHeight = containerSize.height() - originalContainerSize.height();

    return component.position().addX(difWidth).addY(difHeight);
  }

  @Override
  public @NotNull TermDim resize(@NotNull Component component,
      @NotNull TermDim containerSize,
      @NotNull TermDim originalContainerSize) {
    if(!component.isResizeable()) {
      return component.size();
    }
    Resizeable resizeable = (Resizeable) component;
    TermDim size = component.size();
    int difWidth = containerSize.width() - originalContainerSize.width();
    int difHeight = containerSize.height() - originalContainerSize.height();

    int width = Math.max(resizeable.minimumSize().width(),
        Math.min(resizeable.maximumSize().width(), size.width() + difWidth));
    int height = Math.max(resizeable.minimumSize().height(),
        Math.min(resizeable.maximumSize().height(), size.height() + difHeight));

    int newWidth = Math.max(0, width);
    int newHeight = Math.max(0, height);

    return new TermDim(newWidth, newHeight);
  }
}
