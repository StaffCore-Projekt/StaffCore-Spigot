package de.lacodev.staffcore.api;

import de.lacodev.rsystem.objects.MuteReasons;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.staffcore.api.utils.MuteUnit;
import de.lacodev.staffcore.errors.ReasonAlreadyExistsExeption;
import de.lacodev.staffcore.errors.ReasonNotExistsExeption;

import java.util.ArrayList;

public class MuteOptions {

    public void createMuteReason(String reason, MuteUnit unit, int lenght) throws ReasonAlreadyExistsExeption {
        if (existsMuteReason(reason))
            throw new ReasonAlreadyExistsExeption(reason);
        switch (unit) {
            case m:
                BanManager.createMuteReason(reason, "m", lenght);
                break;
            case h:
                BanManager.createMuteReason(reason, "h", lenght);
                break;
            case d:
                BanManager.createMuteReason(reason, "d", lenght);
                break;
        }
    }

    public void deleteMuteReason(String reason) throws ReasonNotExistsExeption {
        if (existsMuteReason(reason)) {
            BanManager.deleteMuteReason(reason);
        } else {
            throw new ReasonNotExistsExeption(reason);
        }
    }

    public boolean unMute(String uuid) {
        if (isMuted(uuid)) {
            BanManager.unmute(uuid);
            return true;
        }
        return false;
    }

    public boolean isMuted(String uuid) {
        return BanManager.isMuted(uuid);
    }

    public boolean existsMuteReason(String reason) {
        return BanManager.existsMuteReason(reason);
    }

    public boolean existsMuteID(int id) {
        return BanManager.existsMuteID(id);
    }

    public String getMuteEnd(String uuid) {
        return BanManager.getMuteFinalEnd(uuid);
    }

    public String getMuteReasonFromID(int id) {
        if (existsMuteID(id))
            return BanManager.getMuteReasonFromID(id);
        return null;
    }

    public ArrayList<MuteReasons> getMuteReasons() {
        return converter(BanManager.getMuteReasons());
    }

    private ArrayList<MuteReasons> converter(ArrayList<MuteReasons> muteReasons) {
        ArrayList<MuteReasons> finalReasons = new ArrayList<>();
        for (MuteReasons reasons : muteReasons)
            finalReasons.add(
                    new MuteReasons(
                            reasons.getName(),
                            reasons.getID(),
                            reasons.getLength()));
        return finalReasons;
    }

    public String getMuteReason(String uuid) {
        return BanManager.getMuteReason(uuid);
    }

    public int getMutes(String uuid) {
        return BanManager.getMutes(uuid);
    }

    public boolean mutePlayer(String targetuuid, String reason, String teamuuid) {
        return BanManager.submitMute(targetuuid, reason, teamuuid);
    }
}
