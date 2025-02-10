package net.jterminal.test;

import java.util.Set;
import net.jterminal.Terminal;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.command.DefaultCommandParser;
import net.jterminal.cli.commodore.CommandState;
import net.jterminal.cli.commodore.CommodoreFunctional.StyleEditor;
import net.jterminal.cli.commodore.CommodoreHandler;
import net.jterminal.cli.commodore.CommodoreManager;
import net.jterminal.cli.exception.CommandBuildException;
import net.jterminal.test.user.User;
import net.jterminal.test.user.UserManager;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;

public class ExampleCommandHandler extends CommodoreHandler<CommandArgument> {

  private final Terminal terminal;
  private final ExampleApplication app;

  public ExampleCommandHandler(Terminal terminal, ExampleApplication exampleApplication) {
    super(new DefaultCommandParser());
    this.terminal = terminal;
    this.app = exampleApplication;
  }

  @Override
  protected void build(@NotNull CommodoreManager<CommandArgument> manager)
      throws CommandBuildException {
    manager.addCommand("user")
        .add(value("name")
            .styleEditor(userStyle(false))
            .suggestionResolver(this::resolveUsernameSuggestions)
            .add(literal("passedit")
                .add(value("pass")
                        .execute(this::handlePassEditExecute)
                        .styleEditor(passwordStyle())
                    )
            )
            .add(literal("addperm")
                .add(value("perm")
                    .styleEditor(highlightStyle(TerminalColor.GREEN))
                    .execute(this::handleUserPermissionAddExecute)
                )
            )
            .add(literal("remperm")
                .add(value("perm")
                    .styleEditor(highlightStyle(TerminalColor.GREEN))
                    .execute(this::handleUserPermissionRemExecute)
                )
            )
            .add(literal("seclevel")
                .add(value("level")
                    .styleEditor(numberInvalidStyle())
                    .execute(this::handleUserSecurityLevel)
                )
            )
            .add(literal("delete")
                .execute(this::handleUserDelete)
            )
    );
    manager.addCommand("logout")
        .execute(state -> {
            new Thread(app::startLogin)
                .start();
        });
    manager.addCommand("useradd")
        .add(value("name")
            .styleEditor(userStyle(true))
            .execute(this::handleUserAddNoPwd)
            .add(value("pass")
                .styleEditor(passwordStyle())
                .execute(this::handleUserAdd)
            )
        );
  }

  private void handleUserAddNoPwd(@NotNull CommandState<CommandArgument> state) {
    CommandArgument nameArg = state.argumentById("name");
    UserManager userManager = app.userManager();
    User user = userManager.getByName(nameArg.asString());
    if(user != null) {
      terminal.writeLine("User already exists");
      return;
    }
    userManager.add(new User(nameArg.asString(), ""));
    terminal.writeLine("User added");
  }

  private void handleUserAdd(@NotNull CommandState<CommandArgument> state) {
    CommandArgument nameArg = state.argumentById("name");
    CommandArgument passArg = state.argumentById("pass");
    UserManager userManager = app.userManager();
    User user = userManager.getByName(nameArg.asString());
    if(user != null) {
      terminal.writeLine("User already exists");
      return;
    }
    userManager.add(new User(nameArg.asString(), passArg.asString()));
    terminal.writeLine("User added");
  }

  private void handleUserDelete(@NotNull CommandState<CommandArgument> state) {
    CommandArgument nameArg = state.argumentById("name");
    UserManager userManager = app.userManager();
    User user = userManager.getByName(nameArg.asString());
    if(user == null) {
      terminal.writeLine("Invalid user");
      return;
    }
    if(app.currentUser() == user) {
      terminal.writeLine("You can not delete yourself");
      return;
    }
    userManager.remove(user);
    terminal.writeLine("User deleted!");
  }

  private void handleUserSecurityLevel(@NotNull CommandState<CommandArgument> state) {
    CommandArgument nameArg = state.argumentById("name");
    CommandArgument permArg = state.argumentById("level");
    UserManager userManager = app.userManager();
    User user = userManager.getByName(nameArg.asString());
    if(user == null) {
      terminal.writeLine("Invalid user");
      return;
    }
    if(!permArg.isNumber()) {
      terminal.writeLine("Invalid security level");
      return;
    }
    user.securityLevel(permArg.asNumber().intValue());
    terminal.writeLine("User modified");
  }

  private void handleUserPermissionAddExecute(@NotNull CommandState<CommandArgument> state) {
    CommandArgument nameArg = state.argumentById("name");
    CommandArgument permArg = state.argumentById("perm");
    UserManager userManager = app.userManager();
    User user = userManager.getByName(nameArg.asString());
    if(user == null) {
      terminal.writeLine("Invalid user");
      return;
    }
    user.addPermission(permArg.asString());
    terminal.writeLine("User modified");
  }

  private void handleUserPermissionRemExecute(@NotNull CommandState<CommandArgument> state) {
    CommandArgument nameArg = state.argumentById("name");
    CommandArgument permArg = state.argumentById("perm");
    UserManager userManager = app.userManager();
    User user = userManager.getByName(nameArg.asString());
    if(user == null) {
      terminal.writeLine("Invalid user");
      return;
    }
    user.removePermission(permArg.asString());
    terminal.writeLine("User modified");
  }

  private void handlePassEditExecute(@NotNull CommandState<CommandArgument> state) {
    CommandArgument nameArg = state.argumentById("name");
    CommandArgument passArg = state.argumentById("pass");
    UserManager userManager = app.userManager();
    User user = userManager.getByName(nameArg.asString());
    if(user == null) {
      terminal.writeLine("Invalid user");
      return;
    }
    user.password(passArg.asString());
    terminal.writeLine("Password changed");
  }

  private StyleEditor<CommandArgument> userStyle(boolean reverse) {
    return new StyleEditor<>() {
      @Override
      public @NotNull TermString edit(@NotNull TermString input,
          @NotNull CommandState<CommandArgument> state) {
        User user = app.userManager().getByName(input.raw());
        TerminalColor invalidColor = reverse ? TerminalColor.YELLOW : TerminalColor.RED;
        TerminalColor okColor = reverse ? TerminalColor.RED : TerminalColor.YELLOW;
        return TermString.builder()
            .foregroundColor(user == null ? invalidColor : okColor)
            .append(input)
            .build();
      }
    };
  }

  private void resolveUsernameSuggestions(@NotNull Set<String> suggestions,
      @NotNull CommandState<CommandArgument> state) {
    UserManager userManager = app.userManager();
    for (User user : userManager.users()) {
      suggestions.add(user.name());
    }
  }


}
