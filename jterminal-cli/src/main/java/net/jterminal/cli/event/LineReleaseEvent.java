package net.jterminal.cli.event;

import net.devpscl.eventbus.Event;
import org.jetbrains.annotations.NotNull;

public class LineReleaseEvent implements Event {

  private final String line;

  public LineReleaseEvent(@NotNull String line) {
    this.line = line;
  }

  public @NotNull String line() {
    return line;
  }
}

