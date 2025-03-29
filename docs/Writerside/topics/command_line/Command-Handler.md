# Command Handler

The command handler is the base for creating your own commands.

### Preparation
<code-block lang="java">
terminal.commandHandler(...);
LineReader lineReader = terminal.lineReader();
lineReader.tabCompleter(new CommandTabCompleter(terminal));
lineReader.inputHistory(InputHistory.create(false));
terminal.lineReading(true);
</code-block>

### Define command handler
<code-block lang="java">
private static class ExampleCommandHandler implements
    CommandHandlerr&lt;CommandArgument&gt; {

  @Override
  public @NotNull CommandParserr&lt;CommandArgument&gt; parser() {
    return new DefaultCommandParser();
  }

  @Override
  public void command(CommandArgument[] args, @NotNull String line) {
    ...
  }

  //optional method
  @Override
  public @NotNull TermString view(@NotNull TermString view, 
    CommandArgument[] args) {
    //return new view of line
    return view;
  }

  //optional method
  @Override
  public @NotNull List&lt;String&gt; getTabCompletions(
    CommandArgument[] args, int cursor) {
    List&lt;String&gt; tabCompletions = new ArrayList&lt;&gt;();
    tabCompletions.add(...);
    return tabCompletions;
  }
}
</code-block>



