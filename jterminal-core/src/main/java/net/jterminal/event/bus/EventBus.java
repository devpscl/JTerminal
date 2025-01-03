package net.jterminal.event.bus;

import net.jterminal.event.Event;
import org.jetbrains.annotations.NotNull;

public interface EventBus {

  <T extends Event> @NotNull EventBus subscribe(@NotNull Class<T> type,
      @NotNull EventListener<T> listener);

  @NotNull EventBus register(@NotNull Object eventHandlerObject);

  @NotNull EventBus post(@NotNull Event event);

  @NotNull EventBus postAsync(@NotNull Event event);

  @NotNull EventBus unregister(@NotNull EventListener<?> listener);

  @NotNull EventBus unregister(@NotNull Class<? extends Event> type);

  static @NotNull EventBus create() {
    return new EventBusImpl();
  }

}
