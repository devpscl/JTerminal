package net.jterminal.cli.line;

import java.util.function.Supplier;
import net.jterminal.cli.CLITerminal;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class PrefixedLineRenderer extends DefaultLineRenderer {

  protected final Supplier<TermString> prefixSupply;

  public PrefixedLineRenderer(@NotNull Supplier<TermString> prefixSupply) {
    this.prefixSupply = prefixSupply;
  }

  @Override
  public @NotNull LineView view(@NotNull CLITerminal terminal, @NotNull LineReader lineReader) {
    TermString prefixTermString = prefixSupply.get();
    TermDim winSize = terminal.windowSize();
    TermString termString = TermString.value(lineReader.displayingInput());
    termString = terminal.modifyCommandLineView(termString, lineReader.displayingInput());

    TermString viewString = TermString.builder()
        .append(prefixTermString)
        .append(termString)
        .build();

    return LineView.create(viewString, lineReader.cursor()
        + prefixTermString.length(), winSize, viewString.length());
  }

  @Override
  public @NotNull TermString legacyView(@NotNull CLITerminal terminal,
      @NotNull LineReader lineReader) {
    TermString prefixTermString = prefixSupply.get();
    return TermString.builder()
        .append(prefixTermString)
        .append(lineReader.displayingInput())
        .build();
  }

}
