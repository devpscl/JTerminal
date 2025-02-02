package net.jterminal.ui.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.component.tool.ContainerViewArea;
import net.jterminal.ui.component.tool.FullContainerViewArea;
import net.jterminal.ui.event.component.ComponentSelectEvent;
import net.jterminal.ui.event.component.ComponentUnselectEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.selector.SelectionResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RootContainer extends Container {

  private SelectableComponent selectedComponent = null;

  @Override
  public void paint(@NotNull TermGraphics graphics, @NotNull Component component) {
    ComponentGraphics.prepare(component);
    ComponentGraphics.draw(graphics, component);
  }

  @Override
  protected ContainerViewArea containerViewArea() {
    return new FullContainerViewArea();
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    super.paint(graphics);
    for (Component component : deepComponents()) {
      if(component instanceof HeadSurfacePainter painter) {
        if(!component.isVisible()) {
          continue;
        }
        TermGraphics g = TermGraphics.transfer(graphics, component.toStyle());
        painter.paintSurface(g);
      }
    }
  }

  public void select(@NotNull SelectableComponent selectedComponent) {
    if(this.selectedComponent != null) {
      selectedComponent.eventBus().post(new ComponentUnselectEvent(this.selectedComponent));
    }
    this.selectedComponent = selectedComponent;
    selectedComponent.eventBus().post(new ComponentSelectEvent(selectedComponent));
    repaint();
  }

  public void unselect() {
    if(selectedComponent != null) {
      selectedComponent.eventBus().post(new ComponentUnselectEvent(selectedComponent));
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
      if(!component.isEnabled() || !component.isVisible()) {
        continue;
      }
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
      if(!component.isEnabled() || !component.isVisible()) {
        continue;
      }
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
