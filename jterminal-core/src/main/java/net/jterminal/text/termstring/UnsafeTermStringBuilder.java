package net.jterminal.text.termstring;

import org.jetbrains.annotations.NotNull;

public interface UnsafeTermStringBuilder extends TermStringBuilder {

  @NotNull TermString getNoCopy();

  @NotNull StringBuilder stringBuilder();

  @NotNull IndexedStyleData data();

}
