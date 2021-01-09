package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerIPAdressUnBanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private String playername;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerIPAdressUnBanEvent(String playername) {
        this.playername = playername;
    }

    public String getPlayername() {
        return this.playername;
    }
}
