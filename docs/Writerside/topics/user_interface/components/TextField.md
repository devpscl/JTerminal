# TextField

A text field is used to read a text input as line.

<code-block lang="java">
TextFieldComponent textFieldComponent = new TextFieldComponent();
textFieldComponent.value("Hello");
</code-block>
<img src="textfield.png" alt="textfield example"/>

## Methods

| Method                                 | Description                            |
|----------------------------------------|----------------------------------------|
| <code>void value(String)</code>        | Sets the input value                   |
| <code>String value()</code>            | Returns the input value                |
| <code>void limitLength(int)</code>     | Sets maximum length of input buffer    |
| <code>int limitLength()</code>         | Returns maximum length of input buffer |
| <code>void cursor(int)</code>          | Sets text cursor                       |
| <code>int cursor()</code>              | Returns text cursor                    |
| <code>String displayValue()</code>     | Returns current displaying text        |

## Events

| Event                       | Description                    |
|-----------------------------|--------------------------------|
| TextChangedEvent            | Event on change of input value |

