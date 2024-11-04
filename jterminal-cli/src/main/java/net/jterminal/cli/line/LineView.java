package net.jterminal.cli.line;

import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public interface LineView {

  int usedLines();

  @NotNull TermString view();

  int cursor();

  int length();

  @NotNull TerminalPosition cursorPos(@NotNull TerminalPosition origin);

  @NotNull TerminalDimension preferredWindowSize();

  @NotNull LineView convert(@NotNull TerminalDimension windowSize);

  static @NotNull LineView create(@NotNull TermString termString, int consoleCursor,
      @NotNull TerminalDimension windowSize, int length) {
    int width = windowSize.width();
    int lines = Math.max(0, (length - 1) / width + 1);
    return new DefaultLineView(lines, termString, consoleCursor, windowSize, length);
  }

}
