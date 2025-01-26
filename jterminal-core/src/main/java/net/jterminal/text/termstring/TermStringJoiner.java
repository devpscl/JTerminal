package net.jterminal.text.termstring;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface TermStringJoiner {

  @NotNull TermStringJoiner addAll(@NotNull TermString...value);

  @NotNull TermStringJoiner addAll(@NotNull Collection<TermString> value);

  @NotNull TermStringJoiner addAllStrings(@NotNull String...value);

  @NotNull TermStringJoiner addAllStrings(@NotNull Collection<String> value);

  @NotNull TermStringJoiner add(@NotNull TermString value);

  @NotNull TermStringJoiner add(@NotNull String value);

  @NotNull TermStringJoiner add(@NotNull Object value);

  @NotNull TermStringJoiner delimiter(@NotNull TermString value);

  @NotNull TermStringJoiner delimiter(@NotNull String value);

  @NotNull TermStringJoiner delimiter(@NotNull Object value);

  @NotNull TermString build();

}
