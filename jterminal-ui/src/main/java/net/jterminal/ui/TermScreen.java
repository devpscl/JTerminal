package net.jterminal.ui;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.ComponentGraphics;
import net.jterminal.ui.component.Container;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public class TermScreen extends Container {

  UITerminal terminal;

  @Override
  public void repaint() {
    if(!isOpened()) {
      return;
    }
    terminal.drawScreen();
  }

  @Override
  public void repaintFully() {
    repaint();
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    for (Component component : components()) {
      component.updatePositionSize();
      ComponentGraphics.draw(graphics, component);
    }
  }

  public boolean isOpened() {
    if(terminal == null) {
      return false;
    }
    return terminal.openedScreen() == this;
  }

  @Override
  public @NotNull TerminalDimension size() {
    if(terminal == null) {
      return new TerminalDimension();
    }
    return terminal.windowSize();
  }

  @Override
  public @NotNull TerminalDimension effectiveSize() {
    return size();
  }

  @Override
  public @NotNull TerminalPosition position() {
    return new TerminalPosition(1, 1);
  }

  @Override
  public @NotNull TerminalPosition effectivePosition() {
    return new TerminalPosition(1, 1);
  }
}
