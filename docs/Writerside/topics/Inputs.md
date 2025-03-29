# Inputs

In the terminal, physical inputs such as keyboard and mouse can be intercepted as well as certain
events when the console window is changed.

***In non-native terminal instances, this feature is severely limited and is not recommended for use!***

<code-block lang="Java">
TerminalInput terminalInput = TerminalInput.create(capacity);
...
terminalInput.close();
</code-block>

The TerminalInput class receives the data from a FIFO buffer from the native engine and can output
it. If the object is no longer required, the `TerminalInput#close()`
method should be used to end the read process.

Example program:
<code-block lang="Java">
Terminal terminal = Terminal.auto();
terminal.flags(Terminal.FLAG_SIGNAL_INPUT
        | Terminal.FLAG_EXTENDED_INPUT);
TerminalInput terminalInput = TerminalInput.create(128);
byte[] buf = new byte[1];
while(true) {
  int len = terminalInput.read(buf, 0, 1);
  if(len &lt;= 0) {
    terminal.writeLine("Error!");
    continue;
  }
  terminal.writeLine("Input: " + buf[0]);
  if(buf[0] == 27) { //Escape key
    terminalInput.close();
    break;
  }
}
terminal.writeLine("Input closed!");
</code-block>

## Input Event

The InputEvent class describes a specific input event that can be read out. The class can be 
inherited from: `KeyboardInputEvent`, `MouseInputEvent`, `WindowInputEvent`

### KeyboardInputEvent

<code-block lang="Java">
while(true) {
  InputEvent inputEvent = terminalInput.readEvent(
    Duration.ofSeconds(4));
  if(inputEvent == null) {
    terminal.writeLine("Timeout!");
    continue;
  }
  terminal.writeLine("Input Event: " + inputEvent);
  if(inputEvent instanceof KeyboardInputEvent kie) {
    if(kie.key() == Keyboard.KEY_ESCAPE) {
      break;
    }
  }
}
</code-block>

### MouseInputEvent
If used, the `FLAG_MOUSE_INPUT` flag or `FLAG_MOUSE_EXTENDED_INPUT` flag must be enabled.

<code-block lang="java">
while(true) {
  InputEvent inputEvent = terminalInput.readEvent();
  if(!(inputEvent instanceof MouseInputEvent event)) {
    continue;
  }
  Button button = event.button();
  TermPos termPos = event.terminalPosition();
  if(button != Button.LEFT) {
    continue;
  }
  terminal.writeLine("Clicked at " + termPos);
}
</code-block>

### WindowInputEvent
If used, the `FLAG_WINDOW_INPUT` flag must be enabled.

<code-block lang="java">
while(true) {
  InputEvent inputEvent = terminalInput.readEvent();
  if(!(inputEvent instanceof WindowInputEvent event)) {
    continue;
  }
  terminal.writeLine("Window resized!");
}
</code-block>

## Integrated listener

When the event listener is activated (`Terminal#inputEventListenerEnabled()`) the events are sent
on via the event bus.

<code-block lang="Java">
terminal.inputEventListenerEnabled(true);
terminal.eventBus().subscribe(KeyboardInputEvent.class, event -> {
  terminal.writeLine("Key: " + event.key());
});
terminal.waitFutureShutdown();
</code-block>