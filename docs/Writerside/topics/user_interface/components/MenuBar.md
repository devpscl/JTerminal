# MenuBar

The `MenuBar` allows you to insert a control element in the screen, which is always displayed
at the top.

<code-block lang="java">
MenuTab tab1 = new MenuTab("File");
tab1.add(new MenuItem("New"));
tab1.add(new MenuItem("Save"));
tab1.add(new MenuItem("Open"));
tab1.add(new MenuItem("Exit").action(() -> {
  Terminal.get().shutdown(1);
}));

MenuTab tab2 = new MenuTab("Options");
tab2.add(new MenuItem("Colors"));
tab2.add(new MenuItem("Font"));
tab2.add(new MenuItem("Language"));

MenuBarComponent menuBarComponent = new MenuBarComponent();
menuBarComponent.add(tab1);
menuBarComponent.add(tab2);
</code-block>
<img src="menubar.png" alt="menubar example"/>

## Methods

<table>
<tr><td width="300">Method</td><td>Description</td></tr>
<tr><td><code>int minimumMenuWidth()</code></td><td>Returns the width of menu block</td></tr>
<tr><td><code>void minimumMenuWidth(int)</code></td><td>Set maximum width of menu block</td></tr>
<tr><td><code>void selectedStyle(TextStyle)</code></td><td>Sets selection style (Default: TextFont.UNDERLINE)</td></tr>
<tr><td><code>void add(MenuTab)</code></td><td>Add new tab</td></tr>
<tr><td><code>void remove(MenuTab)</code></td><td>Remove tab</td></tr>
<tr><td><code>MenuTab selectedTab()</code></td><td>Returns selected tab. If none is present, null is returned</td></tr>
<tr><td><code>selectTab(MenuTab)</code></td><td>Selects a tab</td></tr>
<tr><td><code>Collection&lt;MenuTab&gt; menuTabs()</code></td><td>Return all tabs</td></tr>
</table>