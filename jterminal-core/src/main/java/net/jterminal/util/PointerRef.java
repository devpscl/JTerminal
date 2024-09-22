package net.jterminal.util;

import java.util.HexFormat;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public final class PointerRef {

  private long ptr = 0;

  public PointerRef() {

  }

  public PointerRef(long ptr) {
    this.ptr = ptr;
  }

  public PointerRef(@Nullable Long ptr) {
    this.ptr = ptr == null ? 0 : ptr;
  }

  public long get() {
    return ptr;
  }

  public void set(long value) {
    ptr = value;
  }

  public void setNull() {
    ptr = 0;
  }

  public boolean isNull() {
    return ptr == 0;
  }

  public boolean isPresent() {
    return ptr != 0;
  }

  public Optional<Long> opt() {
    return isNull() ? Optional.empty() : Optional.of(ptr);
  }

  public PointerRef clone() {
    return new PointerRef(ptr);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PointerRef that = (PointerRef) o;
    return ptr == that.ptr;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(ptr);
  }

  @Override
  public String toString() {
    return "0x" + HexFormat.of().toHexDigits(ptr);
  }
}
