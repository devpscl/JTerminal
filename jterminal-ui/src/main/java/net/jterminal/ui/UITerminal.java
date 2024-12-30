package net.jterminal.ui;

import net.jterminal.NativeTerminal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UITerminal extends NativeTerminal {

  void openScreen(@NotNull TermScreen screen);

  void closeScreen(@NotNull TermScreen screen);

  @Nullable TermScreen openedScreen();

  void drawScreen();

}
