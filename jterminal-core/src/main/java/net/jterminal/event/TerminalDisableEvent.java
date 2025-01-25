package net.jterminal.event;

import net.devpscl.eventbus.Event;
import net.jterminal.Terminal;
import org.jetbrains.annotations.NotNull;

public record TerminalDisableEvent(@NotNull Terminal terminal) implements Event {}
