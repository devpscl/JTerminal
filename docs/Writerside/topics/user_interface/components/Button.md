# Button

The `ButtonComponent` class makes it possible to add your own labeled elements to the user interface
and equip them with an action event. The button is selectable and can
be controlled by tab, arrow keys or mouse input.

<code-block lang="java">
ButtonComponent buttonComponent = new ButtonComponent();
buttonComponent.text("I am Button");
buttonComponent.action(() -> {
  //...
});
</code-block>
<img src="button_unselect.png" alt="button unselected"/>
<img src="button_select.png" alt="button selected"/>

## Methods

| Method                            | Description                       |
|-----------------------------------|-----------------------------------|
| <code>int preferredWidth()</code> | Returns the recommended width     |
| <code>String text()</code>        | Returns the name                  |
| <code>void text(String)</code>    | Sets the name and adjust the size |
| <code>Runnable action()</code>    | Returns the action event          |
| <code>void action(Runnable)</code> | Sets the action event             |

## Events

<procedure title="ButtonClickedEvent">
Event is sent on interaction with button.
<code-block lang="java">
buttonComponent.eventBus().subscribe(ButtonClickedEvent.class, event -> {
//...
});
</code-block>
</procedure>

