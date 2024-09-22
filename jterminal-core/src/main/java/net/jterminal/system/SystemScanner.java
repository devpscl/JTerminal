package net.jterminal.system;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

class SystemScanner {

  private static final String PROP_OS_NAME = "os.name";
  private static final String PROP_OS_ARCH = "os.arch";
  private static final String PROP_BIT_MODE = "com.ibm.vm.bitmode";

  public static @NotNull Optional<Integer> detectBitMode() {
    try {
      return Optional.of(Integer.parseInt(System.getProperty(PROP_BIT_MODE)));
    } catch(NumberFormatException e) {
      return Optional.empty();
    }
  }

  public static @NotNull OperationSystem detectOperationSystem() {
    String propertyValue = System.getProperty(PROP_OS_NAME);
    for (OperationSystem operationSystem : OperationSystem.values()) {
      if(propertyValue.toLowerCase().startsWith(operationSystem.id())) {
        return operationSystem;
      }
    }
    return OperationSystem.UNKNOWN;
  }

  public static @NotNull ProcessorArchitecture detectArchitecture() {
    String propertyValue = System.getProperty(PROP_OS_ARCH);
    for (ProcessorArchitecture architecture : ProcessorArchitecture.values()) {
      if(propertyValue.equalsIgnoreCase(architecture.mnemonic())) {
        return architecture;
      }
    }
    return ProcessorArchitecture.UNKNOWN;
  }

}
