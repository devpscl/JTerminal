package net.jterminal.cli.line;

import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

class DefaultLineView implements LineView {

  private final int lines;
  private final TermString view;
  private final int cursor;
  private final TermDim preferredWindowSize;
  private final int length;

  public DefaultLineView(int lines, TermString view, int cursor,
      TermDim preferredWindowSize, int length) {
    this.lines = lines;
    this.view = view;
    this.cursor = cursor;
    this.preferredWindowSize = preferredWindowSize;
    this.length = length;
  }

  @Override
  public int length() {
    return length;
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
  public @NotNull TermPos cursorPos(@NotNull TermPos origin) {
    int x = cursor % (preferredWindowSize.width());
    int y = cursor / (preferredWindowSize.width());
    return origin.copy().addX(x).addY(y);
  }

  @Override
  public @NotNull TermDim preferredWindowSize() {
    return preferredWindowSize;
  }

  @Override
  public @NotNull LineView convert(@NotNull TermDim windowSize) {
    return LineView.create(view, cursor, windowSize, length);
  }
}
