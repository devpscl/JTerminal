# UI Module

[Go to docs](User-Interface-UI.md)

To use this module in your project.
You need to add the following repository and dependencies
to your project via Gradle or Maven.

<tabs>
<tab title="Maven">
<code-block lang="xml">
&lt;dependency&gt;
    &lt;groupId&gt;net.jterminal&lt;/groupId&gt;
    &lt;artifactId&gt;jterminal-ui&lt;/artifactId&gt;
    &lt;version&gt;1.0.0&lt;/version&gt;
 &lt;/dependency>
</code-block>
</tab>
<tab title="Gradle (Kotlin)">
<code-block lang="groovy">
dependencies {
    implementation("net.jterminal:jterminal-ui:1.0.0")
 }
</code-block>
</tab>
<tab title="Gradle (Groovy)">
<code-block lang="gradle">
dependencies {
    implementation "net.jterminal:jterminal-ui:1.0.0"
 }
</code-block>w
</tab>
</tabs>

### UI instance
<code-block lang="java">
UITerminal terminal = Terminal.create(UITerminalProvider.class);
Terminal.set(terminal);
</code-block>
