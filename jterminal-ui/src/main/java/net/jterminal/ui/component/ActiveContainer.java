package net.jterminal.ui.component;

import net.jterminal.ui.component.tool.ContainerViewArea;
import net.jterminal.ui.component.tool.FullContainerViewArea;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.layout.Layout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveContainer extends Container implements Resizeable {

  private Container container;

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    if(container == null) {
      return;
    }
    container.setWidth(Layout.fill());
    container.setHeight(Layout.fill());
    ComponentGraphics.prepare(container);
    ComponentGraphics.draw(graphics, container);
  }

  @Override
  public void paint(@NotNull TermGraphics graphics, @NotNull Component component) {}

  @Override
  protected ContainerViewArea containerViewArea() {
    return new FullContainerViewArea();
  }

  public @Nullable Container activeContainer() {
    return container;
  }

  public void activeContainer(@Nullable Container container) {
    this.container = container;
    if(container == null) {
      super.removeAll();
      repaint();
      return;
    }
    super.add(container);
    repaint();
  }

  public boolean hasActiveContainer() {
    return container != null;
  }
}
