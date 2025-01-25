package net.jterminal.ui.event.special;

import net.jterminal.ui.component.selectable.ListComponent;
import org.jetbrains.annotations.NotNull;

public class ListChooseItemEvent extends AbstractComponentEvent<ListComponent> {

  private final int selectedIndex;

  public ListChooseItemEvent(@NotNull ListComponent component, int selectedIndex) {
    super(component);
    this.selectedIndex = selectedIndex;
  }

  public final int selectedIndex() {
    return selectedIndex;
  }
}
