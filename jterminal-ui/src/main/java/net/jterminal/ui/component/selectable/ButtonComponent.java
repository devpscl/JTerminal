package net.jterminal.ui.component.selectable;

import net.jterminal.input.Keyboard;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.component.SelectableComponentKeyEvent;
import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ButtonComponent extends SelectableComponent {

  private String text;
  private Runnable action;

  public ButtonComponent() {
    this("Button");
  }

  public ButtonComponent(@NotNull String text) {
    text(text);
  }

  public @NotNull String text() {
    return text;
  }

  public @Nullable Runnable action() {
    return action;
  }

  public void action(@Nullable Runnable action) {
    this.action = action;
  }

  public void text(@NotNull String text) {
    this.text = text;
    setWidth(text.length() + 2);
    setHeight(1);
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    if(isSelected()) {
      TerminalColor foregroundColor = TerminalColor.from(graphics.foregroundColor());
      TerminalColor backgroundColor = TerminalColor.from(graphics.backgroundColor());
      graphics.foregroundColor(backgroundColor);
      graphics.backgroundColor(foregroundColor);
    }
    graphics.drawString(1, 1, "[" + text+ "]");
  }

  @Override
  public void processKeyEvent(@NotNull SelectableComponentKeyEvent event) {
    if(!isSelected()) {
      return;
    }
    if(event.key() == Keyboard.KEY_ENTER && !event.cancelledAction()) {
      if(action != null) {
        action.run();
      }
      event.interceptInput(true);
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    if(!isSelected()) {
      return;
    }
    if(event.action() == Action.RELEASE && event.button() == Button.LEFT) {
      if(action != null && !event.cancelledAction()) {
        action.run();
      }
    }
  }

}
