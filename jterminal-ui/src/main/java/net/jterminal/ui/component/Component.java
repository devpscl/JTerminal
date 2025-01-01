package net.jterminal.ui.component;

import java.util.concurrent.atomic.AtomicLong;
import net.jterminal.event.bus.EventBus;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.layout.Layout;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Component implements Displayable, Comparable<Component> {

  private static final AtomicLong idCounter = new AtomicLong(1);

  private Container parent;
  private int priority;
  private boolean visible = true;
  private ForegroundColor foregroundColor;
  private BackgroundColor backgroundColor;
  protected final Object lock = new Object();
  protected final long id;
  private final EventBus eventBus = EventBus.create();

  protected TerminalDimension size = new TerminalDimension(1, 1);
  protected TerminalPosition position = new TerminalPosition(1, 1);
  protected TerminalDimension effectiveSize = size.clone();
  protected TerminalPosition effectivePosition = position.clone();

  public Component() {
     id = idCounter.getAndIncrement();
  }

  public long id() {
    return id;
  }

  public void updatePositionSize() {
    if(parent != null) {
      final Layout layout = parent.layout();
      final TerminalDimension containerSize = parent.effectiveSize();
      final TerminalDimension containerOriginalSize = parent.size();
      effectiveSize = layout.resize(this, containerSize.clone(),
          containerOriginalSize.clone());
      effectivePosition = layout.move(this, containerSize, containerOriginalSize);
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
      if(this instanceof Container container) {
        return container;
      }
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

  public int priority() {
    return priority;
  }

  public void priority(int idx) {
    this.priority = idx;
  }

  public int depthIndex() {
    if(parent == null) {
      return 0;
    }
    return parent.depthIndex() + 1;
  }

  @Override
  public int compareTo(@NotNull Component o) {
    if(o instanceof SelectableComponent ^ this instanceof SelectableComponent) {
      if(o instanceof SelectableComponent) {
        return -1;
      }
      return 1;
    }
    if(priority == o.priority) {
      return Long.compare(id, o.id);
    }
    return Integer.compare(priority, o.priority);
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

  public boolean isSelectable() {
    return this instanceof SelectableComponent;
  }

  public boolean isResizeable() {
    return this instanceof Resizeable;
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

  public @NotNull TerminalPosition displayMidPosition() {
    TerminalPosition start = displayPosition();
    TerminalDimension effSize = effectiveSize();
    int midWidth = Math.max((effSize.width()-1)/2, 0);
    int midHeight = Math.max((effSize.height()-1)/2, 0);
    return start.addX(midWidth).addY(midHeight);
  }

  public @NotNull EventBus eventBus() {
    return eventBus;
  }

  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    eventBus.post(event);
  }

  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    eventBus.post(event);
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
