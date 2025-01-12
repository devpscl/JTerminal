package net.jterminal.ui.component.selectable;

import net.jterminal.ui.TermScreen;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.Container;
import net.jterminal.ui.event.component.SelectableComponentKeyEvent;
import net.jterminal.ui.graphics.TerminalState;
import net.jterminal.ui.selector.AutoSelector;
import net.jterminal.ui.selector.ComponentSelector;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;

public abstract class SelectableComponent extends Component {

  private ComponentSelector selector = new AutoSelector();

  public boolean isSelected() {
    Container container = rootContainer();
    if(container == null) {
      return false;
    }
    if(container instanceof TermScreen screen) {
      return screen.selectedComponent() == this;
    }
    return false;
  }

  public void selector(@NotNull ComponentSelector selector) {
    this.selector = selector;
  }

  public @NotNull ComponentSelector selector() {
    return selector;
  }

  @OverrideOnly
  public void updateState(@NotNull TerminalState terminalState) {}

  public void processKeyEvent(@NotNull SelectableComponentKeyEvent event) {}

}
