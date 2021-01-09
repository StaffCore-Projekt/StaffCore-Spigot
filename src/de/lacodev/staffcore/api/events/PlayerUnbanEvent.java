package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUnbanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private String uuid;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerUnbanEvent(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }
}
