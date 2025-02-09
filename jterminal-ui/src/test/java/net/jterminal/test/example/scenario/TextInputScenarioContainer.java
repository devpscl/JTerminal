package net.jterminal.test.example.scenario;

import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.LabelComponent;
import net.jterminal.ui.component.selectable.PasswordFieldComponent;
import net.jterminal.ui.component.selectable.TextAreaComponent;
import net.jterminal.ui.component.selectable.TextFieldComponent;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;
import org.jetbrains.annotations.NotNull;

public class TextInputScenarioContainer extends FrameContainer {

  public void create() {
    LabelComponent textInputLabel = new LabelComponent(TermString.value("Text Input"));
    textInputLabel.x(3);
    textInputLabel.y(2);

    TextFieldComponent textField = new TextFieldComponent("Text here...");
    textField.x(Layout.relative(textInputLabel, Anchor.LEFT));
    textField.y(Layout.dock(textInputLabel, Anchor.BOTTOM));
    textField.width(35);

    LabelComponent passInputLabel = new LabelComponent(TermString.value("Password Input"));
    passInputLabel.x(Layout.relative(textField, Anchor.LEFT));
    passInputLabel.y(Layout.dock(textField, Anchor.BOTTOM), Layout.offset(2));

    PasswordFieldComponent passwordField = new PasswordFieldComponent();
    passwordField.value("1234");
    passwordField.x(Layout.relative(passInputLabel, Anchor.LEFT));
    passwordField.y(Layout.dock(passInputLabel, Anchor.BOTTOM));
    passwordField.width(Layout.relativeTo(textField, Anchor.RIGHT));

    LabelComponent linesInputLabel = new LabelComponent(
        TermString.value("Multiple line input"));
    linesInputLabel.x(Layout.relative(passwordField, Anchor.LEFT));
    linesInputLabel.y(Layout.dock(passwordField, Anchor.BOTTOM), Layout.offset(2));

    TextAreaComponent textArea = new TextAreaComponent();
    textArea.x(Layout.relative(linesInputLabel, Anchor.LEFT));
    textArea.y(Layout.dock(linesInputLabel, Anchor.BOTTOM));
    textArea.width(Layout.relativeTo(passwordField, Anchor.RIGHT));
    textArea.height(5);
    textArea.text(createTextAreaContent());

    add(textInputLabel);
    add(textField);
    add(passInputLabel);
    add(passwordField);
    add(linesInputLabel);
    add(textArea);
  }

  private static @NotNull TermString createTextAreaContent() {
    return TermString.builder()
        .appendFormatted("""
            This is a $textarea$ with
            $formatted content$
            """, '$',
            TextStyle.create(TerminalColor.RED, null),
            TextStyle.getDefault(),
            TextStyle.create(null, null, TextFont.UNDERLINE),
            TextStyle.getDefault()
            )
        .build();
  }

}
