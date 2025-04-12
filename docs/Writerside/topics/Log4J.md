# Log4J

There is a Log4j appender to add an error screen.

Example configuration:

<code-block lang="xml">
&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
&lt;Configuration packages=&quot;net.jterminal.ui.log4j&quot;&gt;
  &lt;Appenders&gt;
    &lt;ErrorScreenAppender name=&quot;ErrorScreenAppender&quot;/&gt;
  &lt;/Appenders&gt;
  &lt;Loggers&gt;
    &lt;Root level=&quot;ERROR&quot;&gt;
      &lt;AppenderRef ref=&quot;ErrorScreenAppender&quot;/&gt;
    &lt;/Root&gt;
  &lt;/Loggers&gt;
&lt;/Configuration&gt;
</code-block>