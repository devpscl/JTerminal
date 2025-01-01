package net.jterminal.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.PaneContainer;
import net.jterminal.ui.event.component.ComponentSelectEvent;
import net.jterminal.ui.event.component.ComponentUnselectEvent;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TermScreen extends PaneContainer {

  UITerminal terminal;
  private Component selectedComponent = null;

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

  @Override
  public @NotNull TerminalDimension size() {
    if(terminal == null) {
      return new TerminalDimension();
    }
    return terminal.windowSize();
  }

  @Override
  public @NotNull TerminalDimension effectiveSize() {
    return size();
  }

  @Override
  public @NotNull TerminalPosition position() {
    return new TerminalPosition(1, 1);
  }

  @Override
  public @NotNull TerminalPosition effectivePosition() {
    return new TerminalPosition(1, 1);
  }

  public void select(@NotNull Component selectedComponent) {
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

  public @Nullable Component selectedComponent() {
    return selectedComponent;
  }

  public void performSelect(int x, int y) {
    Collection<Component> components = deepSelectableComponents();
    if(components.isEmpty()) {
      unselect();
      return;
    }
    List<Component> priorityComponents = new ArrayList<>(components);
    Collections.reverse(priorityComponents);
    for (Component component : priorityComponents) {
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
    Collection<Component> components = deepSelectableComponents();
    if(components.isEmpty()) {
      unselect();
      return;
    }
    boolean next = false;
    for (Component component : components) {
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
