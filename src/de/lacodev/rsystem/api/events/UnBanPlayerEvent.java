package de.lacodev.rsystem.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class UnBanPlayerEvent extends Event {
    private HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private String uuid;
    public UnBanPlayerEvent(String uuid) {
        this.uuid = uuid;
    }


    /**
     * @return the UUID of the Player witch got unbanned
     * */
    public String getUuid() {
        return uuid;
    }
}
