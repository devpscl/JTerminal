package net.jterminal.test.example;

import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.TermScreen;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.LabelComponent;
import net.jterminal.ui.component.selectable.ButtonComponent;
import net.jterminal.ui.component.selectable.PasswordFieldComponent;
import net.jterminal.ui.component.selectable.TextFieldComponent;
import net.jterminal.ui.dialog.ResultDialog;
import net.jterminal.ui.dialog.ResultDialog.OptionType;
import net.jterminal.ui.dialog.ResultDialogBuilder;
import net.jterminal.ui.event.special.ButtonClickedEvent;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.util.BoxCharacter.Type;
import net.jterminal.ui.util.TextAlignment;
import org.jetbrains.annotations.NotNull;

public class LoginScreen extends TermScreen {

  private FrameContainer frameContainer;
  private LabelComponent nameFieldLabel;
  private TextFieldComponent textField;
  private LabelComponent passwordFieldLabel;
  private PasswordFieldComponent passwordField;
  private ButtonComponent loginButton;

  private FrameContainer infoFrameContainer;
  private LabelComponent infoLabel;

  private ExampleApp instance;

  public LoginScreen(@NotNull ExampleApp exampleApp) {
    this.instance = exampleApp;
  }

  public void create() {
    setMouseInputEnabled(true);

    frameContainer = new FrameContainer("Login", TextAlignment.LEFT, 2);
    frameContainer.boxLineType(Type.DOUBLE);
    frameContainer.width(Layout.fill());
    frameContainer.height(Layout.fill());

    nameFieldLabel = new LabelComponent(TermString.value("Name:"));
    nameFieldLabel.x(3);
    nameFieldLabel.y(3);

    textField = new TextFieldComponent();
    textField.width(25);
    textField.x(Layout.relative(nameFieldLabel, Anchor.LEFT));
    textField.y(Layout.dock(nameFieldLabel, Anchor.BOTTOM));

    passwordFieldLabel = new LabelComponent(TermString.value("Passwort:"));
    passwordFieldLabel.x(Layout.relative(textField, Anchor.LEFT));
    passwordFieldLabel.y(Layout.dock(textField, Anchor.BOTTOM), Layout.offset(4));

    passwordField = new PasswordFieldComponent();
    passwordField.width(Layout.relativeTo(textField, Anchor.RIGHT));
    passwordField.x(Layout.relative(passwordFieldLabel, Anchor.LEFT));
    passwordField.y(Layout.dock(passwordFieldLabel, Anchor.BOTTOM));

    loginButton = new ButtonComponent("Login");
    loginButton.x(Layout.relative(passwordField, Anchor.LEFT));
    loginButton.y(Layout.relative(passwordField, Anchor.BOTTOM), Layout.offset(3));
    loginButton.eventBus().subscribe(ButtonClickedEvent.class, this::handleLoginButton);

    infoFrameContainer = new FrameContainer(" Info ", TextAlignment.CENTER);
    infoLabel = new LabelComponent(infoText());
    infoLabel.autoWrapLines(true);
    infoLabel.width(Layout.fill());
    infoLabel.height(Layout.fill());
    infoFrameContainer.add(infoLabel);
    infoFrameContainer.x(Layout.relative(Anchor.RIGHT), Layout.offset(-40));
    infoFrameContainer.y(1);
    infoFrameContainer.width(Layout.fill());
    infoFrameContainer.height(Layout.fill());

    frameContainer.add(nameFieldLabel);
    frameContainer.add(textField);
    frameContainer.add(passwordFieldLabel);
    frameContainer.add(passwordField);
    frameContainer.add(loginButton);
    frameContainer.add(infoFrameContainer);

    add(frameContainer);
  }

  private void handleLoginButton(@NotNull ButtonClickedEvent event) {
    String name = textField.value();
    String password = passwordField.value();
    if(!name.equals("console") || !password.equals("java")) {
      ResultDialog dialog = new ResultDialogBuilder()
          .title("Login error")
          .message(TermString.builder()
              .foregroundColor(TerminalColor.RED)
              .append("Invalid name or password. Try it again :)")
              .build()
          )
          .optionType(OptionType.OK_TYPE)
          .build();
      dialog.openDialog();
      return;
    }
    instance.openHomeScreen();
  }

  private static @NotNull TermString infoText() {
    return TermString.builder()
        .appendFormatted("""
            $This is a login screen.$
            
            
            
            
            
            
            For testing:
            Name: console
            Passwort: java
            """, '$',
            TextStyle.create(TerminalColor.RED, null),
            TextStyle.getDefault()
        )
        .build();
  }


}
