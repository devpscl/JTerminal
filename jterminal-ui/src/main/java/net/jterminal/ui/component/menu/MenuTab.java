package net.jterminal.ui.component.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuTab {

  private final List<MenuItem> items = new ArrayList<>();
  private final String name;

  public MenuTab(@NotNull String name) {
    this.name = name;
  }

  public @Nullable MenuItem get(int index) {
    if(index < 0 || index >= items.size()) {
      return null;
    }
    return items.get(index);
  }

  public int preferredWidth() {
    int maxWidth = 0;
    for (MenuItem item : items) {
      maxWidth = Math.max(item.name().length(), maxWidth);
    }
    return maxWidth;
  }

  public @NotNull String name() {
    return name;
  }

  public void add(@NotNull MenuItem item) {
    items.add(item);
  }

  public void remove(@NotNull MenuItem item) {
    items.remove(item);
  }

  public int count() {
    return items.size();
  }

  public @NotNull Collection<MenuItem> items() {
    return Collections.unmodifiableCollection(items);
  }

}
