package net.jterminal.test.example;

import net.jterminal.ui.UITerminal;
import org.jetbrains.annotations.NotNull;

public class ExampleApp {

  private final LoginScreen loginScreen = new LoginScreen(this);
  private final HomeScreen homeScreen = new HomeScreen(this);

  private UITerminal terminal;

  public ExampleApp(@NotNull UITerminal terminal) {
    this.terminal = terminal;
  }

  public void init() {
    loginScreen.create();
    homeScreen.create();
  }

  public void openLoginScreen() {
    terminal.openScreen(loginScreen);
  }

  public void openHomeScreen() {
    terminal.openScreen(homeScreen);
  }

}
