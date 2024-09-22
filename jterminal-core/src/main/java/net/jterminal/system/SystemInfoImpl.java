package net.jterminal.system;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class SystemInfoImpl implements SystemInfo {

  protected static SystemInfoImpl instance = null;

  private final OperationSystem operationSystem;
  private final ProcessorArchitecture processorArchitecture;
  private final int bitMode;

  public SystemInfoImpl(OperationSystem operationSystem,
      ProcessorArchitecture processorArchitecture, int bitMode) {
    this.operationSystem = operationSystem;
    this.processorArchitecture = processorArchitecture;
    this.bitMode = bitMode;
  }

  @Override
  public @NotNull OperationSystem os() {
    return operationSystem;
  }

  @Override
  public @NotNull ProcessorArchitecture arch() {
    return processorArchitecture;
  }

  @Override
  public int bitMode() {
    return bitMode;
  }

  @Override
  public boolean is64Bit() {
    return bitMode == 64;
  }

  @Override
  public @NotNull String dynamicLibFileEnd() {
    return switch (operationSystem) {
      case WINDOWS -> "dll";
      case LINUX -> "so";
      case MACOS -> "dylib";
      default -> "obj";
    };
  }

  public boolean equals(@NotNull SystemInfoImpl systemInfoImpl) {
    return operationSystem == systemInfoImpl.operationSystem &&
        processorArchitecture == systemInfoImpl.processorArchitecture &&
        bitMode == systemInfoImpl.bitMode;
  }

  @Override
  public boolean equals(@NotNull SystemInfo other) {
    return equals((SystemInfoImpl) other);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemInfoImpl that = (SystemInfoImpl) o;
    return bitMode == that.bitMode && operationSystem == that.operationSystem
        && processorArchitecture == that.processorArchitecture;
  }

  @Override
  public int hashCode() {
    return Objects.hash(operationSystem.ordinal(),
        processorArchitecture.ordinal(),
        bitMode);
  }

  @Override
  public @NotNull String sysId() {
    return Integer.toHexString(hashCode());
  }

  @Override
  public String toString() {
    return "SystemInfoImpl{" +
        "os=" + operationSystem.name() +
        ", arch=" + processorArchitecture.name() +
        ", bit=" + bitMode +
        '}';
  }
}
