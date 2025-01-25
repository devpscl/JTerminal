package net.jterminal.ui.event.special;

import net.devpscl.eventbus.Event;
import net.jterminal.ui.component.Component;
import org.jetbrains.annotations.NotNull;

public class AbstractComponentEvent<T extends Component> implements Event {

  private final T component;

  public AbstractComponentEvent(@NotNull T component) {
    this.component = component;
  }

  public @NotNull T component() {
    return component;
  }
}
