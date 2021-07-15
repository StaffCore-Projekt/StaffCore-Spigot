package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BanReasonDeleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String reason;

    public BanReasonDeleteEvent(String reason) {
        this.reason = reason;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getReason() {
        return this.reason;
    }
}
