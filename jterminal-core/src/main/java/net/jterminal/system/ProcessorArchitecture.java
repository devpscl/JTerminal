package net.jterminal.system;

import org.jetbrains.annotations.NotNull;

public enum ProcessorArchitecture {

  ARCHITECTURE_X86("x86"),
  ARCHITECTURE_X86_64("x86_64", true),
  ARCHITECTURE_AMD64("amd64", true),
  ARCHITECTURE_ARM("arm"),
  ARCHITECTURE_AARCH64("aarch64", true),
  ARCHITECTURE_PPC("ppc"),
  ARCHITECTURE_PPC64("ppc64", true),
  ARCHITECTURE_PPC64LE("ppc64le", true),
  ARCHITECTURE_SPARC("sparc"),
  ARCHITECTURE_SPARCV9("sparcv9", true),
  ARCHITECTURE_MIPS("mips"),
  ARCHITECTURE_MIPS64("mips64", true),
  ARCHITECTURE_MIPS64EL("mips64el", true),
  ARCHITECTURE_POWERPC("PowerPC"),
  ARCHITECTURE_IA64("ia64", true),
  UNKNOWN("");

  private final String mnemonic;
  private final boolean is64Bit;

  ProcessorArchitecture(final String mnemonic) {
    this(mnemonic, false);
  }

  ProcessorArchitecture(final String mnemonic, boolean is64Bit) {
    this.mnemonic = mnemonic;
    this.is64Bit = is64Bit;
  }

  public boolean is64Bit() {
    return is64Bit;
  }

  public @NotNull String mnemonic() {
    return mnemonic;
  }
}
