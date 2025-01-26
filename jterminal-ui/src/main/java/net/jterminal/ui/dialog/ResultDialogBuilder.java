package net.jterminal.ui.dialog;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.dialog.ResultDialog.OptionType;
import net.jterminal.ui.layout.Layout;
import org.jetbrains.annotations.NotNull;

public class ResultDialogBuilder {

  private String title;
  private TermString message;
  private ResultDialog.OptionType optionType;
  private float widthScale = 0.5F;
  private float heightScale = 0.2F;
  private final Map<Integer, Consumer<ResultDialog>> eventMap = new HashMap<>();

  public @NotNull String title() {
    return title;
  }

  public @NotNull ResultDialogBuilder title(String title) {
    this.title = title;
    return this;
  }

  public @NotNull TermString message() {
    return message;
  }

  public @NotNull ResultDialogBuilder message(@NotNull TermString message) {
    this.message = message;
    return this;
  }

  public @NotNull OptionType optionType() {
    return optionType;
  }

  public @NotNull ResultDialogBuilder optionType(@NotNull OptionType optionType) {
    this.optionType = optionType;
    return this;
  }

  public float widthScale() {
    return widthScale;
  }

  public @NotNull ResultDialogBuilder widthScale(float scale) {
    this.widthScale = scale;
    return this;
  }

  public float heightScale() {
    return heightScale;
  }

  public @NotNull ResultDialogBuilder heightScale(float scale) {
    this.heightScale = scale;
    return this;
  }

  public @NotNull ResultDialogBuilder event(int button, @NotNull Consumer<ResultDialog> event) {
    eventMap.put(button, event);
    return this;
  }

  public @NotNull ResultDialog build() {
    final ResultDialog dialog = new ResultDialog(title, optionType);
    dialog.message(message);
    dialog.width(Layout.fill(), Layout.scale(widthScale));
    dialog.height(Layout.fill(), Layout.scale(heightScale), Layout.min(5));
    dialog.action(intValue -> {
      Consumer<ResultDialog> resultDialogConsumer = eventMap.get(intValue);
      if(resultDialogConsumer != null) {
        resultDialogConsumer.accept(dialog);
      }
    });
    return dialog;
  }

}
