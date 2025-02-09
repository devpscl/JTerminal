package net.jterminal.test.example.scenario;

import net.jterminal.Terminal;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.selectable.ButtonComponent;
import net.jterminal.ui.event.special.ButtonClickedEvent;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;

public class ButtonScenarioContainer extends FrameContainer {

  public void create() {
    ButtonComponent button1 = new ButtonComponent("Click me!");
    button1.x(Layout.center(button1.preferredWidth()));
    button1.y(5);

    ButtonComponent button2 = new ButtonComponent("Generate Random");
    button2.x(Layout.center(button2.preferredWidth()));
    button2.y(Layout.dock(button1, Anchor.BOTTOM), Layout.offset(2));
    button2.eventBus().subscribe(ButtonClickedEvent.class, event -> {
      Terminal terminal = Terminal.get();
      terminal.title("Random: " + Math.random());
    });

    ButtonComponent button3 = new ButtonComponent("Beep");
    button3.x(Layout.center(button3.preferredWidth()));
    button3.y(Layout.dock(button2, Anchor.BOTTOM), Layout.offset(2));
    button3.eventBus().subscribe(ButtonClickedEvent.class, event -> {
      Terminal terminal = Terminal.get();
      terminal.beep();
    });

    add(button1);
    add(button2);
    add(button3);
  }

}
