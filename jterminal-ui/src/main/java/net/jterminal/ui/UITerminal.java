package net.jterminal.ui;

import net.jterminal.NativeTerminal;
import net.jterminal.Terminal;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.ui.component.RootContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UITerminal extends NativeTerminal {

  void openScreen(@NotNull TermScreen screen);

  void closeScreen();

  @Nullable TermScreen openedScreen();

  @Nullable RootContainer activeRootContainer();

  void drawScreen();

  void drawNewScreen();

  long lastRenderTime();

  static @NotNull UITerminal create() throws TerminalInitializeException {
    return Terminal.create(UITerminalProvider.class);
  }

}
