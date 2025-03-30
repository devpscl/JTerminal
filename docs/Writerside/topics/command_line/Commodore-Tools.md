# Commodore Tools

The Commodore framework offers the possibility to define several commands and provides a
clear structure in programming.

### Preparation
<code-block lang="java">
terminal.commandHandler(new ExampleCommandHandler()); //see next code block
LineReader lineReader = terminal.lineReader();
lineReader.tabCompleter(new CommandTabCompleter(terminal));
lineReader.inputHistory(InputHistory.create(false));
terminal.lineReading(true);
</code-block>

### Define command handler

Command: <code>service &lt;???&gt; &lt;start/stop&gt;</code>

<code-block lang="java">
public class ExampleCommandHandler extends 
    CommodoreHandler&lt;CommandArgument&gt; {

  public ExampleCommandHandler() {
    super(new DefaultCommandParser());
  }

  @Override
  protected void build(@NotNull CommodoreManager&lt;CommandArgument&gt; manager)
      throws CommandBuildException {
    manager.addCommand("service")
        .add(value("id")
            .suggestionResolver((suggestions, state) -> {
              suggestions.add(...)
            })
            .styleEditor(highlightStyle(TerminalColor.YELLOW))
            .add(literal("start")
                .execute(state -> {
                  CommandArgument arg = state.argumentById("id");
                  ...
                })
            ).add(literal("stop")
                .execute(state -> {
                  CommandArgument arg = state.argumentById("id");
                  ...
                })
            )
        );
    manager.unknownCommandExecute(state -> {
      System.out.println("Unknown Command!");
    });
  }
}
</code-block>

### Style Editor templates
There are style templates for arguments at <code>CommodoreHelper</code> interface:
- <code>highlightStyle(foreground_color)</code>
- <code>passwordStyle()</code>
- <code>numberInvalidStyle()</code>
- <code>modifiedStyle(func(state) => textstyle)</code>