package net.jterminal.ui;

import java.util.ArrayList;
import java.util.List;
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
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.event.ScreenCloseEvent;
import net.jterminal.ui.event.ScreenOpenEvent;
import net.jterminal.ui.event.ScreenRenderedEvent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.graphics.TerminalState;
import net.jterminal.ui.renderer.FastScreenRenderer;
import net.jterminal.ui.renderer.ScreenRenderer;
import net.jterminal.ui.selector.SelectionResult;
import net.jterminal.util.TermPos;
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
        TerminalState globalState = new TerminalState();
        SelectableComponent selectableComponent = activeScreen.selectedComponent();
        if(selectableComponent != null) {
          TerminalState state = new TerminalState();
          selectableComponent.updateState(state);
          globalState = state.origin(selectableComponent.displayPosition());
        }
        ForegroundColor foregroundColor = activeScreen.foregroundColor();
        BackgroundColor backgroundColor = activeScreen.backgroundColor();
        CellData cellData = CellData.empty(foregroundColor, backgroundColor);
        TermGraphics graphics = TermGraphics.create(windowSize(), cellData);
        graphics.foregroundColor(foregroundColor);
        graphics.backgroundColor(backgroundColor);
        activeScreen.paint(graphics);
        screenRenderer.render(graphics.buffer(), globalState);
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
    ComponentKeyEvent event = new ComponentKeyEvent(e);
    activeScreen.eventBus().post(e);
    if(!event.cancelledAction()) {
      activeScreen.processKeyEvent(event);
    }

    SelectableComponent selectedComponent = activeScreen.selectedComponent();
    if(selectedComponent != null &&
        selectedComponent.interceptScreenActionInput(e)) {
      return;
    }
    List<SelectableComponent> selectableComponents
        = new ArrayList<>(activeScreen.deepSelectableComponents());

    int key = e.key();
    SelectionResult result;
    switch (key) {
      case Keyboard.KEY_TAB:
        activeScreen.selectNext();
        break;
      case Keyboard.KEY_ARROW_UP:
        if(selectedComponent == null) {
          activeScreen.selectNext();
          break;
        }
        result = selectedComponent.selector()
            .up(selectedComponent, selectableComponents);
        activeScreen.performSelect(result);
        break;
      case Keyboard.KEY_ARROW_DOWN:
        if(selectedComponent == null) {
          activeScreen.selectNext();
          break;
        }
        result = selectedComponent.selector()
            .down(selectedComponent, selectableComponents);
        activeScreen.performSelect(result);
        break;
      case Keyboard.KEY_ARROW_LEFT:
        if(selectedComponent == null) {
          activeScreen.selectNext();
          break;
        }
        result = selectedComponent.selector().left(selectedComponent, selectableComponents);
        activeScreen.performSelect(result);
        break;
      case Keyboard.KEY_ARROW_RIGHT:
        if(selectedComponent == null) {
          activeScreen.selectNext();
          break;
        }
        result = selectedComponent.selector().right(selectedComponent, selectableComponents);
        activeScreen.performSelect(result);
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
      TermPos pos = e.terminalPosition();
      activeScreen.performSelect(pos.x(), pos.y());
    }
    ComponentMouseEvent event = new ComponentMouseEvent(e);
    activeScreen.eventBus().post(e);
    if(!event.cancelledAction()) {
      activeScreen.processMouseEvent(event);
    }
  }

}
