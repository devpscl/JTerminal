package net.jterminal.cli.line;

import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public interface LineView {

  int usedLines();

  @NotNull TermString view();

  int cursor();

  int length();

  @NotNull TermPos cursorPos(@NotNull TermPos origin);

  @NotNull TermDim preferredWindowSize();

  @NotNull LineView convert(@NotNull TermDim windowSize);

  static @NotNull LineView create(@NotNull TermString termString, int consoleCursor,
      @NotNull TermDim windowSize, int length) {
    int width = windowSize.width();
    int lines = Math.max(0, (length - 1) / width + 1);
    return new DefaultLineView(lines, termString, consoleCursor, windowSize, length);
  }

}
