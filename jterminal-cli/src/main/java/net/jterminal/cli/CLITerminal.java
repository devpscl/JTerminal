package net.jterminal.cli;

import net.jterminal.NativeTerminal;
import net.jterminal.cli.line.LineReader;
import net.jterminal.cli.util.RecordingBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CLITerminal extends NativeTerminal {

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

}
