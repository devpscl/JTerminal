package net.jterminal.ui.selector;

import java.util.List;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoSelector implements ComponentSelector {

  private @Nullable SelectableComponent first(@NotNull List<SelectableComponent> list) {
    if(list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

  private @Nullable SelectableComponent find(@NotNull List<SelectableComponent> list,
      boolean above, boolean below, boolean left, boolean right,
      @NotNull SelectableComponent origin) {
    TermPos originPos = origin.currentDisplayPosition();
    int ox = originPos.x();
    int oy = originPos.y();
    double distance = Double.MAX_VALUE;
    SelectableComponent found = null;
    for (SelectableComponent component : list) {
      if(!component.isEnabled() || !component.isVisible()) {
        continue;
      }
      if(component == origin) {
        continue;
      }
      TermPos midPos = component.currentDisplayPosition();
      int cx = midPos.x();
      int cy = midPos.y();
      if(!above && oy > cy) {
        continue;
      }
      if(!below && oy < cy) {
        continue;
      }
      if(!left && ox > cx) {
        continue;
      }
      if(!right && ox < cx) {
        continue;
      }
      if((above ^ below) && oy == cy) {
        continue;
      }
      if((left ^ right) && ox == cx) {
        continue;
      }

      double value = midPos.distance(originPos);
      if(value < distance) {
        distance = value;
        found = component;
      }
    }

    return found;
  }

  private @NotNull SelectionResult createResult(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList,
      boolean above, boolean below, boolean left, boolean right) {
    if(origin == null) {
      SelectableComponent first = first(selectableComponentList);
      if(first == null) {
        return SelectionResult.unselect();
      }
      return SelectionResult.select(first);
    }
    SelectableComponent component = find(selectableComponentList, above, below,
        left, right, origin);
    if(component == null) {
      return SelectionResult.ignore();
    }
    return SelectionResult.select(component);
  }

  @Override
  public @NotNull SelectionResult up(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList) {
    return createResult(origin, selectableComponentList,
        true, false, true, true);
  }

  @Override
  public @NotNull SelectionResult down(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList) {

    return createResult(origin, selectableComponentList,
        false, true, true, true);
  }

  @Override
  public @NotNull SelectionResult left(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList) {
    return createResult(origin, selectableComponentList,
        true, true, true, false);
  }

  @Override
  public @NotNull SelectionResult right(@Nullable SelectableComponent origin,
      @NotNull List<SelectableComponent> selectableComponentList) {
    return createResult(origin, selectableComponentList,
        true, true, false, true);
  }
}
