package net.jterminal.text.termstring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;

class TermStringJoinerImpl implements TermStringJoiner {

  private final List<TermString> list = new ArrayList<>();
  private TermString delimiter = TermString.empty();

  @Override
  public @NotNull TermStringJoiner addAll(@NotNull TermString... value) {
    list.addAll(Arrays.asList(value));
    return this;
  }

  @Override
  public @NotNull TermStringJoiner addAll(@NotNull Collection<TermString> value) {
    list.addAll(value);
    return this;
  }

  @Override
  public @NotNull TermStringJoiner addAllStrings(@NotNull String... value) {
    for (String str : value) {
      list.add(TermString.value(str));
    }
    return this;
  }

  @Override
  public @NotNull TermStringJoiner addAllStrings(@NotNull Collection<String> value) {
    for (String str : value) {
      list.add(TermString.value(str));
    }
    return this;
  }

  @Override
  public @NotNull TermStringJoiner add(@NotNull TermString value) {
    list.add(value);
    return this;
  }

  @Override
  public @NotNull TermStringJoiner add(@NotNull String value) {
    return add(TermString.value(value));
  }

  @Override
  public @NotNull TermStringJoiner add(@NotNull Object value) {
    return add(TermString.value(value));
  }

  @Override
  public @NotNull TermStringJoiner delimiter(@NotNull TermString value) {
    this.delimiter = value;
    return this;
  }

  @Override
  public @NotNull TermStringJoiner delimiter(@NotNull String value) {
    return delimiter(TermString.value(value));
  }

  @Override
  public @NotNull TermStringJoiner delimiter(@NotNull Object value) {
    return delimiter(TermString.value(value));
  }

  @Override
  public @NotNull TermString build() {
    TermStringBuilder builder = TermString.builder();
    for (int idx = 0; idx < list.size(); idx++) {
      if(idx > 0) {
        builder.append(delimiter);
      }
      builder.append(list.get(idx));
    }
    return builder.build();
  }

}
