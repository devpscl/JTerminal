package net.jterminal.cli.command;

import java.util.ArrayList;
import java.util.List;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;

public interface CommandHandler<T extends CommandArgument> {

  @NotNull CommandParser<T> parser();

  void command(T[] args, @NotNull String line);

  default @NotNull TermString view(@NotNull TermString view, T[] args) {
    return view;
  }

  default @NotNull List<String> getTabCompletions(T[] args, int cursor) {
    return new ArrayList<>();
  }


}
