# Graphics

The `TermGraphics` class offers a variety of methods for fill a cell buffer.

<procedure>
<step>
Create graphics type by buffer:
<code-block lang="Java">
final TermDim winSize = terminal.windowSize();
TermGraphics termGraphics = TermGraphics.create(winSize);
</code-block>
</step>
<step>
Execute some graphic fill methods:
<code-block lang="Java">
termGraphics.foregroundColor(TerminalColor.GREEN);
termGraphics.fillRect(2, 2, 5, 5, '#');
</code-block>
</step>
<step>
Display graphics buffer to terminal screen:
<code-block lang="Java">
ScreenRenderer fastScreenRenderer = new FastScreenRenderer(terminal);
fastScreenRenderer.render(termGraphics.buffer(), new TerminalState());
</code-block>
</step>
<p>Console:</p>
<img src="graphics_example.png" alt="output"/>
</procedure>

## BoxShape
A box shape is a visual way to show frame symbols<br/>
Frame symbol types: `NORMAL`,`BRIGHT`,`DOUBLE`

<code-block lang="java">
BoxShape boxShape = new BoxShape(new TermDim(16, 16));
boxShape.addBox(1, 1, 16, 16, Type.DOUBLE); //x1, y1, x2, y2, type
boxShape.addVertical(8, 1, 16, Type.NORMAL); //x1, y1, y2, type
boxShape.addHorizontal(1, 8, 16, Type.NORMAL); //x1, y1, x2, type
boxShape.addHorizontal(1, 4, 16, Type.BRIGHT); //x1, y1, x2, type
boxShape.addVertical(12, 8, 16, Type.DOUBLE); //x1, y1, y2, type
termGraphics.drawShape(1, 1, boxShape);
</code-block>

<img src="graphics_shape.png" alt="output"/>

<code>addVertical(x1,y1,y2,type)</code><br/>
<code>addHorizontal(x1,y1,x2,type)</code><br/>
<code>addBox(x1,y1,x2,y2,type)</code><br/>