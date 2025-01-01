package net.jterminal.ui;

import net.jterminal.Terminal;
import net.jterminal.annotation.SubscribeEvent;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.input.Keyboard;
import net.jterminal.input.KeyboardInputEvent;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.input.MouseInputEvent;
import net.jterminal.input.WindowInputEvent;
import net.jterminal.instance.AbstractNativeTerminal;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.selectable.Selectable;
import net.jterminal.ui.event.ScreenCloseEvent;
import net.jterminal.ui.event.ScreenOpenEvent;
import net.jterminal.ui.event.ScreenRenderedEvent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.renderer.FastScreenRenderer;
import net.jterminal.ui.renderer.ScreenRenderer;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractUITerminal<T extends Terminal> extends AbstractNativeTerminal<T>
    implements UITerminal {

  TermScreen activeScreen = null;
  private final ScreenRenderer screenRenderer;
  private final Object lock = new Object();
  private long lastRenderTime = 0L;

  public AbstractUITerminal(Class<T> interfaceType) {
    super(interfaceType);
    screenRenderer = new FastScreenRenderer(this);
  }

  @Override
  public void initialize() throws TerminalProviderException {
    super.initialize();
    inputEventListenerEnabled(true);
    flags(FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT | FLAG_MOUSE_INPUT);
    eventBus().register(this);
  }

  @Override
  public void openScreen(@NotNull TermScreen screen) {
    if(activeScreen != null) {
      closeScreen(screen);
    }
    synchronized (lock) {
      activeScreen = screen;
      activeScreen.terminal = this;
      activeScreen.repaintFully();
      eventBus.post(new ScreenOpenEvent(screen));
    }
  }

  @Override
  public void closeScreen(@NotNull TermScreen screen) {
    synchronized (lock) {
      activeScreen = null;
      clear();
      eventBus.post(new ScreenCloseEvent(screen));
    }
  }

  @Override
  public @Nullable TermScreen openedScreen() {
    return activeScreen;
  }

  @Override
  public void drawScreen() {
    synchronized (lock) {
      if(activeScreen != null) {
        long start = System.currentTimeMillis();
        ForegroundColor foregroundColor = activeScreen.foregroundColor();
        BackgroundColor backgroundColor = activeScreen.backgroundColor();
        CellData cellData = CellData.empty(foregroundColor, backgroundColor);
        TermGraphics graphics = TermGraphics.create(windowSize(), cellData);
        graphics.foregroundColor(foregroundColor);
        graphics.backgroundColor(backgroundColor);
        activeScreen.paint(graphics);
        screenRenderer.render(graphics.buffer());
        long end = System.currentTimeMillis();
        lastRenderTime = end-start;
        eventBus.post(new ScreenRenderedEvent(activeScreen));
      }
    }
  }

  @Override
  public void drawNewScreen() {
    if(activeScreen == null) {
      return;
    }
    foregroundColor(activeScreen.foregroundColor());
    backgroundColor(activeScreen.backgroundColor());
    clear();
    drawScreen();
  }

  @Override
  public long lastRenderTime() {
    return lastRenderTime;
  }

  @SubscribeEvent
  public void onInputEvent(KeyboardInputEvent e) {
    if(activeScreen == null) {
      return;
    }
    activeScreen.processKeyEvent(new ComponentKeyEvent(e));

    Component component = activeScreen.selectedComponent();
    if(component != null) {
      if(component instanceof Selectable selectable) {
        boolean actionAllowed = selectable.allowActionInput(e);
        if(!actionAllowed) {
          return;
        }
      }
    }
    int key = e.key();
    switch (key) {
      case Keyboard.KEY_TAB:
        activeScreen.selectNext();
        break;
    }
  }

  @SubscribeEvent
  public void onWindowEvent(WindowInputEvent e) {
    drawNewScreen();
  }

  @SubscribeEvent
  public void onMouseEvent(MouseInputEvent e) {
    if(e.action() == Action.PRESS && e.button() == Button.LEFT) {
      TerminalPosition pos = e.terminalPosition();
      activeScreen.performSelect(pos.x(), pos.y());
    }
    activeScreen.processMouseEvent(new ComponentMouseEvent(e));
  }

}
