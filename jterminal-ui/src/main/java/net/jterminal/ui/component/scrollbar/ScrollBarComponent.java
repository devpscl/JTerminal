package net.jterminal.ui.component.scrollbar;

import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.ui.layout.Layout.Modifier;
import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class ScrollBarComponent extends Component implements Resizeable {

  private final VirtualScrollBar virtualScrollBar;

  public ScrollBarComponent(@NotNull Axis axis) {
    this.virtualScrollBar = new VirtualScrollBar(axis);
  }

  public @NotNull VirtualScrollBar virtualScrollBar() {
    return virtualScrollBar;
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    int len = virtualScrollBar.axis() == Axis.HORIZONTAL ?
        currentDimension().width() : currentDimension().height();
    virtualScrollBar.size(len);
    virtualScrollBar.draw(new TermPos(1, 1), graphics);
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    if(event.action() == Action.WHEEL_UP) {
      virtualScrollBar.scrollUp(1);
      repaint();
      return;
    }
    if(event.action() == Action.WHEEL_DOWN) {
      virtualScrollBar.scrollDown(1);
      repaint();
      return;
    }
    if((event.action() != Action.PRESS) || event.button() != Button.LEFT) {
      return;
    }
    TermPos position = event.position();
    int x = position.x();
    int y = position.y();
    if(x == 1 && y == 1) {
      virtualScrollBar.scrollUp(1);
      repaint();
      return;
    }
    if(virtualScrollBar.suffixCharPosition(new TermPos()).equals(position)) {
      virtualScrollBar.scrollDown(1);
      repaint();
    }
  }

  @Override
  public void height(int value, Modifier... modifiers) {
    if(virtualScrollBar.axis() == Axis.VERTICAL) {
      super.setHeight(value, modifiers);
    }
  }

  @Override
  public void height(DimensionValue positionValue, Modifier... modifiers) {
    if(virtualScrollBar.axis() == Axis.VERTICAL) {
      super.setHeight(positionValue, modifiers);
    }
  }

  @Override
  public void width(int value, Modifier... modifiers) {
    if(virtualScrollBar.axis() == Axis.HORIZONTAL) {
      super.setWidth(value, modifiers);
    }
  }

  @Override
  public void width(DimensionValue positionValue, Modifier... modifiers) {
    if(virtualScrollBar.axis() == Axis.HORIZONTAL) {
      super.setWidth(positionValue, modifiers);
    }
  }

}