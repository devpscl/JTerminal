# CLI Module

[Go to docs](Command-Line.md)

To use this module in your project.
You need to add the following repository and dependencies
to your project via Gradle or Maven.

<tabs>
<tab title="Maven">
<code-block lang="xml">
&lt;dependency&gt;
    &lt;groupId&gt;net.jterminal&lt;/groupId&gt;
    &lt;artifactId&gt;jterminal-cli&lt;/artifactId&gt;
    &lt;version&gt;%version%&lt;/version&gt;
 &lt;/dependency>
</code-block>
</tab>
<tab title="Gradle (Kotlin)">
<code-block lang="groovy">
dependencies {
    implementation("net.jterminal:jterminal-cli:%version%")
 }
</code-block>
</tab>
<tab title="Gradle (Groovy)">
<code-block lang="gradle">
dependencies {
    implementation "net.jterminal:jterminal-cli:%version%"
 }
</code-block>
</tab>
</tabs>

### CLI instance
<code-block lang="java">
CLITerminal terminal = Terminal.create(CLITerminalProvider.class);
Terminal.set(terminal);
</code-block>



