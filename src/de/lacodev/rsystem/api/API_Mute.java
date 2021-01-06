package de.lacodev.rsystem.api;


import de.lacodev.rsystem.errors.PlayerIsProtectedExeption;
import de.lacodev.rsystem.errors.ReasonNotFoundExeption;
import de.lacodev.rsystem.objects.MuteReasons;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;
import java.util.ArrayList;

/**
 * @author ViaEnder
 * @version 1.0.0
 */
public class API_Mute {

    /**
     * To Get the ID of the Reason
     * @param reason the Reason
     * @return the ID
     */
    public Integer getIDFromMuteReason(String reason){
        return BanManager.getIDFromMuteReason(reason);
    }

    /**
     * To Get the Reason from the ID
     * @param id of the Reason
     * @return Reason
     */
    public String getMuteReasonFromID(int id){
        return BanManager.getMuteReasonFromID(id);
    }

    /**
     * The String how long the player is banned
     * @param uuid Target UUID
     * @return String how long the player is muted.
     */
    public String getMuteEnd(String uuid){
        return BanManager.getMuteFinalEnd(uuid);
    }

    /**
     * Check if the Mute Reason exists
     * @param reason the Reason
     * @return True if the Mute Reason exists
     */
    public boolean existsMuteReason(String reason){
        return BanManager.existsMuteReason(reason);
    }

    /**
     * Deletes a Mute Reason
     * @param reason The Reason Name
     * @exception ReasonNotFoundExeption when the reason was not found
     */
    public void deleteMuteReason(String reason) throws ReasonNotFoundExeption {
        if (existsMuteReason(reason)){
            BanManager.deleteMuteReason(reason);
            return;
        }
        throw new ReasonNotFoundExeption(reason);
    }

    /**
     * Check if a Player Is Muted
     * @param targetUuid The Target UUID you want to check
     * @return true if the Player is muted
     */
    public boolean isMuted(String targetUuid){
        return BanManager.isMuted(targetUuid);
    }

    /**
     * Unmutes a Player
     * @param targetUuid The UUID of the Player you want to unmute
     * @return true if the Player is unmuted now
     */
    public boolean unmute(String targetUuid){
        return BanManager.unmute(targetUuid);
    }

    /**
     * Get ALL mute Reasons
     * @return An ArrayList with all Mute Reasons
     */
    public ArrayList<MuteReasons> getMuteReasons(){
        return BanManager.getMuteReasons();
    }

    /**
     * Gets the Reason why the target is muted
     * @param uuid Target UUID from what you want to know
     * @return Reason | 'UNKNOWN' if he is not muted
     */
    public String getMuteReason(String uuid){
        return BanManager.getMuteReason(uuid);
    }

    /**
     *
     * @param targetUuid the person you want to mute
     * @param reason the Reason why you will mute him
     * @param teamUuid the UUID of the Person who wants to mute him
     * @return true if it was passible
     * @exception PlayerIsProtectedExeption when the target is protected
     */
    public boolean mute(String targetUuid, String reason, String teamUuid) throws PlayerIsProtectedExeption {
        if (SystemManager.isProtected(targetUuid)){
            throw new PlayerIsProtectedExeption(targetUuid);
        } else{
            if (existsMuteReason(reason)){
                BanManager.submitBan(targetUuid, reason, teamUuid);
            } else{

            }
        }
        return false;
    }

}
