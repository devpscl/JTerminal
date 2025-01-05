package net.jterminal.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.jterminal.ui.component.PaneContainer;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.event.component.ComponentSelectEvent;
import net.jterminal.ui.event.component.ComponentUnselectEvent;
import net.jterminal.ui.selector.SelectionResult;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TermScreen extends PaneContainer {

  UITerminal terminal;
  private SelectableComponent selectedComponent = null;
  private TermDim originalSize = null;

  @Override
  public void repaint() {
    if(!isOpened()) {
      return;
    }
    terminal.drawScreen();
  }

  @Override
  public void repaintFully() {
    if(!isOpened()) {
      return;
    }
    terminal.drawNewScreen();
  }

  public boolean isOpened() {
    if(terminal == null) {
      return false;
    }
    return terminal.openedScreen() == this;
  }

  public void setOriginalSize(@Nullable TermDim originalSize) {
    this.originalSize = originalSize;
  }

  @Override
  public @NotNull TermDim size() {
    if(originalSize != null) {
      return originalSize;
    }
    if(terminal == null) {
      return new TermDim();
    }
    return terminal.defaultWindowSize();
  }

  @Override
  public @NotNull TermDim effectiveSize() {
    if(terminal == null) {
      return new TermDim();
    }
    return terminal.windowSize();
  }

  @Override
  public @NotNull TermPos position() {
    return new TermPos(1, 1);
  }

  @Override
  public @NotNull TermPos effectivePosition() {
    return new TermPos(1, 1);
  }

  public void select(@NotNull SelectableComponent selectedComponent) {
    if(this.selectedComponent != null) {
      selectedComponent.eventBus().post(new ComponentUnselectEvent());
    }
    this.selectedComponent = selectedComponent;
    selectedComponent.eventBus().post(new ComponentSelectEvent());
    repaint();
  }

  public void unselect() {
    if(selectedComponent != null) {
      selectedComponent.eventBus().post(new ComponentUnselectEvent());
      selectedComponent = null;
      repaint();
    }
  }

  public @Nullable SelectableComponent selectedComponent() {
    return selectedComponent;
  }

  public void performSelect(@NotNull SelectionResult result) {
    if(result.isIgnoring()) {
      return;
    }
    SelectableComponent component = result.component();
    if(component == null) {
      unselect();
      return;
    }
    select(component);
  }

  public void performSelect(int x, int y) {
    Collection<SelectableComponent> components = deepSelectableComponents();
    if(components.isEmpty()) {
      unselect();
      return;
    }
    List<SelectableComponent> priorityComponents = new ArrayList<>(components);
    Collections.reverse(priorityComponents);
    for (SelectableComponent component : priorityComponents) {
      if(component.contains(x, y)) {
        if(component == selectedComponent) {
          return;
        }
        select(component);
        return;
      }
    }
    unselect();
  }

  public void selectNext() {
    Collection<SelectableComponent> components = deepSelectableComponents();
    if(components.isEmpty()) {
      unselect();
      return;
    }
    boolean next = false;
    for (SelectableComponent component : components) {
      if(selectedComponent == null) {
        select(component);
        return;
      }
      if(component == selectedComponent) {
        next = true;
        continue;
      }
      if(next) {
        select(component);
        return;
      }
    }
    if(next) {
      unselect();
    }
  }

}
