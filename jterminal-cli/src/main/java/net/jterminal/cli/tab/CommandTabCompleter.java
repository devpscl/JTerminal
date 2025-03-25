package net.jterminal.cli.tab;

import java.util.List;
import net.jterminal.cli.CLITerminal;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.command.CommandHandler;
import net.jterminal.cli.exception.CommandParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandTabCompleter implements TabCompleter {

  private final CLITerminal terminal;

  public CommandTabCompleter(@NotNull CLITerminal terminal) {
    this.terminal = terminal;
  }

  @SuppressWarnings("unchecked")
  @Override
  public @Nullable TabCompletion generate(@NotNull String input, int cursor) {
    CommandHandler<?> commandHandler = terminal.commandHandler();
    if(commandHandler == null) {
      return null;
    }
    try {
      CommandArgument[] args = terminal.parseArguments(input);
      CommandHandler<CommandArgument> raw = (CommandHandler<CommandArgument>)
          commandHandler;
      List<String> tabCompletions = raw.getTabCompletions(args, cursor);
      int position;
      if(args.length == 0) {
        position = 0;
      } else {
        CommandArgument arg = args[args.length - 1];
        if(arg.positionEnd() < cursor) {
          position = cursor;
        } else {
          position = arg.positionStart();
        }
      }
      return TabCompletion.create(tabCompletions, position);
    } catch (CommandParseException e) {
      CLITerminal.LOGGER.error(e);
    }
    return null;
  }
}
