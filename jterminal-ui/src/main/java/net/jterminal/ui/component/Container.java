package net.jterminal.ui.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.jterminal.ui.exception.UIException;
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

  public @NotNull Layout layout() {
    return layout;
  }

  public void setLayout(@Nullable Layout layout) {
    this.layout = layout == null ? new AbsoluteLayout() : layout;
  }

  public @NotNull Collection<Component> components() {
    return Collections.unmodifiableList(childrenComponents);
  }

}
