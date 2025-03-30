package net.jterminal.test;

import java.io.IOException;
import net.jterminal.Terminal;
import net.jterminal.cli.CLITerminal;
import net.jterminal.cli.history.InputHistory;
import net.jterminal.cli.line.DefaultLineReader;
import net.jterminal.cli.line.LineReader;
import net.jterminal.cli.line.PrefixedLineRenderer;
import net.jterminal.cli.tab.CommandTabCompleter;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.test.user.User;
import net.jterminal.test.user.UserManager;
import net.jterminal.text.XtermColor;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;

public class ExampleApplication {

  private CLITerminal terminal = null;
  private final UserManager userManager = new UserManager();

  private User currentUser = null;

  public void init() {
    try {
      terminal = CLITerminal.create();
      Terminal.set(terminal);
    } catch (TerminalInitializeException | TerminalProviderException e) {
      Terminal.LOGGER.catching(e);
    }
    userManager.add(new User("admin", "1234"));
  }

  public @NotNull UserManager userManager() {
    return userManager;
  }

  public @NotNull User currentUser() {
    return currentUser;
  }

  public void startLogin() {
    terminal.clear();
    terminal.unsetCommandHandler(); //remove when command handling enabled before
    terminal.lineReading(true);
    try {
      String username = terminal.readLine("Username: ");
      String password;
      User user = userManager.getByName(username);
      while (true) {
        if(user != null && user.password().isEmpty()) {
          break;
        }
        password = terminal.readPassword("Password: ");
        if(user == null || !user.password().equals(password)) {
          terminal.writeLine("Login incorrect!");
          continue;
        }
        break;
      }
      currentUser = user;
      startPrompt();
    } catch (IOException e) {
      Terminal.LOGGER.catching(e);
    }
  }

  public void startPrompt() {
    LineReader lineReader = new DefaultLineReader();
    lineReader.inputHistory(InputHistory.create(true));
    lineReader.lineRenderer(new PrefixedLineRenderer(this::prefix));
    lineReader.tabCompleter(new CommandTabCompleter(terminal));
    terminal.lineReader(lineReader);
    terminal.lineReading(true);
    terminal.commandHandler(new ExampleCommandHandler(terminal, this).build());
  }

  private @NotNull TermString prefix() {
    return TermString.builder()
        .foregroundColor(XtermColor.COLOR_78)
        .append("@")
        .append(currentUser.name())
        .resetStyle()
        .append("> ")
        .build();
  }


}
