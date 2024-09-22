package net.jterminal.util;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StreamConfig {

  private boolean nonBlocking = false;
  private Duration timeout = null;

  public StreamConfig() {}

  private StreamConfig(boolean nonBlocking, Duration timeout) {
    this.nonBlocking = nonBlocking;
    this.timeout = timeout;
  }

  public @NotNull StreamConfig nonBlocking(boolean nonBlocking) {
    this.nonBlocking = nonBlocking;
    return this;
  }

  public boolean nonBlocking() {
    return nonBlocking;
  }

  public @NotNull StreamConfig timeout(@Nullable Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  public @Nullable Duration timeout() {
    return timeout;
  }

  public @NotNull StreamConfig clone() {
    return new StreamConfig(nonBlocking, timeout);
  }


}
