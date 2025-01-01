package net.jterminal.ui.selector;

import java.util.List;
import net.jterminal.ui.component.selectable.SelectableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentSelector {

  @NotNull SelectionResult up(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList);

  @NotNull SelectionResult down(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList);

  @NotNull SelectionResult left(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList);

  @NotNull SelectionResult right(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList);

}
