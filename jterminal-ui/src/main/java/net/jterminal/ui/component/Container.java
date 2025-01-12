package net.jterminal.ui.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.exception.UIException;
import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public abstract class Container extends Component {

  private final List<Component> childrenComponents = new ArrayList<>();

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

  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {

  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    for (Component component : components()) {
      if(!component.isVisible()) {
        continue;
      }
      paint(graphics, component);
    }
  }

  public abstract void paint(@NotNull TermGraphics graphics, @NotNull Component component);

  public @NotNull Collection<Component> components() {
    return Collections.unmodifiableList(childrenComponents);
  }

  private void deepComponents(@NotNull List<Component> list) {
    for (Component childrenComponent : childrenComponents) {
      if(childrenComponent instanceof Container container) {
        container.deepComponents(list);
      }
      list.add(childrenComponent);
    }
  }

  private void deepSelectableComponents(@NotNull List<SelectableComponent> list) {
    for (Component childrenComponent : childrenComponents) {
      if(childrenComponent instanceof Container container) {
        container.deepSelectableComponents(list);
      }
      if(childrenComponent instanceof SelectableComponent selectableComponent) {
        list.add(selectableComponent);
      }
    }
  }

  public @NotNull Collection<Component> deepComponents() {
    List<Component> list = new ArrayList<>();
    deepComponents(list);
    Collections.sort(list);
    return list;
  }

  public @NotNull Collection<SelectableComponent> deepSelectableComponents() {
    List<SelectableComponent> list = new ArrayList<>();
    deepSelectableComponents(list);
    Collections.sort(list);
    return list;
  }

}
