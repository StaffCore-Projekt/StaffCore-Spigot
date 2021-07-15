package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MuteReasonCreateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String name;

    private final String unit;

    private final int length;

    public MuteReasonCreateEvent(String name, String unit, int length) {
        this.name = name;
        this.unit = unit;
        this.length = length;
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

    public String getUnit() {
        return this.unit;
    }

    public int getLength() {
        return this.length;
    }
}
