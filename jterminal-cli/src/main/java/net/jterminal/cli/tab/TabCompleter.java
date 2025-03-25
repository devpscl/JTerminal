package net.jterminal.cli.tab;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TabCompleter {

  @Nullable TabCompletion generate(@NotNull String input, int cursor);

}
