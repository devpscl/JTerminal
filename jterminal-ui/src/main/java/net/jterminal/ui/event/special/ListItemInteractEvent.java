package net.jterminal.ui.event.special;

import net.jterminal.ui.component.selectable.ListViewComponent;
import org.jetbrains.annotations.NotNull;

public class ListItemInteractEvent extends AbstractComponentEvent<ListViewComponent> {

  private final int index;

  public ListItemInteractEvent(@NotNull ListViewComponent component, int index) {
    super(component);
    this.index = index;
  }

  public final int index() {
    return index;
  }

}
