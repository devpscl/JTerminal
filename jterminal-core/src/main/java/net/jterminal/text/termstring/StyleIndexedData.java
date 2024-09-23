package net.jterminal.text.termstring;

import java.util.List;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;

public interface StyleIndexedData {

  @NotNull TextStyle at(int index);

  @NotNull List<IndexEntry> indexes();

  record IndexEntry(int index, TextStyle textStyle) {

    IndexEntry shift(int offset) {
      return new IndexEntry(index + offset, textStyle);
    }

  }

}
