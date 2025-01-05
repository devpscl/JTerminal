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
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
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
  protected final EventBus eventBus = EventBus.create();

  protected TermDim size = new TermDim(1, 1);
  protected TermPos position = new TermPos(1, 1);
  protected TermDim effectiveSize = size.clone();
  protected TermPos effectivePosition = position.clone();

  public Component() {
     id = idCounter.getAndIncrement();
  }

  public long id() {
    return id;
  }

  public void updatePositionSize() {
    if(parent != null) {
      final Layout layout = parent.layout();
      final TermDim containerSize = parent.effectiveSize();
      final TermDim containerOriginalSize = parent.size();
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

  public @NotNull TermDim size() {
    return size;
  }

  public void position(@NotNull TermPos position) {
    position.secureOriginPositive();
    this.position = position;
    repaint();
  }

  public @NotNull TermPos position() {
    return position.clone();
  }

  public @NotNull TermDim effectiveSize() {
    return effectiveSize.clone();
  }

  public @NotNull TermPos effectivePosition() {
    return effectivePosition.clone();
  }

  public @NotNull TermPos displayPosition() {
    if(parent == null) {
      return effectivePosition.clone();
    }
    return parent.displayPosition()
        .add(effectivePosition)
        .subtract(1);
  }

  public @NotNull TermPos displayMidPosition() {
    TermPos start = displayPosition();
    TermDim effSize = effectiveSize();
    int midWidth = Math.max((effSize.width()-1)/2, 0);
    int midHeight = Math.max((effSize.height()-1)/2, 0);
    return start.addX(midWidth).addY(midHeight);
  }

  public @NotNull EventBus eventBus() {
    return eventBus;
  }

  @OverrideOnly
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {}

  @OverrideOnly
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {}

  public boolean contains(int x, int y) {
    TermPos displayPosition = displayPosition();
    TermDim displaySize = effectiveSize();
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
