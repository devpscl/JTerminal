package net.jterminal.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.devpscl.eventbus.annotation.SubscribeEvent;
import net.jterminal.Terminal;
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
import net.jterminal.text.style.TextStyle;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.Container;
import net.jterminal.ui.component.HeadSurfacePainter;
import net.jterminal.ui.component.RootContainer;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.dialog.TermDialog;
import net.jterminal.ui.event.ScreenCloseEvent;
import net.jterminal.ui.event.ScreenOpenEvent;
import net.jterminal.ui.event.ScreenRenderedEvent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.component.ComponentResizeEvent;
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
  private final Object screenLock = new Object();
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
    flags(FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT | FLAG_MOUSE_INPUT | FLAG_SIGNAL_INPUT);
    eventBus().register(this);
    setBuffer(BufferId.SECONDARY);
  }

  @Override
  public void openScreen(@NotNull TermScreen screen) {
    synchronized (screenLock) {
      if(activeScreen != null) {
        closeScreen();
      }
      activeScreen = screen;
      activeScreen.terminal = this;
      activeScreen.repaintFully();
      eventBus.post(new ScreenOpenEvent(screen));
    }
  }

  @Override
  public void closeScreen() {
    synchronized (screenLock) {
      TermScreen oldScreen = activeScreen;
      activeScreen = null;
      clear();
      eventBus.post(new ScreenCloseEvent(oldScreen));
    }
  }

  @Override
  public @Nullable TermScreen openedScreen() {
    return activeScreen;
  }

  @Override
  public @Nullable RootContainer activeRootContainer() {
    if(activeScreen == null) {
      return null;
    }
    TermDialog termDialog = activeScreen.openedDialog();
    if(termDialog != null) {
      return termDialog;
    }
    return activeScreen;
  }

  @Override
  public void drawScreen() {
    renderScreenSync();
  }

  private void renderScreenSync() {
    if(renderLock.isLocked()) {
      return;
    }
    renderLock.lock();
    RootContainer rootContainer = activeRootContainer();
    try {
      if(rootContainer != null) {
        long start = System.currentTimeMillis();
        TerminalState globalState = new TerminalState();
        SelectableComponent selectableComponent = rootContainer.selectedComponent();

        ForegroundColor foregroundColor = activeScreen.foregroundColor();
        BackgroundColor backgroundColor = activeScreen.backgroundColor();
        CellData cellData = CellData.empty(foregroundColor, backgroundColor);
        TermGraphics graphics = TermGraphics.create(windowSize(), cellData,
            TextStyle.create(foregroundColor, backgroundColor));
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
    } catch (Throwable t) {
      LOGGER.error("Error while rendering", t);
    }
    finally {
      renderLock.unlock();
    }
  }

  @Override
  public void drawNewScreen() {
    if(activeScreen == null) {
      return;
    }
    int flags = FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT | FLAG_SIGNAL_INPUT;
    if(activeScreen.isMouseInputEnabled()) {
      flags |= FLAG_MOUSE_INPUT;
      if(activeScreen.isMouseMoveInputEnabled()) {
        flags |= FLAG_MOUSE_EXTENDED_INPUT;
      }
    }
    flags(flags);
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
    ComponentKeyEvent keyEvent = new ComponentKeyEvent(component, e);
    component.eventBus().post(keyEvent);
    if(!keyEvent.isCancelledAction()) {
      final Object componentLock = component.getLock();
      synchronized (componentLock) {
        component.processKeyEvent(keyEvent);
      }
    }
    if(keyEvent.isIgnoreChildComponents()) {
      return interceptInput;
    }
    interceptInput |= keyEvent.isIntercept();
    if(component instanceof Container c) {
      for (Component child : c.components()) {
        boolean state = sendKeyInput(child, e);
        interceptInput |= state;
      }
    }
    return interceptInput;
  }

  protected boolean sendMouseInput(@NotNull Component component, @NotNull ComponentMouseEvent e) {
    if(!component.isEnabled()) {
      return false;
    }
    boolean interceptInput = false;
    component.eventBus().post(e);
    if(!e.isCancelledAction()) {
      final Object componentLock = component.getLock();
      synchronized (componentLock) {
        component.processMouseEvent(e);
      }
    }
    interceptInput |= e.isIntercept();
    if(e.isIgnoreChildComponents()) {
      return interceptInput;
    }
    if(component instanceof Container c) {
      final TermPos origin = c.currentComponentViewOrigin();
      ComponentMouseEvent viewMouseEvent = e.shiftPosition(origin, c);
      for (Component child : c.components()) {
        TermPos mousePos = viewMouseEvent.position();
        TermPos effPos = child.currentPosition();
        TermDim effDim = child.currentDimension();
        TermPos effEndPos = effPos.copy().addShift(effDim);
        int x = mousePos.x();
        int y = mousePos.y();
        if(x < effPos.x() || y < effPos.y() || x > effEndPos.x() || y > effEndPos.y()) {
          continue;
        }

        ComponentMouseEvent copiedEvent = viewMouseEvent.shiftPosition(effPos, child);
        boolean state = sendMouseInput(child, copiedEvent);
        interceptInput |= state;
      }
    }
    return interceptInput;
  }

  @SubscribeEvent
  public void onInputEvent(KeyboardInputEvent e) {
    RootContainer rootContainer = activeRootContainer();
    if(rootContainer == null) {
      return;
    }
    TermDialog openedDialog = activeScreen.openedDialog();

    boolean interceptInput = sendKeyInput(rootContainer, e);
    if(interceptInput) {
      return;
    }
    SelectableComponent selectedComponent = rootContainer.selectedComponent();
    List<SelectableComponent> selectableComponents
        = new ArrayList<>(rootContainer.deepSelectableComponents());

    int key = e.key();
    SelectionResult result;
    switch (key) {
      case Keyboard.KEY_ESCAPE:
        if(selectedComponent != null) {
          selectedComponent.unselect();
        }
        break;
      case Keyboard.KEY_TAB:
        rootContainer.selectNext();
        break;
      case Keyboard.KEY_ARROW_UP:
        if(selectedComponent == null) {
          rootContainer.selectNext();
          break;
        }
        result = selectedComponent.selector()
            .up(selectedComponent, selectableComponents);
        rootContainer.performSelect(result);
        break;
      case Keyboard.KEY_ARROW_DOWN:
        if(selectedComponent == null) {
          rootContainer.selectNext();
          break;
        }
        result = selectedComponent.selector()
            .down(selectedComponent, selectableComponents);
        rootContainer.performSelect(result);
        break;
      case Keyboard.KEY_ARROW_LEFT:
        if(selectedComponent == null) {
          rootContainer.selectNext();
          break;
        }
        result = selectedComponent.selector().left(selectedComponent, selectableComponents);
        rootContainer.performSelect(result);
        break;
      case Keyboard.KEY_ARROW_RIGHT:
        if(selectedComponent == null) {
          rootContainer.selectNext();
          break;
        }
        result = selectedComponent.selector().right(selectedComponent, selectableComponents);
        rootContainer.performSelect(result);
        break;
    }
  }

  @SubscribeEvent
  public void onWindowEvent(WindowInputEvent e) {
    drawNewScreen();
    if(activeScreen != null) {
      activeScreen.processResizeEvent(
          new ComponentResizeEvent(activeScreen, e.oldDimension(), e.newDimension()));
    }
  }

  @SubscribeEvent
  public void onMouseEvent(MouseInputEvent e) {
    RootContainer rootContainer = activeRootContainer();
    if(rootContainer == null) {
      return;
    }
    TermDialog openedDialog = activeScreen.openedDialog();
    ComponentMouseEvent event = new ComponentMouseEvent(rootContainer, e);
    if(rootContainer instanceof TermDialog) {
      event = event.shiftPosition(rootContainer.currentPosition(), rootContainer);
    }
    for (Component deepComponent : rootContainer.deepComponents()) {
      if(!deepComponent.isEnabled()) {
        continue;
      }
      if(deepComponent instanceof HeadSurfacePainter headSurfacePainter) {
        headSurfacePainter.processSurfaceMouseInput(event);
      }
    }

    boolean intercept = event.isIntercept();
    if(!event.isIgnoreChildComponents()) {
      intercept |= sendMouseInput(rootContainer, event);
    }
    if(!intercept) {
      if(e.action() == Action.PRESS && e.button() == Button.LEFT) {
        TermPos pos = e.terminalPosition();
        rootContainer.performSelect(pos.x(), pos.y());
      }
    }
  }

}
