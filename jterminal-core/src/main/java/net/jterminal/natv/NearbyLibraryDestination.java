package net.jterminal.natv;

import java.io.File;
import net.jterminal.Terminal;
import net.jterminal.system.SystemInfo;
import org.jetbrains.annotations.NotNull;

public class NearbyLibraryDestination implements LibraryDestination {

  private final File directory;

  public NearbyLibraryDestination() {
    this(new File("libs"));
  }

  public NearbyLibraryDestination(@NotNull File directory) {
    this.directory = directory;
  }

  @Override
  public @NotNull File getDestination(@NotNull NativeLibrary nativeLibrary) {
    if(!directory.exists()) {
      if(!directory.mkdir()) {
        Terminal.LOGGER.error("Cannot create library directory: {}",
            directory.getName());
      }
    }
    String filename = nativeLibrary.name().toLowerCase()
        + "." + SystemInfo.current().dynamicLibFileEnd();
    return new File(directory, filename);
  }
}
