package de.lacodev.rsystem.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class BanReasonCreateEvent extends Event {

    private HandlerList handlers = new HandlerList();

    private String name;
    private String unit;
    private int length;

    public BanReasonCreateEvent(String name, String unit, int length) {
        this.name = name;
        this.unit = unit;
        this.length = length;
    }

    /**
     * Gets the Reason Name
     *
     * @return Reason Name
     * */
    public String getName() {
        return name;
    }

    /**
     * Gets the Unit of the ban time
     *
     * @return d = days | h = hours | m = minutes | perma
     * */
    public String getUnit() {
        return unit;
    }

    /**
     * Gets the Length of the ban time
     *
     * @return the Time (in d | h | m )
     * */
    public int getLength() {
        return length;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
