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

  public PrefixedLineRenderer(@NotNull TermString termString) {
    this.prefixSupply = () -> termString;
  }

  public PrefixedLineRenderer(@NotNull String value) {
    this.prefixSupply = () -> TermString.value(value);
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
    int cursor = (lineReader.flags() & LineReader.FLAG_ECHO_MODE) != 0
        ? lineReader.cursor() : 0;

    return LineView.create(viewString, cursor
        + prefixTermString.length(), winSize, viewString.length());
  }

}
