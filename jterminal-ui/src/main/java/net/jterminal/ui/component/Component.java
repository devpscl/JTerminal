package net.jterminal.ui.component;

import java.util.concurrent.atomic.AtomicLong;
import net.jterminal.event.bus.EventBus;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.component.ComponentResizeEvent;
import net.jterminal.ui.layout.DimProperty;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.layout.PosProperty;
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

  protected PosProperty xPosProperty = new PosProperty();
  protected PosProperty yPosProperty = new PosProperty();
  protected DimProperty widthDimProperty = new DimProperty();
  protected DimProperty heightDimProperty = new DimProperty();

  protected volatile TermPos cachedPosition = new TermPos();
  protected volatile TermDim cachedDim = new TermDim(1, 1);

  boolean interceptInput = false;

  public Component() {
     id = idCounter.getAndIncrement();
  }

  public long id() {
    return id;
  }

  public void recalculateProperties() {
    if(parent != null) {
      TermDim oldDim = cachedDim;
      TermDim currentDimension = parent.currentDimension();
      int x = xPosProperty.calculateX(currentDimension);
      int y = yPosProperty.calculateY(currentDimension);
      TermPos pos = new TermPos(x, y);
      int width = widthDimProperty.calculateWidth(currentDimension, pos);
      int height = heightDimProperty.calculateHeight(currentDimension, pos);
      TermDim dim = new TermDim(width, height);
      cachedPosition = pos;
      cachedDim = dim;
      if(!oldDim.equals(dim)) {
        processResizeEvent(new ComponentResizeEvent(oldDim, dim));
      }
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

  public @NotNull TermDim currentDimension() {
    if(parent == null) {
      return new TermDim();
    }
    return cachedDim.clone();
  }

  public @NotNull TermPos currentPosition() {
    if(parent == null) {
      return new TermPos();
    }
    return cachedPosition.clone();
  }

  public @NotNull TermPos currentDisplayPosition() {
    if(parent == null) {
      return currentPosition();
    }
    return parent.currentDisplayPosition()
        .add(cachedPosition)
        .subtract(TermPos.AXIS_ORIGIN);
  }

  public void x(int value, Layout.Modifier...modifiers) {
    x(Layout.createPositionValue(value, 0), modifiers);
  }

  public void x(Layout.PositionValue positionValue, Layout.Modifier...modifiers) {
    xPosProperty = new PosProperty(positionValue, modifiers);
    repaint();
  }

  public void y(int value, Layout.Modifier...modifiers) {
    y(Layout.createPositionValue(0, value), modifiers);
  }

  public void y(Layout.PositionValue positionValue, Layout.Modifier...modifiers) {
    yPosProperty = new PosProperty(positionValue, modifiers);
    repaint();
  }

  protected void setWidth(int value, Layout.Modifier...modifiers) {
    setWidth(Layout.createDimensionValue(value, 0), modifiers);
  }

  protected void setWidth(Layout.DimensionValue positionValue, Layout.Modifier...modifiers) {
    widthDimProperty = new DimProperty(positionValue, modifiers);
    repaint();
  }

  protected void setHeight(int value, Layout.Modifier...modifiers) {
    setHeight(Layout.createDimensionValue(0, value), modifiers);
  }

  protected void setHeight(Layout.DimensionValue positionValue, Layout.Modifier...modifiers) {
    heightDimProperty = new DimProperty(positionValue, modifiers);;
    repaint();
  }

  public @NotNull TermPos displayMidPosition() {
    TermPos start = currentDisplayPosition();
    TermDim effSize = currentDimension();
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

  @OverrideOnly
  public void processResizeEvent(@NotNull ComponentResizeEvent event) {}

  public boolean contains(int x, int y) {
    TermPos displayPosition = currentDisplayPosition();
    TermDim displaySize = currentDimension();
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
