package net.jterminal.cli.commodore;

import java.util.Set;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.exception.CommandExecuteException;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;

public interface CommodoreFunctional {

  interface Executor<T extends CommandArgument> {

    void exec(CommandState<T> state) throws CommandExecuteException;

  }

  interface SuggestionResolver<T extends CommandArgument> {

    void resolve(@NotNull Set<String> suggestions, @NotNull CommandState<T> state);

  }

  interface StyleEditor<T extends CommandArgument> {

    @NotNull TermString edit(@NotNull TermString input,
        @NotNull CommandState<T> state);

  }

}
