package net.jterminal.ui.event.special;

import net.jterminal.ui.component.selectable.ListViewComponent;
import org.jetbrains.annotations.NotNull;

public class ListItemShowEvent extends AbstractComponentEvent<ListViewComponent> {

  private final int index;

  public ListItemShowEvent(@NotNull ListViewComponent component, int index) {
    super(component);
    this.index = index;
  }

  public int index() {
    return index;
  }
}
