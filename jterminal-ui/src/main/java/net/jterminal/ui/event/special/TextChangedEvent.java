package net.jterminal.ui.event.special;

import net.devpscl.eventbus.Event;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;

public class TextChangedEvent implements Event {

  private final TermString text;

  public TextChangedEvent(@NotNull TermString text) {
    this.text = text;
  }

  public @NotNull TermString text() {
    return text;
  }

}
