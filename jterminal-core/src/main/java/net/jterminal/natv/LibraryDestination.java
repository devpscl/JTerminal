package net.jterminal.natv;

import java.io.File;
import org.jetbrains.annotations.NotNull;

public interface LibraryDestination {

  @NotNull File getDestination(@NotNull NativeLibrary nativeLibrary);

}
