package net.jterminal.test.example;

import java.util.Arrays;
import net.jterminal.Terminal;
import net.jterminal.test.example.scenario.ButtonScenarioContainer;
import net.jterminal.test.example.scenario.ChooseListScenarioContainer;
import net.jterminal.test.example.scenario.MouseInputScenarioContainer;
import net.jterminal.test.example.scenario.ProgressBarScenarioContainer;
import net.jterminal.test.example.scenario.RadioButtonScenarioContainer;
import net.jterminal.test.example.scenario.ResultDialogScenarioContainer;
import net.jterminal.test.example.scenario.TextInputScenarioContainer;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.TermScreen;
import net.jterminal.ui.UITerminal;
import net.jterminal.ui.component.ActiveContainer;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.LabelComponent;
import net.jterminal.ui.component.selectable.ListViewComponent;
import net.jterminal.ui.event.special.ListItemInteractEvent;
import net.jterminal.ui.event.special.ListItemShowEvent;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.util.BoxCharacter.Type;
import net.jterminal.ui.util.TextAlignment;

public class HomeScreen extends TermScreen {

  private LabelComponent messageLabel;
  private FrameContainer exampleListViewFrame;
  private ListViewComponent exampleListView;
  private FrameContainer scenarioListViewFrame;
  private ListViewComponent scenarioListView;
  private ActiveContainer activeContainer;

  private ButtonScenarioContainer buttonScenarioContainer;
  private RadioButtonScenarioContainer radioButtonScenarioContainer;
  private ProgressBarScenarioContainer progressBarScenarioContainer;
  private ChooseListScenarioContainer chooseListScenarioContainer;
  private TextInputScenarioContainer textInputScenarioContainer;
  private MouseInputScenarioContainer mouseInputScenarioContainer;
  private ResultDialogScenarioContainer resultDialogScenarioContainer;

  private final String[] exampleArray = {"Editor", "Color256"};
  private final String[] scenarioArray = {"Button", "RadioButton", "ProgressBar",
      "ChooseList", "Text Input", "Mouse Input", "Result dialog"
  };

  private final ExampleApp instance;

  public HomeScreen(ExampleApp instance) {
    this.instance = instance;
  }

  public void create() {
    setMouseInputEnabled(true);
    foregroundColor(TerminalColor.WHITE);
    backgroundColor(TerminalColor.BLUE);

    messageLabel = new LabelComponent(TermString.value("Welcome to jterminal home :)"));
    messageLabel.x(2);
    messageLabel.y(2);

    exampleListViewFrame = new FrameContainer(" Examples ", TextAlignment.CENTER);
    exampleListViewFrame.boxLineType(Type.DOUBLE);
    exampleListViewFrame.x(Layout.relative(Anchor.RIGHT), Layout.offset(-20));
    exampleListViewFrame.y(Layout.dock(messageLabel, Anchor.BOTTOM));
    exampleListViewFrame.width(Layout.fill());
    exampleListViewFrame.height(Layout.fill());

    exampleListView = new ListViewComponent();
    exampleListView.width(Layout.fill());
    exampleListView.height(Layout.fill());
    exampleListView.elements(Arrays.asList(exampleArray));
    exampleListView.eventBus().subscribe(ListItemInteractEvent.class, this::performClickExample);
    exampleListViewFrame.add(exampleListView);

    scenarioListViewFrame = new FrameContainer(" Scenarios ", TextAlignment.LEFT, 2);
    scenarioListViewFrame.y(Layout.dock(messageLabel, Anchor.BOTTOM));
    scenarioListViewFrame.width(20);
    scenarioListViewFrame.height(Layout.fill());
    scenarioListViewFrame.boxLineType(Type.NORMAL);

    scenarioListView = new ListViewComponent();
    scenarioListView.width(Layout.fill());
    scenarioListView.height(Layout.fill());
    scenarioListView.elements(Arrays.asList(scenarioArray));
    scenarioListView.eventBus().subscribe(ListItemShowEvent.class, this::performShowScenario);
    scenarioListViewFrame.add(scenarioListView);

    activeContainer = new ActiveContainer();
    activeContainer.x(Layout.dock(scenarioListViewFrame, Anchor.RIGHT));
    activeContainer.y(Layout.relative(scenarioListViewFrame, Anchor.TOP));
    activeContainer.width(Layout.dockTo(exampleListViewFrame, Anchor.LEFT));
    activeContainer.height(Layout.fill());

    add(messageLabel);
    add(exampleListViewFrame);
    add(scenarioListViewFrame);
    add(activeContainer);

    buttonScenarioContainer = new ButtonScenarioContainer();
    radioButtonScenarioContainer = new RadioButtonScenarioContainer();
    progressBarScenarioContainer = new ProgressBarScenarioContainer();
    chooseListScenarioContainer = new ChooseListScenarioContainer();
    textInputScenarioContainer = new TextInputScenarioContainer();
    mouseInputScenarioContainer = new MouseInputScenarioContainer();
    resultDialogScenarioContainer = new ResultDialogScenarioContainer();
    buttonScenarioContainer.create();
    radioButtonScenarioContainer.create();
    progressBarScenarioContainer.create();
    chooseListScenarioContainer.create();
    textInputScenarioContainer.create();
    mouseInputScenarioContainer.create();
    resultDialogScenarioContainer.create();
    activeContainer.activeContainer(buttonScenarioContainer);
  }

  private void performClickExample(ListItemInteractEvent event) {
    switch (event.index()) {
      case 0 -> {
        FileEditorScreen screen = new FileEditorScreen(instance);
        screen.create();
        Terminal.getAs(UITerminal.class).openScreen(screen);
      }
      case 1 -> {
        Color256Screen screen = new Color256Screen(instance);
        screen.create();
        Terminal.getAs(UITerminal.class).openScreen(screen);
      }
    }
  }

  private void performShowScenario(ListItemShowEvent event) {
    switch (event.index()) {
      case 0 -> activeContainer.activeContainer(buttonScenarioContainer);
      case 1 -> activeContainer.activeContainer(radioButtonScenarioContainer);
      case 2 -> activeContainer.activeContainer(progressBarScenarioContainer);
      case 3 -> activeContainer.activeContainer(chooseListScenarioContainer);
      case 4 -> activeContainer.activeContainer(textInputScenarioContainer);
      case 5 -> activeContainer.activeContainer(mouseInputScenarioContainer);
      case 6 -> activeContainer.activeContainer(resultDialogScenarioContainer);
    }
  }



}
