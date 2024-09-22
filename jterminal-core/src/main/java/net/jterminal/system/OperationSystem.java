package net.jterminal.system;

import org.jetbrains.annotations.NotNull;

public enum OperationSystem {
  WINDOWS("win"),
  LINUX("lin"),
  MACOS("macos"),
  UNKNOWN("");

  private final String name;

  OperationSystem(final String name) {
    this.name = name;
  }

  public @NotNull String id() {
      return name;
  }

}
