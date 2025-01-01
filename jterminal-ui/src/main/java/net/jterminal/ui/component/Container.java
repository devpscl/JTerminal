package net.jterminal.ui.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.jterminal.ui.component.selectable.Selectable;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.exception.UIException;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.layout.AbsoluteLayout;
import net.jterminal.ui.layout.Layout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Container extends Component {

  private final List<Component> childrenComponents = new ArrayList<>();
  private Layout layout = new AbsoluteLayout();

  public void add(@NotNull Component component) {
    synchronized (lock) {
      if(component.parent() != null) {
        throw new UIException("Component is already used in another container");
      }
      component.setParent(this);
      childrenComponents.add(component);
      Collections.sort(childrenComponents);
    }
  }

  public boolean remove(@NotNull Component component) {
    synchronized (lock) {
      if(component.parent() != this) {
        return false;
      }
      component.setParent(null);
      childrenComponents.remove(component);
      return true;
    }
  }

  public void removeAll() {
    synchronized (lock) {
      Iterator<Component> iterator = childrenComponents.iterator();
      while (iterator.hasNext()) {
        iterator.next().setParent(null);
        iterator.remove();
      }
    }
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    for (Component childrenComponent : childrenComponents) {
      childrenComponent.processKeyEvent(event);
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    super.processMouseEvent(event);
    for (Component childrenComponent : childrenComponents) {
      ComponentMouseEvent shiftedEvent = event.shiftPosition(effectivePosition());
      childrenComponent.processMouseEvent(shiftedEvent);
    }
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    for (Component component : components()) {
      paint(graphics, component);
    }
  }

  public abstract void paint(@NotNull TermGraphics graphics, @NotNull Component component);

  public @NotNull Layout layout() {
    return layout;
  }

  public void setLayout(@Nullable Layout layout) {
    this.layout = layout == null ? new AbsoluteLayout() : layout;
  }

  public @NotNull Collection<Component> components() {
    return Collections.unmodifiableList(childrenComponents);
  }

  private void deepComponents(@NotNull List<Component> list, boolean onlySelectable) {
    for (Component childrenComponent : childrenComponents) {
      if(childrenComponent instanceof Container container) {
        container.deepComponents(list, onlySelectable);
      }
      if(!onlySelectable || childrenComponent instanceof Selectable) {
        list.add(childrenComponent);
      }
    }
  }

  public @NotNull Collection<Component> deepComponents() {
    List<Component> list = new ArrayList<>();
    deepComponents(list, false);
    return list;
  }

  public @NotNull Collection<Component> deepSelectableComponents() {
    List<Component> list = new ArrayList<>();
    deepComponents(list, true);
    return list;
  }

}
