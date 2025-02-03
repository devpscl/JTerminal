package net.jterminal.test.example;

import java.util.Arrays;
import net.jterminal.Terminal;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.TermScreen;
import net.jterminal.ui.UITerminal;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.LabelComponent;
import net.jterminal.ui.component.selectable.ListViewComponent;
import net.jterminal.ui.event.special.ListItemInteractEvent;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.util.BoxCharacter.Type;
import net.jterminal.ui.util.TextAlignment;

public class HomeScreen extends TermScreen {

  private LabelComponent messageLabel;
  private FrameContainer listViewFrame;
  private ListViewComponent listView;

  private final String[] exampleArray = {"Editor"};

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

    listViewFrame = new FrameContainer("Examples", TextAlignment.CENTER);
    listViewFrame.boxLineType(Type.DOUBLE);
    listViewFrame.x(Layout.relative(Anchor.RIGHT), Layout.offset(-20));
    listViewFrame.y(Layout.relative(Anchor.TOP));
    listViewFrame.width(Layout.fill());
    listViewFrame.height(Layout.fill());

    listView = new ListViewComponent();
    listView.width(Layout.fill());
    listView.height(Layout.fill());
    listView.elements(Arrays.asList(exampleArray));
    listView.eventBus().subscribe(ListItemInteractEvent.class, this::performClickExample);
    listViewFrame.add(listView);

    add(messageLabel);
    add(listViewFrame);
  }

  private void performClickExample(ListItemInteractEvent event) {
    if(event.index() == 0) {
      FileEditorScreen screen = new FileEditorScreen(instance);
      screen.create();
      Terminal.getAs(UITerminal.class).openScreen(screen);
    }
  }



}
