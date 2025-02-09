package net.jterminal.ui.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class ComponentGroup {

  private final Set<Component> components = new HashSet<>();

  public synchronized void add(@NotNull Component...components) {
    for (@NotNull Component component : components) {
      this.components.add(component);
      component.addGroup(this);
    }
  }

  public synchronized void remove(@NotNull Component component) {
    component.removeGroup(this);
    components.remove(component);
  }

  public synchronized @NotNull Collection<Component> components() {
    return Collections.unmodifiableCollection(components);
  }

  public synchronized void clear() {
    for (Component component : components) {
      component.removeGroup(this);
    }
    components.clear();
  }

  public synchronized <T extends Component> @NotNull Collection<T> components(Class<T> type) {
    List<T> filteredList = new ArrayList<>();
    for (Component component : components) {
      if(component.getClass() == type) {
        T allowedComponent = type.cast(component);
        filteredList.add(allowedComponent);
      }
    }
    return filteredList;
  }

}
