package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BanReasonDeleteEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private String reason;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public BanReasonDeleteEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }
}
