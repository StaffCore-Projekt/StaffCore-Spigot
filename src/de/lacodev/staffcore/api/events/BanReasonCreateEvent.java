package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BanReasonCreateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private String name;

    private String unit;

    private int length;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public BanReasonCreateEvent(String name, String unit, int length) {
        this.name = name;
        this.unit = unit;
        this.length = length;
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
