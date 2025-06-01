package net.jterminal.natv;

import java.io.File;
import net.jterminal.Terminal;
import net.jterminal.system.SystemInfo;
import org.jetbrains.annotations.NotNull;

public class TempDirLibraryDestination implements LibraryDestination {

  private final File tempDir;

  public TempDirLibraryDestination() {
    this("javan");
  }

  public TempDirLibraryDestination(@NotNull String name) {
    File tempDir = new File(System.getProperty("java.io.tmpdir"));
    tempDir = new File(tempDir, name);
    this.tempDir = tempDir;
  }

  @Override
  public @NotNull File getDestination(@NotNull NativeLibrary nativeLibrary) {
    if(!tempDir.exists()) {
      if(!tempDir.mkdir()) {
        Terminal.LOGGER.warn("Failed to create java native directory: {}",
            tempDir.getAbsolutePath());
      }
    }
    SystemInfo systemInfo = SystemInfo.current();
    String sysId = systemInfo.sysId();
    String filename = nativeLibrary.uniqueFileName();
    return new File(tempDir, filename);
  }
}
