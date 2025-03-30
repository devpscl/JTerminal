# Core Module

To use this module in your project.
You need to add the following repository and dependencies
to your project via Gradle or Maven.

<tabs>
<tab title="Maven">
<code-block lang="xml">
&lt;dependency&gt;
    &lt;groupId&gt;net.jterminal&lt;/groupId&gt;
    &lt;artifactId&gt;jterminal-core&lt;/artifactId&gt;
    &lt;version&gt;%version%&lt;/version&gt;
 &lt;/dependency>
</code-block>
</tab>
<tab title="Gradle (Kotlin)">
<code-block lang="gradle">
dependencies {
    implementation("net.jterminal:jterminal-core:%version%")
 }
</code-block>
</tab>
<tab title="Gradle (Groovy)">
<code-block lang="gradle">
dependencies {
    implementation "net.jterminal:jterminal-core:%version%"
 }
</code-block>
</tab>
</tabs>

### Terminal instances

<code-block lang="java">
Terminal terminal = Terminal.auto();
</code-block>
With <code>Terminal.auto()</code> a new terminal instance will be created and set by automatically provider. 
There are two types of providers that can be selected automatically. The native provider, which can only be executed
natively, and the non-native provider, which is selected for a system without native support.
All created instances have their own properties and retain these for the entire runtime and can be changed as desired.
<br/>
You can create a terminal instance with <code>Terminal.create(provider type...)</code> and 
set with <code>Terminal.set(instance...)</code>. Old instances can also be reinstated, but only one instance can be
active.

#### Example
<code-block lang="java">
Terminal terminal = Terminal.create(NativeTerminalProvider.class);
Terminal.set(terminal);
terminal.title("Native Terminal");
terminal.writeLine("Hello World!");
</code-block>

All standard providers: <code>NativeTerminalProvider, NonNativeTerminalProvider</code>