package net.jterminal.cli.tab;

import java.util.List;
import org.jetbrains.annotations.NotNull;

class TabCompleterImpl implements TabCompleter {

  private final List<String> list;
  private final int position;
  private int suggestionIndex = 0;

  public TabCompleterImpl(List<String> list, int position) {
    this.list = list;
    this.position = position;
  }

  @Override
  public boolean empty() {
    return list.isEmpty();
  }

  @Override
  public int position() {
    return position;
  }

  @Override
  public int suggestionIndex() {
    return suggestionIndex;
  }

  @Override
  public @NotNull String suggestion() {
    if(list.isEmpty()) {
      return "";
    }
    return list.get(suggestionIndex);
  }

  @Override
  public void next() {
    if(suggestionIndex + 1 >= list.size()) {
      suggestionIndex = 0;
      return;
    }
    suggestionIndex++;
  }

  @Override
  public @NotNull List<String> suggestions() {
    return list;
  }

}
