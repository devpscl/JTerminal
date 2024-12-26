package net.jterminal.cli.command;

import java.util.ArrayList;
import java.util.List;
import net.jterminal.cli.exception.CommandParseException;
import net.jterminal.cli.util.StringReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultCommandParser implements CommandParser<CommandArgument> {

  @Override
  public CommandArgument[] parse(@NotNull String line) throws CommandParseException {
    StringReader stringReader = new StringReader(line);
    List<CommandArgument> arguments = new ArrayList<>();
    String str;
    int start;
    int end;
    while(true) {
      stringReader.skipAllWhitespaces();
      if(!stringReader.avail()) {
        break;
      }
      start = stringReader.position();
      str = parseSymbol(stringReader);
      end = stringReader.position();
      if(str.isBlank()) {
        break;
      }
      CommandArgument arg = parseArgument(str, start, end);
      arguments.add(arg);
    }
    return arguments.toArray(new CommandArgument[0]);
  }

  private @NotNull String parseSymbol(@NotNull StringReader stringReader) {
    StringBuilder stringBuilder = new StringBuilder();
    while (!stringReader.isNextWhitespace() && stringReader.avail()) {
      if(stringReader.isNextQuoted()) {
        String quoted = stringReader.readQuoted(stringReader.peek());
        stringBuilder.append(quoted);
        continue;
      }
      int ch = stringReader.read();
      stringBuilder.append((char) ch);
    }
    return stringBuilder.toString();
  }

  @Override
  public CommandArgument parseArgument(@NotNull String x, int start, int end) {
    Object obj = parseObject(x);
    return new CommandArgument(x, obj == null ? x : obj, start, end);
  }

  private @Nullable Object parseObject(@NotNull String str) {
    StringReader stringReader = new StringReader(str);
    if(!stringReader.isNextDigit()) {
      return null;
    }
    try {
      return Long.parseLong(str);
    } catch (NumberFormatException ignored) {}
    try {
      return Double.parseDouble(str);
    } catch (NumberFormatException ignored) {}
    return null;
  }
}
