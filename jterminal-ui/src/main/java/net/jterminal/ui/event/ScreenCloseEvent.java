package net.jterminal.ui.event;

import net.jterminal.event.Event;
import net.jterminal.ui.TermScreen;
import org.jetbrains.annotations.NotNull;

public class ScreenCloseEvent implements Event {

  private final TermScreen screen;

  public ScreenCloseEvent(@NotNull TermScreen screen) {
    this.screen = screen;
  }

  public @NotNull TermScreen screen() {
    return screen;
  }
}
