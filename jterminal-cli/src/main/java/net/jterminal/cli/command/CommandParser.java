package net.jterminal.cli.command;

import net.jterminal.cli.exception.CommandParseException;
import org.jetbrains.annotations.NotNull;

public interface CommandParser<T extends CommandArgument> {

  static @NotNull CommandParser<?> simple() {
    return new DefaultCommandParser();
  }

  T[] parse(@NotNull String line) throws CommandParseException;

  T parseArgument(@NotNull String x, int start, int end) throws CommandParseException;

}
