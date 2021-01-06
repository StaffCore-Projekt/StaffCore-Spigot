package de.lacodev.rsystem.api;

import de.lacodev.rsystem.errors.PlayerIsProtectedExeption;
import de.lacodev.rsystem.errors.ReasonNotFoundExeption;
import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;
import java.util.ArrayList;

/**
 * @author ViaEnder
 * @version 1.0.0
 */
public class API_Bans {

    /**
     * Creates a Ban Reason!
     * @param reason Name of the reason
     * @param unit time unti (d, h, m, perma)
     * @param lenth the number of "unit" the player is banned
     */
    public void createBanReason(String reason, Unit unit, int lenth) {
        BanManager.createBanReason(reason, unit.toString(), lenth);
    }

    /**
     * check if its a valid id
     * @param id of the player
     * @return true if its exists
     */
    public boolean existsBanID(int id){
        return BanManager.existsBanID(id);
    }

    /**
     * get the Reason ID from the reason
     * @param reason witch reason
     * @return ID
     */
    public Integer getBanReasonID(String reason){
        return BanManager.getIDFromBanReason(reason);
    }

    /**
     * Get the Reason String from the ID
     * @param id the id of the reason
     * @return Reason
     */
    public String getBanReasonReason(int id){
        return BanManager.getBanReasonFromID(id);
    }

    /**
     * Check if the Ban Reason exists
     * @param name The Reason to check
     * @return true if its exists
     */
    public boolean existsBanReason(String name){
        return BanManager.existsBanReason(name);
    }

    /**
     * Deletes the ban Reason
     * @param name Reason
     */
    public void deleteBanReason(String name){
        BanManager.deleteBanReason(name);
    }

    /**
     * check if a player is Banned
     * @param uuid The UUID of the player you want to check
     * @return true if he is banned
     */
    public boolean isBanned(String uuid){
        return BanManager.isBanned(uuid);
    }

    /**
     * Unban a player
     * @param uuid The Target UUID of the Player you want to unban
     * @return true if it was passible
     */
    public boolean unban(String uuid){
        return BanManager.unban(uuid);
    }

    /**
     * Get the Reason for what the player is banned for
     * @param uuid The Target UUID you want to check
     * @return The Reason
     */
    public String getBanReason(String uuid){
        return BanManager.getBanReason(uuid);
    }

    /**
     * Get all Ban Reasons
     * @return all Reasons
     */
    public ArrayList<BanReasons> getBanReasons(){
        return BanManager.getBanReasons();
    }

    /**
     * Get an Integer How often a player was banned
     * @param uuid the Target UUID you want to check
     * @return the count of his banns
     */
    public int getBans(String uuid){
        return BanManager.getBans(uuid);
    }

    /**
     * A String how long a player is banned
     * @param uuid The Target UUID
     * @return The String how long
     */
    public String getBanEnd(String uuid){
        return BanManager.getBanFinalEnd(uuid);
    }

    /**
     * To Ban A Player
     * @param targetUUID The Player you want to ban
     * @param reason the reason for what you want to ban him
     * @param team REQUIRED the UUID of the Player who wants to ban hin!
     * @exception PlayerIsProtectedExeption When the Player is Protect
     * @exception ReasonNotFoundExeption When the Reason is not in the System
     */
    public void banPlayer(String targetUUID, String reason, String team) throws PlayerIsProtectedExeption, ReasonNotFoundExeption {
        if (SystemManager.isProtected(targetUUID)){
            throw new PlayerIsProtectedExeption(targetUUID);
        }
        if (!existsBanReason(reason)){
            throw new ReasonNotFoundExeption(reason);
        }
        BanManager.submitBan(targetUUID, reason, team);

    }

}
