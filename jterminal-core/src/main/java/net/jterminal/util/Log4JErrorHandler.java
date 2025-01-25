package net.jterminal.util;

import net.devpscl.eventbus.Event;
import net.devpscl.eventbus.util.ErrorHandler;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Log4JErrorHandler implements ErrorHandler {

  private final Logger logger;

  public Log4JErrorHandler(@NotNull Logger logger) {
    this.logger = logger;
  }

  @Override
  public void handleError(@NotNull Throwable throwable, @NotNull Event event) {
    logger.error("Error at event " + event.eventName(), throwable);
  }
}
