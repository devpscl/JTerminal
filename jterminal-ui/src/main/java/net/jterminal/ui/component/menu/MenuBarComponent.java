package net.jterminal.ui.component.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.jterminal.input.Keyboard;
import net.jterminal.input.Keyboard.State;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.Combiner;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.ui.component.HeadSurfacePainter;
import net.jterminal.ui.component.selectable.SelectableComponent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.component.ComponentSelectEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.graphics.shape.BoxShape;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.layout.Layout.Modifier;
import net.jterminal.ui.layout.Layout.PositionValue;
import net.jterminal.ui.util.BoxCharacter.Type;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuBarComponent extends SelectableComponent
    implements HeadSurfacePainter {

  protected TextStyle selectedStyle = TextStyle.create(
      null, null, TextFont.UNDERLINE);

  protected final List<MenuTab> tabs = new ArrayList<>();
  protected MenuTab selectedTab = null;
  protected int itemCursor = -1;
  protected int minimumMenuWidth = 10;

  public MenuBarComponent() {
    super.x(1);
    super.y(1);
    setWidth(Layout.fill());
    setHeight(1);
    foregroundColor(TerminalColor.BLACK);
    backgroundColor(TerminalColor.GRAY);
  }

  public int minimumMenuWidth() {
    return minimumMenuWidth;
  }

  public void minimumMenuWidth(int minimumMenuWidth) {
    this.minimumMenuWidth = minimumMenuWidth;
  }

  protected int findIndex(@Nullable MenuTab menuTab) {
    synchronized (lock) {
      int index = 0;
      for (MenuTab tab : tabs) {
        if(menuTab == tab) {
          return index;
        }
        index++;
      }
      return 0;
    }
  }

  public void selectedStyle(@NotNull TextStyle selectedStyle) {
    this.selectedStyle = selectedStyle;
  }

  public @NotNull TextStyle selectedStyle() {
    return selectedStyle.copy();
  }

  public int tabCount() {
    return tabs.size();
  }

  public @NotNull Collection<MenuTab> menuTabs() {
    return new ArrayList<>(tabs);
  }

  public void add(@NotNull MenuTab tab) {
    synchronized (lock) {
      this.tabs.add(tab);
      selectedTab = null;
    }
  }

  public void remove(@NotNull MenuTab tab) {
    synchronized (lock) {
      this.tabs.remove(tab);
      selectedTab = null;
      itemCursor = -1;
    }
  }

  public @Nullable MenuTab selectedTab() {
    return selectedTab;
  }

  public void selectTab(@Nullable MenuTab selectedTab) {
    synchronized (lock) {
      this.selectedTab = selectedTab;
      itemCursor = -1;
      repaint();
    }
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    int width = graphics.width();
    TextStyle initStyle = graphics.style();
    graphics.fillRect(1, 1, width, 1, ' ');
    int offset = 1;
    boolean selected = isSelected();
    for (MenuTab tab : tabs) {
      String name = tab.name();
      if(tab == selectedTab && selected) {
        graphics.style(Combiner.combine(selectedStyle, initStyle));
      }
      graphics.drawString(offset, 1, name);
      graphics.style(initStyle);
      offset += name.length() + 2;
    }
  }

  @Override
  public void paintSurface(@NotNull TermGraphics graphics) {
    if(!isSelected()) {
      return;
    }
    int offset = 1;
    for (MenuTab tab : tabs) {
      if(selectedTab == tab) {
        int width = Math.max(selectedTab.preferredWidth(), minimumMenuWidth);
        TermGraphics innerGraphics = TermGraphics.create(
            width + 2, selectedTab.count() + 2, graphics.initStyle());

        paintTabList(innerGraphics, selectedTab);
        graphics.draw(offset, 2, innerGraphics);
      }
      String name = tab.name();
      offset += name.length() + 2;
    }
  }

  protected void paintTabList(@NotNull TermGraphics graphics, @NotNull MenuTab tab) {
    TermDim size = graphics.size();
    TextStyle initStyle = graphics.initStyle();
    BoxShape frameShape = new BoxShape(size);
    frameShape.addHorizontal(1, 1, size.width(), Type.DOUBLE);
    frameShape.addHorizontal(1, size.height(), size.width(), Type.DOUBLE);
    frameShape.addVertical(1, 1, size.height(), Type.DOUBLE);
    frameShape.addVertical(size.width(), 1, size.height(), Type.DOUBLE);
    graphics.fillRect(new TermPos(), size, ' ');
    graphics.drawShape(1, 1, frameShape);

    int off = 2;
    int idx = 0;
    for (MenuItem item : tab.items()) {
      if(idx == itemCursor) {
        graphics.style(Combiner.combine(selectedStyle, initStyle));
      }
      graphics.drawString(2, off++, item.name());
      graphics.resetStyle();
      idx++;
    }
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    if(!isSelected()) {
      return;
    }
    int key = event.key();
    int currentIndex = findIndex(selectedTab);
    int count = tabCount();
    if(key == Keyboard.KEY_ARROW_LEFT) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      event.intercept(true);
      currentIndex = Math.max(0, currentIndex - 1);
      selectTab(tabs.get(currentIndex));
      return;
    }
    if(key == Keyboard.KEY_ARROW_RIGHT) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      event.intercept(true);
      currentIndex = Math.min(count - 1, currentIndex + 1);
      selectTab(tabs.get(currentIndex));
      return;
    }
    if(key == Keyboard.KEY_ARROW_UP) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      event.intercept(true);
      if(selectedTab == null) {
        return;
      }
      itemCursor = Math.max(-1, itemCursor - 1);
      repaint();
    }
    if(key == Keyboard.KEY_ARROW_DOWN) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      event.intercept(true);
      if(selectedTab == null) {
        return;
      }
      itemCursor = Math.min(selectedTab.count() - 1, itemCursor + 1);
      repaint();
    }
    if(key == Keyboard.KEY_ENTER) {
      if(event.event().state() == State.CONTROL) {
        return;
      }
      event.intercept(true);
      if(selectedTab == null) {
        return;
      }
      MenuItem menuItem = selectedTab.get(itemCursor);
      if(menuItem == null) {
        return;
      }
      menuItem.performClick();
      unselect();
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    if(event.action() != Action.RELEASE || event.button() != Button.LEFT) {
      return;
    }
    TermPos position = event.position();
    int x = position.x();
    int offset = 1;
    for (MenuTab tab : tabs) {
      String name = tab.name();
      if(x >= offset && x < offset + name.length()) {
        selectTab(tab);
        break;
      }
      offset += name.length() + 2;
    }
  }

  public void performItemClick(int index) {
    itemCursor = index;
    MenuItem menuItem = selectedTab.get(index);
    if(menuItem != null) {
      menuItem.performClick();
      unselect();
      return;
    }
    repaint();
  }

  @Override
  public void processSurfaceMouseInput(@NotNull ComponentMouseEvent event) {
    TermPos position = event.position();
    if(event.action() != Action.PRESS || event.button() != Button.LEFT) {
      return;
    }
    if(isSelected() && selectedTab != null && position.y() > 1) {
      int startX = 1;
      for (MenuTab tab : tabs) {
        if(tab == selectedTab) {
          break;
        }
        String name = tab.name();
        startX += name.length() + 2;
      }
      int width = Math.max(selectedTab.preferredWidth(), minimumMenuWidth);

      int offset = 3;
      int idx = 0;
      for (MenuItem item : selectedTab.items()) {
        if(position.x() >= startX + 1 && position.x() <= startX + width) {
          if(position.y() == offset) {

            performItemClick(idx);
            event.intercept(true);
            event.ignoreChildComponents(true);
            return;
          }
        }
        offset++;
        idx++;
      }
    }
  }

  @Override
  public void x(int value, Modifier... modifiers) {}

  @Override
  public void x(PositionValue positionValue, Modifier... modifiers) {}

  @Override
  public void y(int value, Modifier... modifiers) {}

  @Override
  public void y(PositionValue positionValue, Modifier... modifiers) {}

  public void onSelect(ComponentSelectEvent e) {
    itemCursor = -1;
    if(tabCount() > 0) {
      selectTab(tabs.get(0));
    }
  }

}
