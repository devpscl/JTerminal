package net.jterminal.ui.component;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.layout.Layout;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Component implements Displayable, Comparable<Component> {

  private Container parent;
  private int containerIndex;
  private boolean visible = true;
  private ForegroundColor foregroundColor;
  private BackgroundColor backgroundColor;
  protected final Object lock = new Object();

  protected TerminalDimension size = new TerminalDimension(1, 1);
  protected TerminalPosition position = new TerminalPosition(1, 1);
  protected TerminalDimension effectiveSize = size.clone();
  protected TerminalPosition effectivePosition = position.clone();

  public void updatePositionSize() {
    if(parent != null) {
      final Layout layout = parent.layout();
      final TerminalDimension containerSize = parent.effectiveSize;
      effectiveSize = layout.resize(this, containerSize.clone());
      effectivePosition = layout.move(this, containerSize.clone());
    }
  }

  void setParent(@Nullable Container parent) {
    this.parent = parent;
  }

  public @Nullable Container parent() {
    return parent;
  }

  public @Nullable Container rootContainer() {
    if(parent == null) {
      return null;
    }
    return parent.rootContainer();
  }

  @Override
  public void repaint() {
    if(parent == null) {
      return;
    }
    parent.repaint();
  }

  @Override
  public void repaintFully() {
    if(parent == null) {
      return;
    }
    parent.repaintFully();
  }

  public int containerIndex() {
    return containerIndex;
  }

  public void containerIndex(int idx) {
    this.containerIndex = idx;
  }

  @Override
  public int compareTo(@NotNull Component o) {
    return Integer.compare(containerIndex, o.containerIndex);
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void visible(boolean visible) {
    this.visible = visible;
  }

  public void foregroundColor(@Nullable ForegroundColor foregroundColor) {
    this.foregroundColor = foregroundColor;
    repaint();
  }

  public void backgroundColor(@Nullable BackgroundColor backgroundColor) {
    this.backgroundColor = backgroundColor;
    repaint();
  }

  public @NotNull ForegroundColor foregroundColor() {
    if(foregroundColor == null) {
      if(parent == null) {
        return TerminalColor.DEFAULT;
      }
      return parent.foregroundColor();
    }
    return foregroundColor;
  }

  public @NotNull BackgroundColor backgroundColor() {
    if(backgroundColor == null) {
      if(parent == null) {
        return TerminalColor.DEFAULT;
      }
      return parent.backgroundColor();
    }
    return backgroundColor;
  }

  public boolean isForegroundColorSet() {
    return foregroundColor != null;
  }

  public boolean isBackgroundColorSet() {
    return foregroundColor != null;
  }

  public void size(@NotNull TerminalDimension size) {
    size.securePositive();
    this.size = size;
    repaint();
  }

  public @NotNull TerminalDimension size() {
    return size;
  }

  public void position(@NotNull TerminalPosition position) {
    position.secureTruePositive();
    this.position = position;
    repaint();
  }

  public @NotNull TerminalPosition position() {
    return position.clone();
  }

  public @NotNull TerminalDimension effectiveSize() {
    return effectiveSize.clone();
  }

  public @NotNull TerminalPosition effectivePosition() {
    return effectivePosition.clone();
  }

  public @NotNull TerminalPosition displayPosition() {
    if(parent == null) {
      return effectivePosition.clone();
    }
    return parent.displayPosition()
        .add(effectivePosition)
        .subtract(1);
  }

  public boolean contains(int x, int y) {
    TerminalPosition displayPosition = displayPosition();
    TerminalDimension displaySize = effectiveSize();
    if(x < displayPosition.x()) {
      return false;
    }
    if(y < displayPosition.y()) {
      return false;
    }
    if(x - displayPosition.x() >= displaySize.width()) {
      return false;
    }
    return y - displayPosition.y() < displaySize.height();
  }

}
