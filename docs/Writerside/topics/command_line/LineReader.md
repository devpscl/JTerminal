# LineReader

The LineReader class contains settings for reading out command lines as well as the current 
state of the line input.

## Flags
Certain options can be selected when reading the lines:
<code-block lang="Java">
LineReader lineReader = terminal.lineReader();
lineReader.flags(LineReader.FLAG_???);
</code-block>

| Flag (byte)                  | Description                                                                                  |
|------------------------------|----------------------------------------------------------------------------------------------|
| (0x01) FLAG_INSERT_MODE      | All characters to the right of the text cursor are moved to the right when they are entered. |
| (0x02) FLAG_ECHO_MODE        | When entering the line value, all characters are displayed.                                  |
| (0x04) FLAG_QUICK_DELETE     | Pressing the Backspace key deletes the entire line.                                          |
| (0x08) FLAG_KEEP_LINE        | When the Enter key is pressed, the line entered remains visible.                             |
| (0x10) FLAG_NAVIGABLE_CURSOR | The text cursor can be moved freely within the line using the arrow keys.                    |

Default flags: `FLAG_INSERT_MODE`,`FLAG_ECHO_MODE`,`FLAG_KEEP_LINE`,`FLAG_NAVIGABLE_CURSOR`

## Input History

The input history saves all lines and these can be called up using the up/down arrow keys.
to call them up.

<code-block lang="Java">
LineReader lineReader = terminal.lineReader();
lineReader.inputHistory(InputHistory.create(true));
</code-block>

## TabCompleter

The TabCompleter specifies strings at certain positions. 
These suggestions can be called up using the tabulator key.

<code-block lang="Java">
RawTabCompleter tabCompleter = new RawTabCompleter(0);
tabCompleter.addSuggestions("Test", "house", "console");

LineReader lineReader = terminal.lineReader();
lineReader.tabCompleter(tabCompleter);
</code-block>

## Read lines

There are several ways to read lines:

<code-block lang="java">
LineReader lineReader = terminal.lineReader();
String value = lineReader.readLine();
</code-block>

<code-block lang="java">
terminal.eventBus().subscribe(LineReleaseEvent.class, event -> {
  terminal.writeLine("Line released: " + event.line());
});
</code-block>

<code-block lang="java">
terminal.readLine(prefix...)
terminal.readPassword(prefix...)
</code-block>

<note>
The reading mode <code>CLITerminal#lineReading(boolean)</code> must be enabled
</note>

## Line Renderer
There are ways to change the display of the command line. Prefixes are often used in 
the command line, which are displayed on the left-hand side of the line. To add
your own, the following setting must be made:
<code-block lang="java">
LineReader lineReader = terminal.lineReader();
lineReader.lineRenderer(new PrefixedLineRenderer(() -> TermString.builder()
    .foregroundColor(TerminalColor.RED)
    .append(">> ")
    .resetStyle()
    .build()));
</code-block>