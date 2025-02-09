package net.jterminal.test.example.scenario;

import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.PaneContainer;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.util.TextAlignment;

public class MouseInputScenarioContainer extends FrameContainer {

  public void create() {
    titleAlignment(TextAlignment.LEFT);
    titleOffset(3);
    title("Click in this box anywhere");
    PaneContainer paneContainer = new PaneContainer();
    paneContainer.width(Layout.fill());
    paneContainer.height(Layout.fill());
    paneContainer.eventBus().subscribe(ComponentMouseEvent.class, event -> {
      String str = " Action: %s, Button: %s   X=%03d  Y=%03d ";
      title(String.format(str, event.action().name(), event.button().name(),
          event.position().x(), event.position().y()));
    });
    add(paneContainer);
  }

}
