package net.jterminal.test;

import net.jterminal.Terminal;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.test.example.ExampleApp;
import net.jterminal.ui.UITerminal;

public class Main {

  public static void main(String[] args)
      throws TerminalInitializeException, TerminalProviderException {
    UITerminal terminal = UITerminal.create();
    Terminal.set(terminal);
    ExampleApp exampleApp = new ExampleApp(terminal);
    exampleApp.init();
    exampleApp.openLoginScreen();
  }


}
