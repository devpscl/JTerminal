package net.jterminal.ui.component.selectable;

import java.util.ArrayList;
import java.util.List;
import net.jterminal.input.Keyboard;
import net.jterminal.input.Keyboard.State;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.Combiner;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.special.ListItemInteractEvent;
import net.jterminal.ui.event.special.ListItemShowEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.scrollbar.ScrollBar;
import net.jterminal.ui.util.Axis;
import net.jterminal.ui.util.ViewShifter;
import net.jterminal.ui.util.ViewShifter.Type;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListViewComponent extends SelectableComponent implements Resizeable {

  protected final ViewShifter viewShifter = new ViewShifter(Type.INDEX_SHIFTER);
  protected List<String> elements = new ArrayList<>();
  protected TextStyle cursorStyle = TextStyle.create(
      null, null, TextFont.REVERSED);
  private ScrollBar scrollBar;

  public void cursorStyle(@NotNull TextStyle cursorStyle) {
    this.cursorStyle = cursorStyle;
  }

  public @NotNull TextStyle cursorStyle() {
    return cursorStyle.copy();
  }

  public @NotNull List<String> elements() {
    return new ArrayList<>(elements);
  }

  public @Nullable ScrollBar scrollBar() {
    return scrollBar;
  }

  public @NotNull ScrollBar attachScrollBar() {
    synchronized (lock) {
      this.scrollBar = ScrollBar.create(Axis.VERTICAL);
      updateScrollBar();
      repaint();
      return this.scrollBar;
    }
  }

  public void detachScrollBar() {
    synchronized (lock) {
      this.scrollBar = null;
      repaint();
    }
  }

  public void updateScrollBar() {
    if(scrollBar == null) {
      return;
    }
    scrollBar.update(viewShifter, false);
  }

  public void elements(List<String> elements) {
    synchronized (lock) {
      this.elements = new ArrayList<>(elements);
      viewShifter.bufferSize(elements.size());
      cursor(0);
    }
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    updateScrollBar();
    final TextStyle defaultStyle = graphics.style();
    int len = currentDimension().height();
    viewShifter.viewSize(len);
    int start = viewShifter.bufferStart();
    int end = viewShifter.bufferEnd();
    int counter = 1;
    boolean selected = isSelected();
    for(int x = start; x < end && x < elements.size(); x++) {
      TermPos pos = new TermPos(1, counter++);
      String item = elements.get(x);
      paintItem(graphics, pos, x, item, selected);
      graphics.resetStyle();
    }
    if(scrollBar != null) {
      TermGraphics innerGraphics = TermGraphics.create(1, len);
      innerGraphics.style(defaultStyle);
      scrollBar.draw(innerGraphics, len);
      graphics.draw(currentDimension().width(), 1, innerGraphics);
    }
  }

  public void paintItem(@NotNull TermGraphics graphics, @NotNull TermPos pos,
      int index, @NotNull String itemValue, boolean selected) {
    if(viewShifter.cursor() == index && selected) {
      TextStyle cursorStyle = Combiner.combine(this.cursorStyle, graphics.style());
      graphics.style(cursorStyle);
    }
    graphics.drawString(pos, itemValue);
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    if(!isSelected() || event.isCancelledAction()) {
      return;
    }
    int key = event.key();
    if(key == Keyboard.KEY_ARROW_UP) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      event.intercept(true);
      if(viewShifter.cursorAtStart()) {
        return;
      }
      viewShifter.backward(1);
      updateScrollBar();
      performItemShow();
      repaint();
      return;
    }
    if(key == Keyboard.KEY_ARROW_DOWN) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      event.intercept(true);
      if(viewShifter.cursorAtEnd()) {
        return;
      }
      viewShifter.forward(1);
      updateScrollBar();
      performItemShow();
      repaint();
    }
    if(key == Keyboard.KEY_ENTER) {
      int current = viewShifter.cursor();
      eventBus.post(new ListItemInteractEvent(this, current));
    }
  }

  public void cursor(int cursor) {
    viewShifter.cursor(cursor);
    performItemShow();
    repaint();
  }

  public int cursor() {
    return viewShifter.cursor();
  }

  private void performItemShow() {
    eventBus.post(new ListItemShowEvent(this, viewShifter.cursor()));
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    super.processMouseEvent(event);
    TermPos position = event.position();
    if(event.action() == Action.WHEEL_UP) {
      viewShifter.shiftBackward(1);
      performItemShow();
      repaint();
      return;
    }
    if(event.action() == Action.WHEEL_DOWN) {
      viewShifter.shiftForward(1);
      performItemShow();
      repaint();
    }
    if(event.action() == Action.PRESS && event.button() == Button.LEFT) {
      int bufIdx = viewShifter.viewIndexToBufferIndex(position.y() - 1);
      if(isSelected() && viewShifter.cursor() == bufIdx) {
        eventBus.post(new ListItemInteractEvent(this, bufIdx));
      }
      cursor(bufIdx);
    }
  }
}
