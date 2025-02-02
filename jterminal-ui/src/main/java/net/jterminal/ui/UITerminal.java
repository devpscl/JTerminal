package net.jterminal.ui;

import net.jterminal.NativeTerminal;
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

}
