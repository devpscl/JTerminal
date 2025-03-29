# ListView

The `ListView` component shows a list of items.

<code-block lang="java">
ListViewComponent listViewComponent = new ListViewComponent();
listViewComponent.elements(
    Arrays.asList("Copper", "Iron", "Steel",
        "Bronze", "Aluminium", "Zinc", "Magnesium",
        "Tin", "Lead", "Gold", "Silver"));
listViewComponent.attachScrollBar();
listViewComponent.width(15);
listViewComponent.height(5);
</code-block>
<img src="listview.png" alt="listview example"/>

## Methods

<table>
<tr><td width="300">Method</td><td>Description</td></tr>
<tr><td><code>void cursorStyle(TextStyle)</code></td><td>Sets the cursor style (Default: TextFont.REVERSED)</td></tr>
<tr><td><code>List&lt;String&gt; elements()</code></td><td>Returns a list of all items</td></tr>
<tr><td><code>void elements(List&lt;String&gt; elements)</code></td><td>Set all items</td></tr>
<tr><td><code>ScrollBar scrollBar()</code></td><td>Return the scrollbar config. If none is present, null is returned.</td></tr>
<tr><td><code>ScrollBar attachScrollBar()</code></td><td>Creating a new Scrollbar and return</td></tr>
<tr><td><code>void detachScrollBar()</code></td><td>Remove scrollbar</td></tr>
<tr><td><code>void updateScrollBar()</code></td><td>Updates the scrollbar if present</td></tr>
<tr><td><code>void cursor(int)</code></td><td>Sets cursor index</td></tr>
<tr><td><code>int cursor()</code></td><td>Return cursor index</td></tr>
</table>

## Events

| Event                    | Description                      |
|--------------------------|----------------------------------|
| ListItemInteractEvent    | Event on interaction of an item  |