# CheckBox

The `CheckBox` class is a graphical element for displaying and toggling between 2 states.

<code-block lang="java">
CheckBoxComponent checkBoxComponent = new CheckBoxComponent("CheckBox");
checkBoxComponent.checked(...);
</code-block>
<img src="checkbox.png" alt="checkbox example"/>

## Methods

| Method                              | Description                       |
|-------------------------------------|-----------------------------------|
| <code>void text(String)</code>      | Setzt den Anzeige Text            |
| <code>String text()</code>          | Gibt den Anzeige Text zurück      |
| <code>int preferredWidth()</code>   | Gibt die empfohlene Width zurück  |
| <code>void checked(boolean)</code>  | Setzt den aktuellen Zustand       |
| <code>boolean checked()</code>      | Gibt den aktuellen Zustand zurück |

## Events

| Event                              | Description                |
|------------------------------------|----------------------------|
| CheckBoxChangeEvent                | Event on change of status  |