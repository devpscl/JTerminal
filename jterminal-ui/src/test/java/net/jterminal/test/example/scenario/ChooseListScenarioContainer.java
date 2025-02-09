package net.jterminal.test.example.scenario;

import java.util.Arrays;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.selectable.ListChooseComponent;

public class ChooseListScenarioContainer extends FrameContainer {

  private static final String[] array = {
      "Cherry", "Apple", "Banana", "Carrot", "Cucumber", "Orange",
      "Lemon", "Peach", "Pepper", "Peas", "Strawberry", "Melon",
      "Pineapple", "Tomato", "Pear", "Plum", "Lime",
      "Apricot", "Grape", "Celery", "Spinach", "Beetroot",
      "Ginger", "Radish", "Pumpkin"
  };

  public void create() {

    ListChooseComponent chooseList = new ListChooseComponent();
    chooseList.elements(Arrays.asList(array));
    chooseList.x(5);
    chooseList.y(3);
    chooseList.width(20);
    chooseList.height(10);
    chooseList.attachScrollBar();

    add(chooseList);
  }

}
