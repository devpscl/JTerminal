package net.jterminal.util;

import org.jetbrains.annotations.NotNull;

public class SetLock<T> {

  private T object;
  private final @NotNull String name;

  public SetLock(@NotNull String name) {
    this.name = name;
  }

  public boolean isSet() {
    return object != null;
  }

  public void set(@NotNull T value) {
    if(object == value) {
      return;
    }
    if(object != null) {
      throw new IllegalStateException(name + " is already set");
    }
    this.object = value;
  }

  public @NotNull T get() {
    if(object == null) {
      throw new IllegalStateException(name + " is not set");
    }
    return object;
  }

}
