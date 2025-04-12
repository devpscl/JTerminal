package net.jterminal.ui.dialog;

import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.layout.Layout;
import net.jterminal.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class InfoDialog extends ResultDialog {

  public InfoDialog() {
    this("Info");
  }

  public InfoDialog(@NotNull String title) {
    this(title, "");
  }

  public InfoDialog(@NotNull String title, @NotNull String message) {
    this(title, TermString.value(message));
  }

  public InfoDialog(@NotNull String title, @NotNull TermString message) {
    super(title, OptionType.OK_TYPE, 0);
    message(message);
    setSize(0.5F, 0.2F, StringUtil.countOfChar(message.raw(), '\n'));
  }

  protected void setSize(float widthScale, float heightScale, int lines) {
    width(Layout.fill(), Layout.scale(widthScale));
    height(Layout.fill(), Layout.scale(heightScale), Layout.min(6 + lines));
  }


}
