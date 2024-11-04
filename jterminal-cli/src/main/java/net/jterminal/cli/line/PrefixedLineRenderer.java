package net.jterminal.cli.line;

import java.util.function.Supplier;
import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TerminalDimension;
import org.jetbrains.annotations.NotNull;

public class PrefixedLineRenderer extends DefaultLineRenderer {

  protected final Supplier<TermString> prefixSupply;

  public PrefixedLineRenderer(@NotNull Supplier<TermString> prefixSupply) {
    this.prefixSupply = prefixSupply;
  }

  @Override
  public @NotNull LineView view(@NotNull Terminal terminal, @NotNull LineReader lineReader) {
    TermString prefixTermString = prefixSupply.get();
    TerminalDimension winSize = terminal.windowSize();
    TermString termString = TermString.builder()
        .append(prefixTermString)
        .append(lineReader.displayingInput())
        .build();
    return LineView.create(termString, lineReader.cursor()
        + prefixTermString.length(), winSize, termString.length());
  }

  @Override
  public @NotNull TermString legacyView(@NotNull Terminal terminal,
      @NotNull LineReader lineReader) {
    TermString prefixTermString = prefixSupply.get();
    return TermString.builder()
        .append(prefixTermString)
        .append(lineReader.displayingInput())
        .build();
  }

}
