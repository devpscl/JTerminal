package net.jterminal.ui.component.selectable;

import net.jterminal.input.Keyboard;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.style.TextFont;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.special.ButtonClickedEvent;
import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ButtonComponent extends SelectableComponent {

  protected String text;
  protected Runnable action;

  public ButtonComponent() {
    this("Button");
  }

  public ButtonComponent(@NotNull String text) {
    text(text);
  }

  public int preferredWidth() {
    return text.length() + 2;
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
    synchronized (lock) {
      this.text = text;
      setWidth(text.length() + 2);
      setHeight(1);
    }
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    if(isSelected()) {
      graphics.fonts(TextFont.REVERSED);
    }
    graphics.drawString(1, 1, "[" + text+ "]");
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    if(!isSelected()) {
      return;
    }
    if(event.key() == Keyboard.KEY_ENTER && !event.isCancelledAction()) {
      if(action != null) {
        action.run();
      }
      eventBus().post(new ButtonClickedEvent(this));
      event.intercept(true);
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    if(!isSelected()) {
      return;
    }
    if(event.action() == Action.RELEASE && event.button() == Button.LEFT
        && !event.isCancelledAction()) {
      if(action != null) {
        action.run();
      }
      eventBus().post(new ButtonClickedEvent(this));
    }
  }

}
