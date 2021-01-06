package de.lacodev.rsystem.api;

/**
 * Main API
 *
 * Examples: https://www.github.com/ViaEnder/StaffCore-Examples
 *
 * @author ViaEnder
 * @version 1.0.0
 *
 * */
public class StaffcoreAPI {


    /**
     * To get all about the Bans
     *
     * @return Ban Class
     * */
    public API_Bans Ban(){
        return new API_Bans();
    }

    /**
     * All about Mutes
     * @return Mute Class
     */
    public API_Mute Mute(){
        return new API_Mute();
    }

    /**
     *
     * To get all about the Reports
     *
     * @return All About Reports
     * */
    public API_Reports Report() {
        return new API_Reports();
    }

    /**
     *
     * To get the Utils
     *
     * @return Utils
     * */
    public API_System Utils() {
        return new API_System();
    }

    /**
     * All About the warns
     * @return All about the Warns
     */
    public API_Warn Warn(){
        return new API_Warn();
    }



}
