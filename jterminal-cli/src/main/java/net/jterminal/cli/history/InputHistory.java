package net.jterminal.cli.history;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InputHistory extends Iterable<String> {

  void add(@NotNull String input);

  @Nullable String get(int index);

  int cursor();

  void cursor(int cursor);

  boolean empty();

  boolean selectPrevHistory();

  boolean selectNextHistory();

  boolean isAnySelected();

  void unselect();

  int count();

  void clear();

  @NotNull InputHistory copy();

  @NotNull InputHistory copy(boolean allowMultipleInputs);

  static @NotNull InputHistory create(boolean allowMultipleInputs) {
    return new InputHistoryImpl(0, new ArrayList<>(), allowMultipleInputs);
  }

}
