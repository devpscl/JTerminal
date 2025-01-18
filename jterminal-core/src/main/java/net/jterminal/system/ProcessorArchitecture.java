package net.jterminal.system;

import org.jetbrains.annotations.NotNull;

public enum ProcessorArchitecture {
  ARCHITECTURE_X86("x86"),
  ARCHITECTURE_X86_64(true, "x86_64", "amd64"),
  ARCHITECTURE_ARM("arm"),
  ARCHITECTURE_ARM64(true, "aarch64", "arm64"),
  ARCHITECTURE_PPC("ppc"),
  ARCHITECTURE_PPC64(true, "ppc64"),
  ARCHITECTURE_SPARC("sparc"),
  ARCHITECTURE_SPARCV9(true, "sparcv9"),
  ARCHITECTURE_MIPS("mips"),
  ARCHITECTURE_MIPS64(true, "mips64"),
  ARCHITECTURE_IA64(true, "ia64"),
  UNKNOWN("unknown");

  private final String[] names;
  private final String mnemonic;
  private final boolean is64Bit;

  ProcessorArchitecture(String...names) {
    this(false, names);
  }

  ProcessorArchitecture(boolean is64Bit, String...names) {
    this.mnemonic = names[0];
    this.is64Bit = is64Bit;
    this.names = names;
  }

  public String[] names() {
    return names;
  }

  public boolean is64Bit() {
    return is64Bit;
  }

  public @NotNull String mnemonic() {
    return mnemonic;
  }
}
