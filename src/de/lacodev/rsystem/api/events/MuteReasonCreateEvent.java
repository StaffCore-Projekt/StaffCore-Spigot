package de.lacodev.rsystem.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class MuteReasonCreateEvent extends Event {
    private HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    private String name;
    private String unit;
    private int lenght;

    public MuteReasonCreateEvent(String name, String unit, int lenght) {
        this.name = name;
        this.unit = unit;
        this.lenght = lenght;
    }

    /**
     * @return the name of the reason
     * */
    public String getName() {
        return name;
    }

    /**
     * @return d = days | h = hours | m = minutes
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @return the Time (in d | h | m )
     * */
    public int getLenght() {
        return lenght;
    }
}
