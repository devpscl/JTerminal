package net.jterminal.ui.component.selectable;

import net.jterminal.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class PasswordFieldComponent extends TextFieldComponent {

  public PasswordFieldComponent() {
  }

  @Override
  public @NotNull String displayValue() {
    int length = super.displayValue().length();
    return StringUtil.repeat('*', length);
  }

}
