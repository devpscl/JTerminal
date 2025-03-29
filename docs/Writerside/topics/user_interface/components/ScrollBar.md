# ScrollBar

The `ScrollBar` works in a similar way to [Slider](Slider.md)  but is mainly used to navigate
in a flexible view.

<note>
The scrollbar can only be controlled with the mouse.
</note>

<code-block lang="java">
ScrollBarComponent hoScrollBar = new ScrollBarComponent(Axis.HORIZONTAL);
hoScrollBar.width(40);
ScrollBarComponent veScrollBar = new ScrollBarComponent(Axis.VERTICAL);
veScrollBar.height(15);
</code-block>
<img src="scrollbar.png" alt="scrollbar example"/>

## Methods

| Method                              | Description                  |
|-------------------------------------|------------------------------|
| <code>ScrollBar scrollBar()</code>  | Returns the scrollbar config |

## Example

<code-block lang="java">
ScrollBarComponent veScrollBar = new ScrollBarComponent(Axis.VERTICAL);
veScrollBar.height(15);
ScrollBar scrollBar = veScrollBar.scrollBar();
scrollBar.update(0, 20); //index 0 of 20
</code-block>