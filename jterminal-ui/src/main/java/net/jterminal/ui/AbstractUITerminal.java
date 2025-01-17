package net.jterminal.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
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
import net.jterminal.ui.component.Container;
import net.jterminal.ui.component.HeadSurfacePainter;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.event.ScreenCloseEvent;
import net.jterminal.ui.event.ScreenOpenEvent;
import net.jterminal.ui.event.ScreenRenderedEvent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.component.ComponentResizeEvent;
import net.jterminal.ui.event.component.SelectableComponentKeyEvent;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.graphics.TerminalState;
import net.jterminal.ui.renderer.FastScreenRenderer;
import net.jterminal.ui.renderer.ScreenRenderer;
import net.jterminal.ui.selector.SelectionResult;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractUITerminal<T extends Terminal> extends AbstractNativeTerminal<T>
    implements UITerminal {

  TermScreen activeScreen = null;
  private final ScreenRenderer screenRenderer;
  private final Object lock = new Object();
  private final ReentrantLock renderLock = new ReentrantLock();
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
      renderScreenSync();
    }
  }

  private void renderScreenSync() {
    if(renderLock.isLocked()) {
      return;
    }
    renderLock.lock();
    try {
      if(activeScreen != null) {
        long start = System.currentTimeMillis();
        TerminalState globalState = new TerminalState();
        SelectableComponent selectableComponent = activeScreen.selectedComponent();

        ForegroundColor foregroundColor = activeScreen.foregroundColor();
        BackgroundColor backgroundColor = activeScreen.backgroundColor();
        CellData cellData = CellData.empty(foregroundColor, backgroundColor);
        TermGraphics graphics = TermGraphics.create(windowSize(), cellData);
        graphics.foregroundColor(foregroundColor);
        graphics.backgroundColor(backgroundColor);
        activeScreen.paint(graphics);
        if(selectableComponent != null) {
          if(selectableComponent.isEnabled()) {
            TerminalState state = new TerminalState();
            selectableComponent.updateState(state);
            globalState = state.origin(selectableComponent.currentDisplayPosition());
          } else {
            activeScreen.unselect();
          }
        }
        screenRenderer.render(graphics.buffer(), globalState);
        long end = System.currentTimeMillis();
        lastRenderTime = end-start;
        eventBus.post(new ScreenRenderedEvent(activeScreen));
      }
    } finally {
      renderLock.unlock();
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

  protected boolean sendKeyInput(@NotNull Component component,
      @NotNull KeyboardInputEvent e) {
    if(!component.isVisible()) {
      return false;
    }
    boolean interceptInput = false;
    ComponentKeyEvent keyEvent = new ComponentKeyEvent(e);
    component.eventBus().post(keyEvent);
    if(!keyEvent.cancelledAction()) {
      component.processKeyEvent(keyEvent);
    }
    if(keyEvent.cancelledAction() && component instanceof TermScreen) {
      return false;
    }
    if(component instanceof SelectableComponent selectableComponent
        && selectableComponent.isSelected()) {
      SelectableComponentKeyEvent scke
          = new SelectableComponentKeyEvent(e, keyEvent.cancelledAction());
      selectableComponent.processKeyEvent(scke);
      interceptInput = scke.interceptInput();
    }
    if(component instanceof Container c) {
      for (Component child : c.components()) {
        boolean state = sendKeyInput(child, e);
        interceptInput |= state;
      }
    }
    return interceptInput;
  }

  protected void sendMouseInput(@NotNull Component component, @NotNull ComponentMouseEvent e) {
    if(!component.isEnabled()) {
      return;
    }
    component.eventBus().post(e);
    if(!e.cancelledAction()) {
      component.processMouseEvent(e);
    }
    if(e.cancelledAction() && component instanceof TermScreen) {
      return;
    }
    if(component instanceof Container c) {
      for (Component child : c.components()) {
        TermPos mousePos = e.position();
        TermPos effPos = child.currentPosition();
        TermDim effDim = child.currentDimension();
        TermPos effEndPos = effPos.clone().addShift(effDim);
        int x = mousePos.x();
        int y = mousePos.y();
        if(x < effPos.x() || y < effPos.y() || x > effEndPos.x() || y > effEndPos.y()) {
          continue;
        }
        ComponentMouseEvent copiedEvent = e.shiftPosition(effPos);
        sendMouseInput(child, copiedEvent);
      }
    }
  }

  @SubscribeEvent
  public void onInputEvent(KeyboardInputEvent e) {
    if(activeScreen == null) {
      return;
    }
    boolean interceptInput = sendKeyInput(activeScreen, e);
    if(interceptInput) {
      return;
    }
    SelectableComponent selectedComponent = activeScreen.selectedComponent();
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
    if(activeScreen != null) {
      activeScreen.processResizeEvent(
          new ComponentResizeEvent(e.oldDimension(), e.newDimension()));
    }
  }

  @SubscribeEvent
  public void onMouseEvent(MouseInputEvent e) {
    if(activeScreen == null) {
      return;
    }
    boolean cancelled = false;
    for (Component deepComponent : activeScreen.deepComponents()) {
      if(!deepComponent.isEnabled()) {
        continue;
      }
      if(deepComponent instanceof HeadSurfacePainter headSurfacePainter) {
        cancelled |= !headSurfacePainter.processGlobalMouseInput(e);
      }
    }
    if(cancelled) {
      return;
    }
    if(e.action() == Action.PRESS && e.button() == Button.LEFT) {
      TermPos pos = e.terminalPosition();
      activeScreen.performSelect(pos.x(), pos.y());
    }
    ComponentMouseEvent event = new ComponentMouseEvent(e);
    sendMouseInput(activeScreen, event);
  }

}
