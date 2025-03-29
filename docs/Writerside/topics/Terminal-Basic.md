# Terminal Basic

The terminal interface contains all relevant functions for a console application.


<procedure id="auto_instance" title="Create and set automatic instance">
    If the property <code>termnative</code> is set to false or the system is not 
    supported, the instance is automatically set to
    <code>NonNativeProvider</code>.
    <code-block lang="Java">
        Terminal terminal = Terminal.auto();
    </code-block>
</procedure>

<procedure id="manually_instance" title="Create and set manual instance">
    If instances are created via unspecified providers, such as <code>NativeProvider</code>
    for an unsupported system, an exception is thrown.
    <code-block lang="Java">
        Terminal terminal = Terminal.create(provider_class);
        Terminal.set(terminal);
    </code-block>
</procedure>

Example with change of several instances:
<code-block lang="Java">
Terminal terminal = Terminal.auto();
Terminal nonNativeTerminal = 
    Terminal.create(NonNativeTerminalProvider.class);
Terminal nativeTerminal = 
    Terminal.create(NativeTerminalProvider.class);

Terminal.set(nonNativeTerminal);
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
nonNativeTerminal.write("Title: ");
String titleStr = br.readLine();

Terminal.set(nativeTerminal);
nativeTerminal.title(titleStr);
nativeTerminal.writeLine("Native Terminal enabled");
</code-block>

## Terminal flags

| Flag (byte)                      | Description                                                                         |
|----------------------------------|-------------------------------------------------------------------------------------|
| (0x01) FLAG_LINE_INPUT           | Inputs can only be read out if the line ends with a line break                      |
| (0x02) FLAG_ECHO                 | Inputs are printed out in the console                                               |
| (0x04) FLAG_MOUSE_INPUT          | Enable mouse input (Click event)                                                    |
| (0x08) FLAG_MOUSE_EXTENDED_INPUT | Enable mouse input (Click event, Move event)                                        |
| (0x10) FLAG_EXTENDED_INPUT       | Enable the internal FIFO input buffer + event processor (Required at TerminalInput) |
| (0x20) FLAG_SIGNAL_INPUT         | Allows to receive console signals (Ctrl+C, Ctrl+Z, ...)                             |
| (0x40) FLAG_WINDOW_INPUT         | Enables the sending of window events                                                |

Default flags: `FLAG_LINE_INPUT`,`FLAG_ECHO`,`FLAG_SIGNAL_INPUT`

<code-block lang="Java" title="Test">
terminal.flags(Terminal.FLAG_???);
</code-block>


## Cursor flags

| Flag (byte)            | Description                                |
|------------------------|--------------------------------------------|
| (0x01) CURSOR_VISIBLE  | Enable the visibility of text cursor.      |
| (0x02) CURSOR_BLINKING | Enable the blink-animation of text cursor. |

Default flags: `CURSOR_VISIBLE`,`CURSOR_BLINKING`

<code-block lang="Java" title="Test">
terminal.cursorFlags(Terminal.CURSOR_???);
</code-block>

## JVM Properties

| Property key       | Wert         | Description                                                                                   |
|--------------------|--------------|-----------------------------------------------------------------------------------------------|
| termnative         | Bool= True   | Allows the library to access the Native.                                                      |
| termprovider       | String= null | Determine provider class for <code>Terminal.auto()</code>.                                    |
| forceprovider      | Bool= False  | Forces loading for a specific provider class.                                                 |
| inbuffer_size      | Int= 2048    | Determines the internal input FIFO buffer size for inputs.                                    |
| execmode           | String= Effi | Execution mode.<br/>efficiency: slow but efficient<br/>performance: fast but more power claim |
| nolibcache         | Bool= False  | Disable the Native-Shared-Library cache.                                                      |
