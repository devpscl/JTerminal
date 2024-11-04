package net.jterminal.event.bus;

import net.jterminal.event.Event;
import net.jterminal.exception.EventException;

public interface EventListener<T extends Event> {

  void onEvent(T event) throws EventException;

}
