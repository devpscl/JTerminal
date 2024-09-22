package net.jterminal.test;

import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.system.SystemInfo;
import net.jterminal.text.ColorNamePalette;
import net.jterminal.text.XtermColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.style.TextStyle.FontOption;

public class Main extends TestUnit {

  public static void main(String[] args)
      throws TerminalInitializeException, TerminalProviderException {
    System.out.println(SystemInfo.current().toString());
    Terminal terminal = Terminal.auto();
    Terminal.set(terminal);
    TerminalBuffer buffer = new TerminalBuffer()
        .append("Test: ")
        .foregroundColor(XtermColor.COLOR_49)
        .append("Hello ")
        .backgroundColor(XtermColor.COLOR_55)
        .append("World!")
        .backgroundColor(null)
        .append(" ")
        .fonts(TextFont.UNDERLINE)
        .append("and")
        .appendFonts(TextFont.STRIKE)
        .append(" strike ")
        .removeFonts(TextFont.UNDERLINE)
        .append("this")
        .newLine()
        .append("X")
        .appendStyle(TextStyle.create().foregroundColor(ColorNamePalette.AQUAMARINE).font(
            FontOption.UNSET, TextFont.STRIKE))
        .append(" Test!")
        .resetStyle();
    terminal.writeLine(buffer);
    terminal.writeLine("Size: " + (buffer.toString().getBytes().length / 1024D));
  }

}
