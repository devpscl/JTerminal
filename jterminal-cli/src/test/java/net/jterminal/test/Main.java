package net.jterminal.test;

import net.jterminal.Terminal;
import net.jterminal.cli.CLITerminal;
import net.jterminal.cli.CLITerminalProvider;
import net.jterminal.cli.history.InputHistory;
import net.jterminal.cli.line.LineReader;
import net.jterminal.cli.util.RecordingBuffer;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.exception.TerminalProviderException;

public class Main {

  public static void main(String[] args)
      throws TerminalInitializeException, TerminalProviderException {
    CLITerminal terminal = Terminal.create(CLITerminalProvider.class);
    Terminal.set(terminal);
    terminal.lineReading(true);
    LineReader lineReader = terminal.lineReader();
    lineReader.addFlags(LineReader.FLAG_PRINT_LEGACY);
    lineReader.removeFlags(LineReader.FLAG_INSERT_MODE);
    lineReader.inputHistory(InputHistory.create(false));
    terminal.setOutputRecordingBuffer(new RecordingBuffer(3800, 4096));
    terminal.lineReader().inputHistory(InputHistory.create(true));
    Thread t = new Thread(() -> {
      for (int i = 0; i < 99; i++) {
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        terminal.writeLine("Test: " + i);
        if(i == 6) {
          terminal.clear();
        }
      }
    });
    t.start();
  }

}
