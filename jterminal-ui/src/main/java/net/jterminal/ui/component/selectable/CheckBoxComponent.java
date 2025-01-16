package net.jterminal.ui.component.selectable;

import net.jterminal.input.Keyboard;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.component.SelectableComponentKeyEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class CheckBoxComponent extends SelectableComponent implements Resizeable {

  private String text;
  private boolean checked = false;

  public CheckBoxComponent() {
    this("");
  }

  public CheckBoxComponent(String text) {
    this.text = text;
  }

  public void text(@NotNull String text) {
    this.text = text;
    repaint();
  }

  public @NotNull String text() {
    return text;
  }

  public void checked(boolean checked) {
    this.checked = checked;
    repaint();
  }

  public boolean checked() {
    return checked;
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    int width = currentDimension().width();
    if(width < 3) {
      return;
    }
    TermString termString;
    if(isSelected()) {
      termString = TermString.builder()
          .fonts(TextFont.UNDERLINE)
          .append(text)
          .build();
    } else {
      termString = TermString.value(text);
    }
    String checkboxString = checked ? "[X]" : "[ ]";
    graphics.drawString(1, 1, checkboxString);
    graphics.drawString(5, 1, termString);
  }

  @Override
  public void processKeyEvent(@NotNull SelectableComponentKeyEvent event) {
    if(event.key() == Keyboard.KEY_ENTER) {
      event.interceptInput(true);
      checked(!checked);
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    if(event.action() != Action.RELEASE || event.button() != Button.LEFT) {
      return;
    }
    TermPos position = event.position();
    if(position.x() >= 1 && position.x() <= 3) {
      checked(!checked);
    }
  }
}
