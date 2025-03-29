# Layout

The layout class has functions that allow the position and dimension of elements to be 
determined specifically.

<img src="console_direction.png" alt="console direction"/>

The X/Y values starts at `X1; Y1`

## Position Value

The PositionValue class defines a relative axis value (x/y) that is dependent on the 
plane and certain factors.

| Funktion                   | Description                                               |
|----------------------------|-----------------------------------------------------------|
| center                     | Relative position to center                               |
| center(length)             | Relative position to center with offset of half length    |
| origin                     | Relative position to origin of superordinate layer        |
| relative(Anchor)           | Relative position to anchor of superordinate layer        |
| relative(Component,Anchor) | Relative position to anchor of component                  |
| dock(Anchor)               | Relative position to docking anchor of superordinate layer |
| dock(Component,Anchor)     | Relative position to docking anchor of component          |


<tabs>
<tab title="Center">
<procedure>
<code-block lang="java">
PaneContainer paneContainer = new PaneContainer();
paneContainer.backgroundColor(TerminalColor.DARK_RED);
paneContainer.x(Layout.center());
paneContainer.y(Layout.center());
screen.add(paneContainer);
</code-block>
<img src="layout_center.png" width="650" alt="layout_center"/>
</procedure>
</tab>
<tab title="Origin">
<procedure>
<code-block lang="java">
PaneContainer paneContainer = new PaneContainer();
paneContainer.backgroundColor(TerminalColor.DARK_RED);
paneContainer.x(Layout.origin());
paneContainer.y(Layout.origin());
screen.add(paneContainer);
</code-block>
<img src="layout_origin.png" width="650" alt="layout_origin_comp"/>
</procedure>
</tab>
<tab title="Relative">
<procedure>
<code-block lang="java">
PaneContainer paneContainer = new PaneContainer();
paneContainer.backgroundColor(TerminalColor.DARK_RED);
paneContainer.x(Layout.relative(Anchor.RIGHT));
paneContainer.y(Layout.relative(Anchor.BOTTOM));
screen.add(paneContainer);
</code-block>
<img src="layout_relative.png" width="650" alt="layout_relative"/>
</procedure>
</tab>
<tab title="Relative Component">
<procedure>
<code-block lang="java">
//dummy: white box pane
PaneContainer paneContainer = new PaneContainer();
paneContainer.backgroundColor(TerminalColor.DARK_RED);
paneContainer.x(Layout.relative(dummy, Anchor.LEFT));
paneContainer.y(Layout.relative(dummy, Anchor.BOTTOM));
screen.add(paneContainer);
</code-block>
<img src="layout_relative_comp.png" width="650" alt="layout_relative_comp"/>
</procedure>
</tab>
<tab title="Dock Component">
<procedure>
<code-block lang="java">
//dummy: white box pane
PaneContainer paneContainer = new PaneContainer();
paneContainer.backgroundColor(TerminalColor.DARK_RED);
paneContainer.x(Layout.dock(dummy, Anchor.RIGHT));
paneContainer.y(Layout.dock(dummy, Anchor.TOP));
screen.add(paneContainer);
</code-block>
<img src="layout_dock_comp.png" width="650" alt="layout_dock_comp"/>
</procedure>
</tab>
</tabs>

## Dimension Value

The DimensionValue class defines a relative width/height value that is dependent on 
the level and certain factors.

| Funktion                     | Description                                                           |
|------------------------------|-----------------------------------------------------------------------|
| fill                         | Relative Dimension filled up to superordinate layer                   |
| fill(percent)                | Relative Dimension filled up to superordinate layer by percent        |
| relativeTo(Anchor)           | Relative Dimension filled up to anchor of superordinate layer         |
| relativeTo(Component,Anchor) | Relative Dimension filled up to anchor of component                   |
| dockTo(Anchor)               | Relative Dimension filled up to docking anchor of superordinate layer |
| dockTo(Component,Anchor)     | Relative Dimension filled up to docking anchor of component           |

<tabs>
<tab title="Fill">
<procedure>
<code-block lang="java">
PaneContainer paneContainer = new PaneContainer();
paneContainer.backgroundColor(TerminalColor.DARK_RED);
paneContainer.x(Layout.center());
paneContainer.y(Layout.center());
paneContainer.width(Layout.fill());
paneContainer.height(Layout.fill(0.9F));
screen.add(paneContainer);
</code-block>
<img src="layout_fill.png" width="650" alt="layout_fill"/>
</procedure>
</tab>
<tab title="Relative To">
<procedure>
<code-block lang="java">
//dummy: white box pane
PaneContainer paneContainer = new PaneContainer();
paneContainer.backgroundColor(TerminalColor.DARK_RED);
paneContainer.x(2);
paneContainer.y(12);
paneContainer.width(Layout.relativeTo(dummy, Anchor.RIGHT));
paneContainer.height(2);
screen.add(paneContainer);
</code-block>
<img src="layout_relative_to.png" width="650" alt="layout_relative_to"/>
</procedure>
</tab>
<tab title="Dock To">
<procedure>
<code-block lang="java">
//dummy: white box pane
PaneContainer paneContainer = new PaneContainer();
paneContainer.backgroundColor(TerminalColor.DARK_RED);
paneContainer.x(2);
paneContainer.y(12);
paneContainer.width(Layout.dockTo(dummy, Anchor.LEFT));
paneContainer.height(Layout.dockTo(dummy, Anchor.TOP));
screen.add(paneContainer);
</code-block>
<img src="layout_dock_to.png" width="650" alt="layout_dock_to"/>
</procedure>
</tab>
</tabs>

## Modifier

The modifiers can change the dependent values for position or dimension.

| Modifier                 | Description               |
|--------------------------|---------------------------|
| scale(float 0-1)         | Scale the value by factor |
| offset(int)              | Move value by Offset      |
| min(int)                 | Determine a minimum value |
| max(int)                 | Determine a maximum value |

<code-block lang="java">
component.x(Layout.center(), Layout.offset(-5));
component.y(Layout.center(), Layout.max(15));
component.width(Layout.fill(), Layout.scale(0.5F));
component.height(Layout.fill(), Layout.offset(-1));
</code-block>