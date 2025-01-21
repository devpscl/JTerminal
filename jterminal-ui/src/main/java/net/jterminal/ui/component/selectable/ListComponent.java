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
import net.jterminal.ui.component.scrollbar.VirtualScrollBar;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.util.Axis;
import net.jterminal.ui.util.ViewShifter;
import net.jterminal.ui.util.ViewShifter.Type;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListComponent extends SelectableComponent implements Resizeable {

  private final ViewShifter viewShifter = new ViewShifter(Type.INDEX_SHIFTER);
  private List<String> elements = new ArrayList<>();
  private TextStyle cursorStyle = TextStyle.create(
      null, null, TextFont.REVERSED);
  private TextStyle selectedStyle = TextStyle.create(
      null, null, TextFont.UNDERLINE);
  private VirtualScrollBar scrollBar;
  private int selectedIndex = -1;

  public void selectedStyle(@NotNull TextStyle selectedStyle) {
    this.selectedStyle = selectedStyle;
  }

  public void cursorStyle(@NotNull TextStyle cursorStyle) {
    this.cursorStyle = cursorStyle;
  }

  public @NotNull TextStyle selectedStyle() {
    return selectedStyle.copy();
  }

  public @NotNull TextStyle cursorStyle() {
    return cursorStyle.copy();
  }

  public @NotNull List<String> elements() {
    return new ArrayList<>(elements);
  }

  public @Nullable VirtualScrollBar scrollBar() {
    return scrollBar;
  }

  public int selected() {
    return selectedIndex;
  }

  public void select(int selected) {
    if(elements.isEmpty()) {
      this.selectedIndex = -1;
      repaint();
      return;
    }
    this.selectedIndex = Math.max(-1, Math.min(selected, elements.size()));
    repaint();
  }

  public @NotNull VirtualScrollBar attachScrollBar() {
    this.scrollBar = new VirtualScrollBar(Axis.VERTICAL);
    updateScrollBar();
    repaint();
    return this.scrollBar;
  }

  public void detachScrollBar() {
    this.scrollBar = null;
    repaint();
  }

  public void updateScrollBar() {
    if(scrollBar == null) {
      return;
    }
    scrollBar.setup(viewShifter, false);
    scrollBar.endShrinkLevel(viewShifter.viewSize() / 4);
  }

  public void elements(List<String> elements) {
    this.elements = new ArrayList<>(elements);
    viewShifter.cursor(0);
    viewShifter.bufferSize(elements.size());
    selectedIndex = -1;
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    updateScrollBar();
    final TextStyle initStyle = graphics.style();
    final TextStyle cursorStyle = Combiner.combine(this.cursorStyle, initStyle);
    int len = currentDimension().height();
    viewShifter.viewSize(len);
    int start = viewShifter.bufferStart();
    int end = viewShifter.bufferEnd();
    int counter = 1;
    boolean selected = isSelected();
    for(int x = start; x < end && x < elements.size(); x++) {
      if(viewShifter.cursor() == x && selected) {
        graphics.style(cursorStyle);
      }
      if(x == selectedIndex) {
        TextStyle selectedStyle = Combiner.combine(this.selectedStyle, graphics.style());
        graphics.style(selectedStyle);
      }
      graphics.drawString(1, counter++, elements.get(x));
      graphics.style(initStyle);
    }
    if(scrollBar != null) {
      scrollBar.size(len);
      TermGraphics innerGraphics = TermGraphics.create(1, len);
      innerGraphics.style(initStyle);
      scrollBar.draw(innerGraphics);
      graphics.draw(currentDimension().width(), 1, innerGraphics);
    }
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    if(!isSelected()) {
      return;
    }
    int key = event.key();
    if(key == Keyboard.KEY_ARROW_UP) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      viewShifter.backward(1);
      updateScrollBar();
      event.intercept(true);
      repaint();
      return;
    }
    if(key == Keyboard.KEY_ARROW_DOWN) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      viewShifter.forward(1);
      updateScrollBar();
      event.intercept(true);
      repaint();
    }
    if(key == Keyboard.KEY_ENTER) {
      int current = viewShifter.cursor();
      if(selectedIndex == current) {
        select(-1);
        return;
      }
      select(current);
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    super.processMouseEvent(event);

    TermPos position = event.position();
    if(event.action() == Action.PRESS && event.button() == Button.LEFT) {
      int bufIdx = viewShifter.viewIndexToBufferIndex(position.y() - 1);
      if(viewShifter.cursor() == bufIdx && isSelected()) {
        if(selectedIndex == bufIdx) {
          select(-1);
          return;
        }
        select(bufIdx);
        return;
      }
      viewShifter.cursor(bufIdx);
      repaint();
    }
    if(event.action() == Action.WHEEL_UP) {
      viewShifter.backward(1);
      repaint();
      return;
    }
    if(event.action() == Action.WHEEL_DOWN) {
      viewShifter.forward(1);
      repaint();
    }
  }
}