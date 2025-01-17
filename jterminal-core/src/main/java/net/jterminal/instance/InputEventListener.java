package net.jterminal.instance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.Terminal;
import net.jterminal.input.InputEvent;
import net.jterminal.input.TerminalInput;
import org.jetbrains.annotations.NotNull;

class InputEventListener {

  private final ExecutorService executorService = Executors.newSingleThreadExecutor();
  private final AtomicBoolean paused = new AtomicBoolean(true);
  private final TerminalInput terminalInput;
  private volatile boolean stopped = false;
  private boolean started = false;
  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

  public InputEventListener(int capacity) {
    terminalInput = TerminalInput.create(capacity);
  }

  public void start() {
    if(started) {
      return;
    }
    executorService.submit(this::run);
    started = true;
  }

  public void pause() {
    lock.lock();
    try {
      paused.set(true);
      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public void resume() {
    lock.lock();
    try {
      paused.set(false);
      condition.signal();
    } finally {
      lock.unlock();
    }
  }

  private void run() {
    try {
      while(!stopped) {
        while (paused.get()) {
          if(stopped) {
            break;
          }
          condition.await();
        }
        if(stopped) {
          break;
        }
        InputEvent event = terminalInput.readEvent();
        post(event);
      }
    } catch (InterruptedException ex) {
      Terminal.LOGGER.catching(ex);
    }
  }

  private void post(@NotNull InputEvent inputEvent) {
    Terminal.get().eventBus().post(inputEvent);
  }

  public void stop() {
    lock.lock();
    try {
      stopped = true;
      condition.signalAll();
      terminalInput.close();
    } finally {
      lock.unlock();
    }
  }

}
