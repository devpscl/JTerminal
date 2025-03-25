package net.jterminal.cli.tab;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface TabCompletion {

  boolean empty();

  int position();

  int suggestionIndex();

  @NotNull String suggestion();

  void next();

  @NotNull List<String> suggestions();

  static @NotNull TabCompletion create(@NotNull List<String> suggestions, int position) {
    return new TabCompletionImpl(suggestions, position);
  }

}
