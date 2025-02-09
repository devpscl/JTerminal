package net.jterminal.ui.event.special;

import net.jterminal.ui.component.selectable.ListChooseComponent;
import org.jetbrains.annotations.NotNull;

public class ListItemChooseEvent extends AbstractComponentEvent<ListChooseComponent> {

  private final int selectedIndex;

  public ListItemChooseEvent(@NotNull ListChooseComponent component, int selectedIndex) {
    super(component);
    this.selectedIndex = selectedIndex;
  }

  public final int selectedIndex() {
    return selectedIndex;
  }
}
