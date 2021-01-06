package de.lacodev.rsystem.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class TicketCreateEvent extends Event {
    private HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private Player p;
    public TicketCreateEvent(Player player) {
        this.p = player;
    }

    /**
     * @return Player that creates a Ticket.
     * */
    public Player getPlayer() {
        return p;
    }
}
