package net.jterminal.ui;

import net.jterminal.Terminal;
import net.jterminal.instance.AbstractNativeTerminal;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.renderer.FastScreenRenderer;
import net.jterminal.ui.renderer.ScreenRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractUITerminal<T extends Terminal> extends AbstractNativeTerminal<T>
    implements UITerminal {

  TermScreen activeScreen = null;
  private final ScreenRenderer screenRenderer;
  private final Object lock = new Object();

  public AbstractUITerminal(Class<T> interfaceType) {
    super(interfaceType);
    screenRenderer = new FastScreenRenderer(this);
  }

  @Override
  public void openScreen(@NotNull TermScreen screen) {
    synchronized (lock) {
      activeScreen = screen;
      activeScreen.terminal = this;
      activeScreen.repaint();
    }
  }

  @Override
  public void closeScreen(@NotNull TermScreen screen) {
    synchronized (lock) {
      activeScreen = null;
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
        TermGraphics graphics = TermGraphics.create(windowSize());
        activeScreen.paint(graphics);
        screenRenderer.render(graphics.buffer());
      }
    }
  }
}
