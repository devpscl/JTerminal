# First CLI Application

Set up the terminal instance:
<code-block lang="java">
CLITerminal terminal = Terminal.create(CLITerminalProvider.class);
Terminal.set(terminal);
</code-block>

Create and set a command handler:
<code-block lang="java">
CommandParser&lt;CommandArgument&gt; commandParser = new DefaultCommandParser();
terminal.commandHandler(new AbstractCommandHandler&lt;&gt;(commandParser) {
  @Override
  public void command(CommandArgument[] args, @NotNull String line) {
    for (int idx = 0; idx &lt; args.length; idx++) {
      System.out.print(idx + ":" + args[idx].raw() + "; ");
    }
    System.out.println();
  }
});
</code-block>

Enable line reading:
<code-block lang="java">terminal.lineReading(true);</code-block>

