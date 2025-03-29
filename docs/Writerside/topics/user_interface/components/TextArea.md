# TextArea

The `TextArea` is similar to [TextField](TextField.md), but here several lines can be
edited as well as styled text support.

<code-block lang="java">
TextAreaComponent textAreaComponent = new TextAreaComponent();
textAreaComponent.text(termString);
textAreaComponent.width(80);
textAreaComponent.height(6);
textAreaComponent.attachScrollBar(Axis.VERTICAL);
</code-block>
<img src="textarea.png" alt="textarea example"/>

## Methods

<table>
<tr><td width="300">Method</td><td>Description</td></tr>
<tr><td><code>boolean editable()</code></td><td>Returns true if the textarea is editable</td></tr>
<tr><td><code>void editable(boolean)</code></td><td>Sets the editable option</td></tr>
<tr><td><code>CursorType cursorType()</code></td><td>Returns the cursor type</td></tr>
<tr><td><code>void cursorType(CursorType)</code></td><td>Sets the cursor type</td></tr>
<tr><td><code>ScrollBar attachScrollBar(Axis)</code></td><td>Create scrollbar and return it</td></tr>
<tr><td><code>void detachScrollBar(Axis)</code></td><td>Remove scrollbar</td></tr>
<tr><td><code>void updateScrollBar()</code></td><td>Updates the scrollbar if present</td></tr>
<tr><td><code>int cursorChar()</code></td><td>Returns the cursor of character in current line</td></tr>
<tr><td><code>int cursorLine()</code></td><td>Returns the line cursor</td></tr>
<tr><td><code>void text(TermString)</code></td><td>Sets the text value</td></tr>
<tr><td><code>TermString text()</code></td><td>Returns the text value</td></tr>
<tr><td><code>List&lt;TermString&gt; viewLines()</code></td><td>Returns the currently displayed lines</td></tr>

</table>

## Events

| Event                       | Description             |
|-----------------------------|-------------------------|
| TextChangedEvent            | Event on change of text |
