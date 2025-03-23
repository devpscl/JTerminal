package net.jterminal.cli.command;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommandHandler<T extends CommandArgument>
    implements CommandHandler<T> {

  private final CommandParser<T> parser;

  public AbstractCommandHandler(@NotNull CommandParser<T> parser) {
    this.parser = parser;
  }

  @Override
  public @NotNull CommandParser<T> parser() {
    return parser;
  }

}
