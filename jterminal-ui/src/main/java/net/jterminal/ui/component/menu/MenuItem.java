package net.jterminal.ui.component.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuItem {

  private final String name;
  private Runnable action;

  public MenuItem(@NotNull String name) {
    this.name = name;
  }

  public @NotNull String name() {
    return name;
  }

  public @NotNull MenuItem action(@Nullable Runnable action) {
    this.action = action;
    return this;
  }

  public void performClick() {
    if(action != null) {
      action.run();
    }
  }

}
