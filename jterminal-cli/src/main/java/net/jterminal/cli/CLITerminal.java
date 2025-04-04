package net.jterminal.cli;

import java.io.IOException;
import net.jterminal.NativeTerminal;
import net.jterminal.Terminal;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.command.CommandHandler;
import net.jterminal.cli.exception.CommandParseException;
import net.jterminal.cli.line.LineReader;
import net.jterminal.cli.util.RecordingBuffer;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CLITerminal extends NativeTerminal {

  @Nullable CommandHandler<?> commandHandler();

  <T extends CommandArgument> CommandHandler<T> commandHandler(Class<T> type);

  void commandHandler(@Nullable CommandHandler<?> commandHandler);

  void unsetCommandHandler();

  @NotNull CommandArgument[] parseArguments(@NotNull String input) throws CommandParseException;

  @NotNull TermString modifyCommandLineView(@NotNull TermString termString, @NotNull String input);

  boolean dispatchCommand(@NotNull String line);

  @NotNull LineReader lineReader();

  void lineReader(@NotNull LineReader lineReader);

  void lineReading(boolean state);

  boolean lineReading();

  void setOutputRecordingBuffer(@Nullable RecordingBuffer recordingBuffer);

  @Nullable RecordingBuffer outputRecordingBuffer();

  boolean restoreRecordedOutput();

  void clearRecordedInput();

  void updateLine();

  void eventReleaseLine(@NotNull String line);

  @Override
  void update();

  @NotNull String readLine(@NotNull String prefix) throws IOException;

  @NotNull String readPassword(@NotNull String prefix) throws IOException;

  @NotNull String readLine(@NotNull String prefix, int flags) throws IOException;

  static @NotNull CLITerminal create() throws TerminalInitializeException {
    return Terminal.create(CLITerminalProvider.class);
  }

}
