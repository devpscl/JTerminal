package net.jterminal.ui.component.selectable;

import java.util.List;
import net.jterminal.text.Combiner;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.ui.event.special.ListItemInteractEvent;
import net.jterminal.ui.event.special.ListItemChooseEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class ListChooseComponent extends ListViewComponent {

  private TextStyle selectedStyle = TextStyle.create(
      null, null, TextFont.UNDERLINE);
  private int selectedIndex = -1;

  public ListChooseComponent() {
    eventBus.subscribe(ListItemInteractEvent.class, this::onInteract);
  }

  public void selectedStyle(@NotNull TextStyle selectedStyle) {
    this.selectedStyle = selectedStyle;
  }

  public @NotNull TextStyle selectedStyle() {
    return selectedStyle.copy();
  }

  public void select(int selected) {
    synchronized (lock) {
      if(elements.isEmpty()) {
        this.selectedIndex = -1;
        repaint();
        eventBus.post(new ListItemChooseEvent(this, selectedIndex));
        return;
      }
      this.selectedIndex = Math.max(-1, Math.min(selected, elements.size()));
      repaint();
      eventBus.post(new ListItemChooseEvent(this, selectedIndex));
    }
  }

  public void elements(List<String> elements) {
    synchronized (lock) {
      selectedIndex = -1;
      super.elements(elements);
    }
  }

  @Override
  public void paintItem(@NotNull TermGraphics graphics, @NotNull TermPos pos, int index,
      @NotNull String itemValue, boolean selected) {
    if(selectedIndex == index && selected) {
      TextStyle cursorStyle = Combiner.combine(this.selectedStyle, graphics.style());
      graphics.style(cursorStyle);
    }
    super.paintItem(graphics, pos, index, itemValue, selected);
  }

  private void onInteract(@NotNull ListItemInteractEvent event) {
    if(selectedIndex == event.index()) {
      select(-1);
    } else {
      select(event.index());
    }
  }

}