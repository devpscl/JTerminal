# Slider

The `Slider` makes it possible to achieve a status value between 0 and 1 by sliding.

<code-block lang="java">
SliderComponent sliderComponent = new SliderComponent();
sliderComponent.value(0.7F);
sliderComponent.width(30);
</code-block>
<img src="slider.png" alt="slider example"/>

## Methods

| Method                                    | Description                 |
|-------------------------------------------|-----------------------------|
| <code>void value(float)</code>            | Sets the status value (0-1) |
| <code>float value()</code>                | Returns the status value    |

## Events

| Event              | Description               |
|--------------------|---------------------------|
| SliderChangeEvent  | Event on change of status |