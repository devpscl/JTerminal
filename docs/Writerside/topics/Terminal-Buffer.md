# Terminal-Buffer

The terminal buffer makes it possible to store strings and ansi codes to 
be passed to the console output at the end.

<code-block lang="Java">
terminal.clear();
TerminalBuffer buffer = new TerminalBuffer()
    .append("Hello")
    .cursorRight(5)
    .foregroundColor(TerminalColor.RED)
    .cursorSave()
    .append("World!")
    .cursorRestore()
    .cursorDown(1)
    .foregroundColor(null)
    .append("World!")
    .cursor(new TermPos(1, 3))
    .appendFonts(TextFont.UNDERLINE)
    .append("Buffered ansi text")
    .removeFonts(TextFont.UNDERLINE)
    .append(" is easier to use");
terminal.writeLine(buffer);
</code-block>

Console output:
<img src="terminalbuffer1.png" alt="console output"/>