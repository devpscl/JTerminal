package net.jterminal.event.bus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.Terminal;
import net.jterminal.annotation.SubscribeEvent;
import net.jterminal.event.Event;
import net.jterminal.exception.EventException;
import org.jetbrains.annotations.NotNull;

class EventBusImpl implements EventBus {

  private final Map<Class<? extends Event>, List<EventListener<?>>> map = new HashMap<>();
  private final ReentrantLock syncLock = new ReentrantLock();
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();

  @SuppressWarnings("unchecked")
  private <T extends Event> void call(EventListener<T> listener, Event event) {
    try {
      listener.onEvent((T) event);
    } catch (EventException e) {
      Terminal.LOGGER.error("Error at event {}", event.getClass().getSimpleName(), e);
    }
  }

  @Override
  public @NotNull <T extends Event> EventBus subscribe(@NotNull Class<T> type,
      @NotNull EventListener<T> listener) {
    return subscribeNonGeneric(type, listener);
  }

  private @NotNull EventBus subscribeNonGeneric(
      @NotNull Class<? extends Event> type, @NotNull EventListener<?> listener) {
    syncLock.lock();
    try {
      List<EventListener<?>> eventListeners = map.getOrDefault(type, new ArrayList<>());
      eventListeners.add(listener);
      map.put(type, eventListeners);
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull EventBus register(@NotNull Object eventHandlerObject) {
    Class<?> clazz = eventHandlerObject.getClass();
    for (Method method : clazz.getMethods()) {
      Class<?>[] parameterTypes = method.getParameterTypes();
      if(parameterTypes.length != 1) {
        continue;
      }
      if(!method.isAnnotationPresent(SubscribeEvent.class)) {
        continue;
      }
      if(!Event.class.isAssignableFrom(parameterTypes[0])) {
        continue;
      }
      Class<? extends Event> eventType = parameterTypes[0].asSubclass(Event.class);
      subscribeNonGeneric(eventType, new ReflectEventListener(eventHandlerObject, method));
    }
    return this;
  }

  @Override
  public @NotNull EventBus post(@NotNull Event event) {
    syncLock.lock();
    try {
      List<EventListener<?>> eventListeners = map.get(event.getClass());
      if(eventListeners != null) {
        for (EventListener<?> eventListener : eventListeners) {
          call(eventListener, event);
        }
      }
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull EventBus postAsync(@NotNull Event event) {
    syncLock.lock();
    try {
      executorService.submit(() -> {
        List<EventListener<?>> eventListeners = map.get(event.getClass());
        if(eventListeners == null) {
          return;
        }
        for (EventListener<?> eventListener : new ArrayList<>(eventListeners)) {
          call(eventListener, event);
        }
      });
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull EventBus unregister(@NotNull EventListener<?> listener) {
    syncLock.lock();
    try {
      for (Class<? extends Event> eventType : map.keySet()) {
        List<EventListener<?>> eventListeners = map.get(eventType);
        eventListeners.remove(listener);
      }
    } finally {
      syncLock.unlock();
    }
    return this;
  }

  @Override
  public @NotNull EventBus unregister(@NotNull Class<? extends Event> type) {
    syncLock.lock();
    try {
      map.remove(type);
    } finally {
      syncLock.unlock();
    }
    return this;
  }

}
