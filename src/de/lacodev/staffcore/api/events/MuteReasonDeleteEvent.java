package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MuteReasonDeleteEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private String name;

  public MuteReasonDeleteEvent(String name) {
    this.name = name;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public String getName() {
    return this.name;
  }
}
