package net.jterminal.ui.log4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import net.jterminal.NativeTerminal.BufferId;
import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.UITerminal;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(name="ErrorScreenAppender", category = Core.CATEGORY_NAME,
    elementType = Appender.ELEMENT_TYPE, printObject = true)
public class ErrorScreenAppender extends AbstractAppender {

  protected ErrorScreenAppender(String name, Filter filter) {
    super("ErrorScreenAppender", null, null,
        false, Property.EMPTY_ARRAY);
  }

  @PluginFactory
  public static ErrorScreenAppender createAppender(
      @PluginAttribute("name") String name,
      @PluginElement("Filter") Filter filter) {
    return new ErrorScreenAppender(name, filter);
  }

  @Override
  public void append(LogEvent event) {
    if(!Terminal.isSet()) {
      return;
    }
    Terminal terminal = Terminal.get();
    if(terminal instanceof UITerminal uit) {
      TerminalColor bgc = event.getLevel() == Level.ERROR ? TerminalColor.RED : TerminalColor.YELLOW;
      TerminalColor fgc = TerminalColor.BLACK;
      uit.closeScreen();
      uit.backgroundColor(bgc);
      uit.foregroundColor(fgc);
      uit.setBuffer(BufferId.MAIN);
      uit.resetFlags();
      uit.clear();
      Message message = event.getMessage();
      TerminalBuffer buffer = new TerminalBuffer()
          .foregroundColor(fgc)
          .backgroundColor(bgc)
          .append("[" + event.getLevel().name() + " - " + event.getThreadName() + "]: "
          + message.getFormattedMessage());

      Throwable throwable = message.getThrowable();
      buffer.newLine();
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      buffer.append(stringWriter);
      uit.write(buffer);
    }
  }

}
