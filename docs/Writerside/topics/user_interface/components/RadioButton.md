# RadioButton

The `RadioButton` works similar to [CheckBox](CheckBox.md) but has a different design and
can only be selected individually within a component group.

<code-block lang="java">
RadioButtonComponent radioButtonComponent = new RadioButtonComponent("RB 1");
RadioButtonComponent radioButtonComponent2 = new RadioButtonComponent("RB 2");
RadioButtonComponent radioButtonComponent3 = new RadioButtonComponent("RB 3");
radioButtonComponent.checked(true);
ComponentGroup group = new ComponentGroup();
group.add(radioButtonComponent, radioButtonComponent2, radioButtonComponent3);
</code-block>

<img src="radiobutton.png" alt="radiobutton example"/>

## Methods

| Method                              | Description                 |
|-------------------------------------|-----------------------------|
| <code>void text(String)</code>      | Sets the display text       |
| <code>String text()</code>          | Returns the display text    |
| <code>int preferredWidth()</code>   | Returns the preferred width |
| <code>void checked(boolean)</code>  | Sets the current status     |
| <code>boolean checked()</code>      | Returns the current status  |

## Events

| Event                                   | Description               |
|-----------------------------------------|---------------------------|
| RadioButtonChangeEvent                  | Event on change of status |