package net.jterminal.system;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface SystemInfo {

  @NotNull OperationSystem os();

  @NotNull ProcessorArchitecture arch();

  int bitMode();

  boolean is64Bit();

  @NotNull String dynamicLibFileEnd();

  boolean equals(@NotNull SystemInfo other);

  int hashCode();

  @NotNull String sysId();

  static boolean isSet() {
    return SystemInfoImpl.instance != null;
  }

  static @NotNull SystemInfo current() {
    if(SystemInfoImpl.instance == null) {
      return scan();
    }
    return SystemInfoImpl.instance;
  }

  static @NotNull SystemInfo create(@NotNull OperationSystem operationSystem,
      @NotNull ProcessorArchitecture processorArchitecture) {
    return create(operationSystem, processorArchitecture,
        processorArchitecture.is64Bit() ? 64 : 32);
  }

  static @NotNull SystemInfo create(@NotNull OperationSystem operationSystem,
      @NotNull ProcessorArchitecture processorArchitecture,
      int bitMode) {
    return new SystemInfoImpl(operationSystem,
        processorArchitecture, bitMode);
  }

  static @NotNull SystemInfo scan() {
    OperationSystem operationSystem = SystemScanner.detectOperationSystem();
    ProcessorArchitecture processorArchitecture = SystemScanner.detectArchitecture();
    Optional<Integer> bitModeOpt = SystemScanner.detectBitMode();
    int bitMode = bitModeOpt.orElse(64);
    SystemInfoImpl systemInfo
        = new SystemInfoImpl(operationSystem, processorArchitecture, bitMode);
    SystemInfoImpl.instance = systemInfo;
    return systemInfo;
  }

}
