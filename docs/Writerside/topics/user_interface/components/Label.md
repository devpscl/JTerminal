# Label

The `LabelComponent` class can display a style text to a specific size.

<code-block lang="Java">
LabelComponent labelComponent = new LabelComponent();
labelComponent.text(TermString.value("Hello World!"), true);
</code-block>

<img src="label1.png" alt="label example"/>

<procedure title="Examples">
<code-block lang="Java">
LabelComponent labelComponent = new LabelComponent(termString);
</code-block>
<img src="label2.png" alt="label example"/>
<code-block lang="Java">
LabelComponent labelComponent = new LabelComponent(termString);
labelComponent.autoWrapLines(true);
labelComponent.width(30);
labelComponent.height(20);
</code-block>
<img src="label3.png" alt="label example"/>
<code-block lang="Java">
LabelComponent labelComponent = new LabelComponent(termString);
labelComponent.textAlignment(TextAlignment.CENTER);
</code-block>
<img src="label4.png" alt="label example"/>
</procedure>

## Methods

| Method                                         | Description                                                       |
|------------------------------------------------|-------------------------------------------------------------------|
| <code>void autoWrapLines(boolean)</code>       | Allow automatic line wrapping                                     |
| <code>boolean autoWrapLines()</code>           | Returns the status of whether automatic line breaks are activated |
| <code>void textAlignment(TextAlignment)</code> | Sets the alignment for the displaying text                        |
| <code>TextAlignment textAlignment()</code>     | Returns current alignment option                                  |
| <code>void updateSize()</code>                 | Adjust auto size                                                  |
| <code>TermString text()</code>                 | Returns current text                                              |
| <code>text(TermString)</code>                  | Sets the new text of label                                        |