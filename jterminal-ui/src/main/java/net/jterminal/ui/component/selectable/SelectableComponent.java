package net.jterminal.ui.component.selectable;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.RootContainer;
import net.jterminal.ui.graphics.TerminalState;
import net.jterminal.ui.selector.AutoSelector;
import net.jterminal.ui.selector.ComponentSelector;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;

public abstract class SelectableComponent extends Component {

  private ComponentSelector selector = new AutoSelector();

  public boolean isSelected() {
    RootContainer container = rootContainer();
    if(container == null) {
      return false;
    }
    return container.selectedComponent() == this;
  }

  public void selector(@NotNull ComponentSelector selector) {
    this.selector = selector;
  }

  public @NotNull ComponentSelector selector() {
    return selector;
  }

  @OverrideOnly
  public void updateState(@NotNull TerminalState terminalState) {}

  public void select() {
    RootContainer container = rootContainer();
    if(container != null) {
      container.select(this);
    }
  }

  public void unselect() {
    RootContainer container = rootContainer();
    if(container != null) {
      container.unselect();
    }

  }

}
