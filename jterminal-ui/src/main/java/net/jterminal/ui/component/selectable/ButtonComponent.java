package net.jterminal.ui.component.selectable;

import net.jterminal.input.Keyboard;
import net.jterminal.input.KeyboardInputEvent;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TerminalDimension;
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
    size = new TerminalDimension(text.length() + 2, 1);
  }

  @Override
  public void size(@NotNull TerminalDimension size) {
    throw new UnsupportedOperationException();
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
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    if(!isSelected()) {
      return;
    }
    if(event.key() == Keyboard.KEY_ENTER && action != null) {
      action.run();
    }
  }

}
