package net.jterminal.cli.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RawTabCompleter implements TabCompleter {

  private final List<String> suggestionList = new ArrayList<>();
  private final int position;

  public RawTabCompleter(int position) {
    this.position = position;
  }

  public int position() {
    return position;
  }

  public void addSuggestions(@NotNull String...suggestions) {
    suggestionList.addAll(Arrays.asList(suggestions));
  }

  @Override
  public @Nullable TabCompletion generate(@NotNull String input, int cursor) {
    return TabCompletion.create(suggestionList, position);
  }
}
