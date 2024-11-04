package net.jterminal.text.termstring;

import net.jterminal.text.Combiner;
import net.jterminal.text.element.TextElement;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;

class Element2StringConverter {
  
  public static @NotNull TermString convert(@NotNull TextElement textElement) {
    UnsafeTermStringBuilder builder = (UnsafeTermStringBuilder) TermString.builder();
    convert(textElement, TextStyle.create(), builder);
    return builder.getNoCopy();
  }

  private static void convert(@NotNull TextElement textElement,
      @NotNull TextStyle textStyle, @NotNull TermStringBuilder builder) {
    TextStyle newStyle = Combiner.combine(textElement.style(), textStyle);
    builder.appendExplicitStyle(newStyle);
    builder.append(textElement.value());
    for (TextElement childElement : textElement.child()) {
      convert(childElement, newStyle , builder);
    }
  }

}
