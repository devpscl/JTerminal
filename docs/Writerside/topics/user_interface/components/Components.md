# Components

<img src="component_structure.png" alt="component structure"/>

## TermScreen

The terminal screen contains all components and containers as well as open dialogs. The screen can
start by <code>UITerminal#openScreen(...)</code> and stop by <code>UITerminal#closeScreen()</code>
The screen occupies the dimension of the console 
window and is the main origin of all elements added to this screen.

## RootContainer
The root container is the origin of all components and the superclass of `TermScreen` and
`TermDialog`. The selection of components with superclass (<code>SelectableComponent</code>) is
controlled by keyboard and mouse inputs.

<code-block lang="java">
RootContainer container = ...;
SelectableComponent component = container.selectedComponent();
//the component variable is the current selected element in the root container
</code-block>
<code-block lang="java">
container.select(SelectableComponent);
</code-block>
<code-block lang="java">
container.unselect();
</code-block>

## SelectableComponent
All components with `SelectableComponent` as superclass, are selectabe if it enabled
(`Component#enabled()`) and visible (`Component#visible()`).

## Container
The container has the option of having subordinate components and containers. 
All subordinate elements on the first layer refer to this container.

<code-block lang="java">
Container container = ...;
container.add(component);
container.remove(component);
for (Component component : container.components()) {
  //child component from first layer
}
</code-block>

