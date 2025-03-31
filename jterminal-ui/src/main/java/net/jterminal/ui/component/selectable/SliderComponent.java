package net.jterminal.ui.component.selectable;

import net.jterminal.input.Keyboard;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.style.TextFont;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.special.SliderChangeEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.ui.layout.Layout.Modifier;
import net.jterminal.ui.util.MathUtil;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class SliderComponent extends SelectableComponent implements Resizeable {

  protected float value = 1.0F;

  public SliderComponent() {
    setHeight(1);
  }

  public float value() {
    return value;
  }

  public void value(float value) {
    this.value = MathUtil.range(value, 0F, 1F);
    repaint();
  }

  private int toStep(float f, int width) {
    if(width <= 1 || f == 0.F) {
      return 1;
    }
    int value = (int) ((width - 1) * f) + 1;
    return MathUtil.range(value, 2, width);
  }

  private float fromStep(int step, int width) {
    return step == 1 ? 0 : ((step - 1) / ((float) width - 1));
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    int width = graphics.width();
    int actionWidth = width - 2;
    if(isSelected()) {
      graphics.fonts(TextFont.UNDERLINE);
    }
    graphics.draw(1, 1, '<');
    graphics.draw(width, 1, '>');
    if(actionWidth < 1) {
      return;
    }
    int step = toStep(value, actionWidth);
    for(int x = 1; x <= actionWidth; x++) {
      graphics.draw(x + 1, 1, step == x ? '|' : '=');
    }
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    int width = currentDimension().width();
    int actionWidth = width - 2;
    if(!isSelected()) {
      return;
    }
    if(event.key() == Keyboard.KEY_ARROW_LEFT) {
      event.intercept(true);
      int step = toStep(value, actionWidth);
      if(step > 1) {
        step--;
      }
      float newValue = fromStep(step, actionWidth);
      eventBus.post(new SliderChangeEvent(this, newValue));
      value(newValue);
      return;
    }
    if(event.key() == Keyboard.KEY_ARROW_RIGHT) {
      event.intercept(true);
      int step = toStep(value, actionWidth);
      if(step < actionWidth) {
        step++;
      }
      float newValue = fromStep(step, actionWidth);
      eventBus.post(new SliderChangeEvent(this, newValue));
      value(newValue);
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    super.processMouseEvent(event);
    TermPos position = event.position();
    int width = currentDimension().width();
    int actionWidth = width - 2;
    if(event.button() != Button.LEFT) {
      return;
    }
    if(event.action() == Action.PRESS || event.action() == Action.MOVE) {
      int step = position.x() - 1;
      if(step > actionWidth || step < 1) {
        return;
      }
      float newValue = fromStep(step, actionWidth);
      eventBus.post(new SliderChangeEvent(this, newValue));
      value(newValue);
    }
  }

  @Deprecated
  @Override
  public void height(int value, Modifier... modifiers) {
    throw new IllegalStateException("Height change is not supported!");
  }

  @Deprecated
  @Override
  public void height(DimensionValue positionValue, Modifier... modifiers) {
    throw new IllegalStateException("Height change is not supported!");
  }
}
