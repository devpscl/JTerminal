package net.jterminal.test;

import java.time.Duration;
import net.jterminal.input.InputEvent;
import net.jterminal.input.KeyboardInputEvent;
import net.jterminal.input.MouseInputEvent;
import net.jterminal.input.WindowInputEvent;
import org.jetbrains.annotations.NotNull;

public class TestUnit {

  public static void print(Object x, Object...objects) {
    System.out.printf(String.valueOf(x), objects);
  }

  public static void println(Object x, Object...objects) {
    System.out.printf((x) + "%n", objects);
  }

  public static @NotNull String unescapeString(@NotNull String s) {
    return s.replaceAll("\u001b", "ESC");
  }

  public static void print(@NotNull InputEvent event) {
    switch (event.type()) {
      case KEYBOARD -> {
        KeyboardInputEvent e = (KeyboardInputEvent) event;
        char input = e.input();
        int key = e.key();
        String state = e.state().name();
        String raw = unescapeString(e.raw());
        println("""
            InputEvent (Keyboard):
              Input: %c
              Key:   %d
              State: %s
              Raw:   %s
            
            """, input, key, state, raw);
      }
      case MOUSE -> {
        MouseInputEvent e = (MouseInputEvent) event;
        String button = e.button().name();
        String action = e.action().name();
        String pos = e.terminalPosition().toString();
        String raw = unescapeString(e.raw());
        println("""
            InputEvent (Mouse):
              Button: %s
              Action: %s
              Pos:    %s
              Raw:    %s
            
            """, button, action, pos, raw);
      }
      case WINDOW -> {
        WindowInputEvent e = (WindowInputEvent) event;
        String oldDim = e.oldDimension().toString();
        String newDim = e.newDimension().toString();
        String raw = unescapeString(e.raw());
        println("""
            InputEvent (Window):
              Old: %s
              New: %s
              Raw: %s
            
            """, oldDim, newDim, raw);
      }
      case UNKNOWN -> {
        String raw = unescapeString(event.raw());
        println("""
            InputEvent (Unknown):
              Raw: %s
            
            """, raw);
      }
    }
  }

  public static void sleep(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static class StopWatch {

    private long startTime = 0;
    private long stopTime = 0;

    public void start() {
      startTime = System.currentTimeMillis();
    }

    public void stop() {
      stopTime = System.currentTimeMillis();
    }

    public void reset() {
      stopTime = 0;
      startTime = 0;
    }

    public Duration time() {
      return Duration.ofMillis(stopTime - startTime);
    }

    public void print(@NotNull String name) {
      println("[" + name + "] Time: %d ms", time().toMillis());
    }

  }

}
