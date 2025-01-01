package net.jterminal.ui.selector;

import net.jterminal.ui.component.selectable.SelectableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SelectionResult {

  private final SelectableComponent component;
  private final boolean ignore;

  private SelectionResult(@Nullable SelectableComponent component, boolean ignore) {
    this.component = component;
    this.ignore = ignore;
  }

  public @Nullable SelectableComponent component() {
    return component;
  }

  public boolean isIgnoring() {
    return ignore;
  }

  public static @NotNull SelectionResult ignore() {
    return new SelectionResult(null, true);
  }

  public static @NotNull SelectionResult unselect() {
    return new SelectionResult(null, false);
  }

  public static @NotNull SelectionResult select(@NotNull SelectableComponent component) {
    return new SelectionResult(component, false);
  }

}
