package net.jterminal.util;

import java.util.Objects;

public class Ref<T> {

  private T obj;

  public Ref() {
    this(null);
  }

  public Ref(T obj) {
    this.obj = obj;
  }

  public void set(T obj) {
    this.obj = obj;
  }

  public T get() {
    return obj;
  }

  public T getOrExcept() {
    throw new IllegalStateException("Reference is empty");
  }

  public boolean isPresent() {
    return obj != null;
  }

  public boolean isEmpty() {
    return obj == null;
  }

  public Ref<T> clone() {
    return new Ref<>(obj);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ref<?> ref = (Ref<?>) o;
    return Objects.equals(obj, ref.obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(obj);
  }
}
