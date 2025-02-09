package net.jterminal.test.example.scenario;

import net.jterminal.ui.component.ComponentGroup;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.selectable.RadioButtonComponent;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;

public class RadioButtonScenarioContainer extends FrameContainer {

  public void create() {
    RadioButtonComponent radioButton1 = new RadioButtonComponent("Button 1");
    radioButton1.x(5);
    radioButton1.y(3);

    RadioButtonComponent radioButton2 = new RadioButtonComponent("Button 2");
    radioButton2.x(Layout.relative(radioButton1, Anchor.LEFT));
    radioButton2.y(Layout.dock(radioButton1, Anchor.BOTTOM), Layout.offset(2));

    RadioButtonComponent radioButton3 = new RadioButtonComponent("Button 3");
    radioButton3.x(Layout.relative(radioButton1, Anchor.LEFT));
    radioButton3.y(Layout.dock(radioButton2, Anchor.BOTTOM), Layout.offset(2));

    RadioButtonComponent radioButton4 = new RadioButtonComponent("Button 4");
    radioButton4.x(Layout.relative(radioButton1, Anchor.LEFT));
    radioButton4.y(Layout.dock(radioButton3, Anchor.BOTTOM), Layout.offset(2));

    ComponentGroup group = new ComponentGroup();
    group.add(radioButton1, radioButton2, radioButton3, radioButton4);

    add(radioButton1);
    add(radioButton2);
    add(radioButton3);
    add(radioButton4);
  }

}
