package net.jterminal.ui.component.selectable;

import java.util.Collection;
import net.jterminal.input.Keyboard;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.component.ComponentGroup;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.special.RadioButtonChangeEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class RadioButtonComponent extends SelectableComponent {

  private String text;
  private boolean checked = false;

  public RadioButtonComponent() {
    this("");
  }

  public RadioButtonComponent(@NotNull String text) {
    text(text);
  }

  public void text(@NotNull String text) {
    this.text = text;
    setWidth(preferredWidth());
    setHeight(1);
    repaint();
  }

  public int preferredWidth() {
    return text.length() + 4;
  }

  public @NotNull String text() {
    return text;
  }

  public void checked(boolean checked) {
    this.checked = checked;
    eventBus.post(new RadioButtonChangeEvent(this, checked));
    if(checked) {
      for (ComponentGroup memberGroup : memberGroups()) {
        uncheckGroup(memberGroup);
      }
    }
    repaint();
  }

  private void uncheckGroup(@NotNull ComponentGroup group) {
    Collection<RadioButtonComponent> components = group.components(RadioButtonComponent.class);
    for (RadioButtonComponent component : components) {
      if(component == this) {
        continue;
      }
      component.checked(false);
    }
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
    String checkboxString = checked ? "(X)" : "( )";
    graphics.drawString(1, 1, checkboxString);
    graphics.drawString(5, 1, termString);
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    if(!isSelected()) {
      return;
    }
    if(event.key() == Keyboard.KEY_ENTER) {
      event.intercept(true);
      checked(true);
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    if(event.action() != Action.RELEASE || event.button() != Button.LEFT) {
      return;
    }
    TermPos position = event.position();
    if(position.x() >= 1 && position.x() <= 3) {
      checked(true);
    }
  }


}
