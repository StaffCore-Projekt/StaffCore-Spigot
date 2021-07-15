package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerIPAdressUnBanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String playername;

    public PlayerIPAdressUnBanEvent(String playername) {
        this.playername = playername;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getPlayername() {
        return this.playername;
    }
}
