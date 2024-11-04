package net.jterminal.event.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.jterminal.event.Event;
import net.jterminal.exception.EventException;

public class ReflectEventListener implements EventListener<Event> {

  private final Object source;
  private final Method method;

  public ReflectEventListener(Object source, Method method) {
    this.source = source;
    this.method = method;
  }

  @Override
  public void onEvent(Event event) throws EventException {
    try {
      method.invoke(source, event);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new EventException("Failed to call method " + method.getName(), e);
    }
  }
}
