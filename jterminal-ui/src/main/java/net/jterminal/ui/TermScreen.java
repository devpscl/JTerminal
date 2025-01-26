package net.jterminal.ui;

import net.jterminal.ui.component.RootContainer;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TermScreen extends RootContainer {

  UITerminal terminal;
  private TermDim originalSize = null;
  private boolean mouseInputEnabled = false;
  private boolean mouseMoveInputEnabled = false;

  @Override
  public void repaint() {
    if(!isOpened()) {
      return;
    }
    terminal.drawScreen();
  }

  @Override
  public void repaintFully() {
    if(!isOpened()) {
      return;
    }
    terminal.drawNewScreen();
  }

  public boolean isOpened() {
    if(terminal == null) {
      return false;
    }
    return terminal.openedScreen() == this;
  }

  public void setOriginalSize(@Nullable TermDim originalSize) {
    this.originalSize = originalSize;
  }

  @Override
  public @NotNull TermDim currentDimension() {
    if(terminal == null) {
      return defaultDimension();
    }
    return terminal.windowSize();
  }

  public @NotNull TermDim defaultDimension() {
    if(originalSize != null) {
      return originalSize;
    }
    if(terminal == null) {
      return new TermDim();
    }
    return terminal.defaultWindowSize();
  }

  @Override
  public @NotNull TermPos currentPosition() {
    return new TermPos(1, 1);
  }

  @Override
  public @NotNull TermPos currentDisplayPosition() {
    return new TermPos(1, 1);
  }

  public boolean isMouseInputEnabled() {
    return mouseInputEnabled;
  }

  public boolean isMouseMoveInputEnabled() {
    return mouseMoveInputEnabled;
  }

  public void setMouseInputEnabled(boolean mouseInputEnabled) {
    this.mouseInputEnabled = mouseInputEnabled;
    repaintFully();
  }

  public void setMouseMoveInputEnabled(boolean mouseMoveInputEnabled) {
    this.mouseMoveInputEnabled = mouseMoveInputEnabled;
    repaintFully();
  }
}
