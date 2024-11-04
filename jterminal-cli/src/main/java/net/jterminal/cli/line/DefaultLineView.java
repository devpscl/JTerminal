package net.jterminal.cli.line;

import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

class DefaultLineView implements LineView {

  private final int lines;
  private final TermString view;
  private final int cursor;
  private final TerminalDimension preferredWindowSize;

  public DefaultLineView(int lines, TermString view, int cursor,
      TerminalDimension preferredWindowSize) {
    this.lines = lines;
    this.view = view;
    this.cursor = cursor;
    this.preferredWindowSize = preferredWindowSize;
  }

  @Override
  public int usedLines() {
    return lines;
  }

  @Override
  public @NotNull TermString view() {
    return view;
  }

  @Override
  public int cursor() {
    return cursor;
  }

  @Override
  public @NotNull TerminalPosition cursorPos(@NotNull TerminalPosition origin) {
    int x = cursor % (preferredWindowSize.width());
    int y = cursor / (preferredWindowSize.width());
    return origin.clone().addX(x).addY(y);
  }

  @Override
  public @NotNull TerminalDimension preferredWindowSize() {
    return preferredWindowSize;
  }

  @Override
  public @NotNull LineView convert(@NotNull TerminalDimension windowSize) {
    return LineView.create(view, cursor, windowSize);
  }
}
