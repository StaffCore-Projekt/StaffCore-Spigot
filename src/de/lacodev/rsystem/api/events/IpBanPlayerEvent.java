package de.lacodev.rsystem.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IpBanPlayerEvent extends Event {
    private HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private String team;
    private Player target;

    public IpBanPlayerEvent(Player target, String team) {
        this.target = target;
        this.team = team;
    }

    /**
     * @return The Name of the Player who banned the Target
     * */
    public String getTeam() {
        return team;
    }

    /**
     * @return The Target who got banned
     * */
    public Player getTarget() {
        return target;
    }
}
