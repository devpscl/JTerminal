package net.jterminal.ui.event.component;

import net.jterminal.event.Event;
import net.jterminal.input.KeyboardInputEvent;
import org.jetbrains.annotations.NotNull;

public class ComponentKeyEvent implements Event {

  private final KeyboardInputEvent event;

  public ComponentKeyEvent(@NotNull KeyboardInputEvent event) {
    this.event = event;
  }

  public @NotNull KeyboardInputEvent event() {
    return event;
  }

  public char input() {
    return event.input();
  }

  public int key() {
    return event.key();
  }

}
