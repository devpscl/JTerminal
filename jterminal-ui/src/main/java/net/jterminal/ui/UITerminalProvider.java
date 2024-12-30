package net.jterminal.ui;

import net.jterminal.exception.TerminalProviderException;

public class UITerminalProvider extends AbstractUITerminal<UITerminal> {

  public UITerminalProvider() {
    super(UITerminal.class);
  }


  @Override
  public void initialize() throws TerminalProviderException {
    super.initialize();
    inputEventListenerEnabled(true);
    flags(FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT);

  }
}
