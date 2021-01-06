package de.lacodev.rsystem.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IpUnBanPlayerEvent extends Event {
    private HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private String playerName;
    public IpUnBanPlayerEvent(String playername) {
        this.playerName = playername;
    }

    /**
     * @return the PlayerName of the UnBanPlayer
     * */
    public String getPlayerName() {
        return playerName;
    }
}
