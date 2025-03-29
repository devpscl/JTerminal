# First UI Application

Set up the terminal instance:
<code-block lang="Java">
UITerminal terminal = UITerminal.create();
Terminal.set(terminal);
terminal.title("Example UI");
</code-block>

Creation of new screen:
<code-block lang="java">
TermScreen screen = new TermScreen();
screen.backgroundColor(TerminalColor.BLUE);
terminal.openScreen(screen);
</code-block>

Add label component:
<code-block lang="java">
TermString ts = TermString.value("A label");
LabelComponent labelComponent = new LabelComponent(ts);
labelComponent.x(1);
labelComponent.y(1);
screen.add(labelComponent);
screen.repaint(); //Required if screen is already opened
</code-block>

<img src="ui_first_app.png" alt="result"/>