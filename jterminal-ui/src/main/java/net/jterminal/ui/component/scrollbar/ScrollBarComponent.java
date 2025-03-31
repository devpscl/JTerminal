package net.jterminal.ui.component.scrollbar;

import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.ui.layout.Layout.Modifier;
import net.jterminal.ui.scrollbar.ScrollBar;
import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class ScrollBarComponent extends Component implements Resizeable {

  protected final ScrollBar scrollBar;

  public ScrollBarComponent(@NotNull Axis axis) {
    this.scrollBar = ScrollBar.create(axis);
  }

  public @NotNull ScrollBar scrollBar() {
    return scrollBar;
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    int len = scrollBar.axis() == Axis.HORIZONTAL ?
        currentDimension().width() : currentDimension().height();
    scrollBar.draw(graphics, len);
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    if(event.action() == Action.WHEEL_UP) {
      scrollBar.scrollUp(1);
      repaint();
      return;
    }
    if(event.action() == Action.WHEEL_DOWN) {
      scrollBar.scrollDown(1);
      repaint();
      return;
    }
    if((event.action() != Action.PRESS && event.action() != Action.MOVE)
        || event.button() != Button.LEFT) {
      return;
    }
    TermPos position = event.position();
    int x = position.x();
    int y = position.y();
    if(x == 1 && y == 1) {
      scrollBar.scrollUp(1);
      repaint();
      return;
    }
    int len = scrollBar.axis() == Axis.HORIZONTAL ?
        currentDimension().width() : currentDimension().height();
    if (scrollBar.performInteract(position, len)) {
      repaint();
    }

  }

  protected @NotNull TermPos suffixPosition(int len) {
    if(scrollBar.axis() == Axis.VERTICAL) {
      return new TermPos(1, len);
    }
    return new TermPos(len, 1);
  }

  @Override
  public void height(int value, Modifier... modifiers) {
    if(scrollBar.axis() == Axis.VERTICAL) {
      super.setHeight(value, modifiers);
    }
  }

  @Override
  public void height(DimensionValue positionValue, Modifier... modifiers) {
    if(scrollBar.axis() == Axis.VERTICAL) {
      super.setHeight(positionValue, modifiers);
    }
  }

  @Override
  public void width(int value, Modifier... modifiers) {
    if(scrollBar.axis() == Axis.HORIZONTAL) {
      super.setWidth(value, modifiers);
    }
  }

  @Override
  public void width(DimensionValue positionValue, Modifier... modifiers) {
    if(scrollBar.axis() == Axis.HORIZONTAL) {
      super.setWidth(positionValue, modifiers);
    }
  }

}