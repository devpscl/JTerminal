package net.jterminal.ui.dialog;

import net.jterminal.text.TerminalColor;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.layout.Layout;
import net.jterminal.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageDialog extends ResultDialog {

  public enum MessageType {
    INFO("Info", null),
    WARN("Warning", TerminalColor.DARK_YELLOW),
    ERROR("Error", TerminalColor.RED);

    private final String title;
    private final TerminalColor color;

    MessageType(@NotNull String title, @Nullable TerminalColor color) {
      this.title = title;
      this.color = color;
    }

    public @NotNull String title() {
      return title;
    }

    public @Nullable TerminalColor color() {
      return color;
    }
  }

  public MessageDialog() {
    this("Info");
  }

  public MessageDialog(@NotNull String title) {
    this(title, "");
  }

  public MessageDialog(@NotNull String title, @NotNull String message) {
    this(title, TermString.value(message));
  }

  public MessageDialog(@NotNull MessageType messageType) {
    this(messageType.title());
  }

  public MessageDialog(@NotNull MessageType messageType, @NotNull String message) {
    this(messageType.title(), TermString.createWithForeground(message, messageType.color()));
  }

  public MessageDialog(@NotNull String title, @NotNull TermString message) {
    super(title, OptionType.OK_TYPE, 0);
    message(message);
    setSize(0.5F, 0.2F, StringUtil.countOfChar(message.raw(), '\n'));
  }

  protected void setSize(float widthScale, float heightScale, int lines) {
    width(Layout.fill(), Layout.scale(widthScale));
    height(Layout.fill(), Layout.scale(heightScale), Layout.min(6 + lines));
  }


}
