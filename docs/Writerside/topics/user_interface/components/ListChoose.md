# ListChoose

The `ListChoose` component is similar to [ListView](ListView.md) component but has the 
option of selecting an item.

<code-block lang="java">
ListChooseComponent listViewComponent = new ListChooseComponent();
listViewComponent.elements(
    Arrays.asList("Copper", "Iron", "Steel",
        "Bronze", "Aluminium", "Zinc", "Magnesium",
        "Tin", "Lead", "Gold", "Silver"));
listViewComponent.attachScrollBar();
listViewComponent.width(15);
listViewComponent.height(5);
</code-block>
<img src="listchoose.png" alt="listchoose example"/>

## Methods

See [ListView](ListView.md)

<table>
<tr><td width="300">Method</td><td>Description</td></tr>
<tr><td><code>void selectedStyle(TextStyle)</code></td><td>Sets the selection style (Default: TextFont.UNDERLINE)</td></tr>
<tr><td><code>select(int)</code></td><td>Sets the selected item index</td></tr>
<tr><td><code>int selectedIndex()</code></td><td>Returns selected item index</td></tr>
</table>

## Events

| Event                   | Description                |
|-------------------------|----------------------------|
| ListItemChooseEvent     | Event on choose of an item |