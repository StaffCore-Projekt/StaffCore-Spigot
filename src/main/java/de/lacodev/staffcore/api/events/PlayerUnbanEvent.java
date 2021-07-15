package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUnbanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String uuid;

    public PlayerUnbanEvent(String uuid) {
        this.uuid = uuid;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getUuid() {
        return this.uuid;
    }
}
