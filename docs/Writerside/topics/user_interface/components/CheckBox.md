# CheckBox

The `CheckBox` class is a graphical element for displaying and toggling between 2 states.

<code-block lang="java">
CheckBoxComponent checkBoxComponent = new CheckBoxComponent("CheckBox");
checkBoxComponent.checked(...);
</code-block>
<img src="checkbox.png" alt="checkbox example"/>

## Methods

| Method                              | Description                 |
|-------------------------------------|-----------------------------|
| <code>void text(String)</code>      | Sets the display text       |
| <code>String text()</code>          | Retuns the display text     |
| <code>int preferredWidth()</code>   | Returns the recommend width |
| <code>void checked(boolean)</code>  | Sets the status             |
| <code>boolean checked()</code>      | Returns the current status  |

## Events

| Event                              | Description                |
|------------------------------------|----------------------------|
| CheckBoxChangeEvent                | Event on change of status  |