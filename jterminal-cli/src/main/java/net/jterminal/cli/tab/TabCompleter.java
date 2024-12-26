package net.jterminal.cli.tab;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface TabCompleter {

  boolean empty();

  int position();

  int suggestionIndex();

  @NotNull String suggestion();

  void next();

  @NotNull List<String> suggestions();

  static @NotNull TabCompleter create(@NotNull List<String> suggestions, int position) {
    return new TabCompleterImpl(suggestions, position);
  }

}
