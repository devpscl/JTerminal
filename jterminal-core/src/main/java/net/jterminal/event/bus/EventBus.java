package net.jterminal.event.bus;

import net.jterminal.event.Event;
import org.jetbrains.annotations.NotNull;

public interface EventBus {

  @NotNull EventBus subscribe(@NotNull Class<? extends Event> type,
      @NotNull EventListener<?> listener);

  @NotNull EventBus register(@NotNull Object eventHandlerObject);

  @NotNull EventBus post(@NotNull Event event);

  @NotNull EventBus postAsync(@NotNull Event event);

  @NotNull EventBus unregister(@NotNull EventListener<?> listener);

  @NotNull EventBus unregister(@NotNull Class<? extends Event> type);

  @NotNull EventBus unregisterAll();

  static @NotNull EventBus create() {
    return new EventBusImpl();
  }

}
