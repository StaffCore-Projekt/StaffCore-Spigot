package de.lacodev.staffcore.api;

import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.staffcore.api.utils.BanUnit;
import de.lacodev.staffcore.errors.ReasonIDNotExistsExeption;
import de.lacodev.staffcore.errors.ReasonNotExistsExeption;
import java.util.ArrayList;
import org.bukkit.entity.Player;

public class BanOptions {

  public void createBanReason(String name, BanUnit unit, int time) {
    switch (unit) {
      case d:
        BanManager.createBanReason(name, "d", time);
        break;
      case h:
        BanManager.createBanReason(name, "h", time);
        break;
      case m:
        BanManager.createBanReason(name, "m", time);
        break;
      case perma:
        BanManager.createBanReason(name, "perma", time);
        break;
    }
  }

  public boolean existsBanID(int id) {
    return BanManager.existsBanID(id);
  }

  public int getIDFromBanReason(String banReason) throws ReasonNotExistsExeption {
      if (existsBanReason(banReason)) {
          return BanManager.getIDFromBanReason(banReason);
      }
    throw new ReasonNotExistsExeption(banReason);
  }

  public boolean existsBanReason(String banReason) {
    return BanManager.existsBanReason(banReason);
  }

  public String getBanReasonFromID(int id) throws ReasonIDNotExistsExeption {
      if (existsBanID(id)) {
          return BanManager.getBanReasonFromID(id);
      }
    throw new ReasonIDNotExistsExeption(id, "BAN");
  }

  public void deleteBanReason(String reason) throws ReasonNotExistsExeption {
    if (existsBanReason(reason)) {
      BanManager.deleteBanReason(reason);
    } else {
      throw new ReasonNotExistsExeption(reason);
    }
  }

  public boolean isBanned(String uuid) {
    return BanManager.isBanned(uuid);
  }

  public boolean unban(String uuid) {
    if (isBanned(uuid)) {
      BanManager.unban(uuid);
      return true;
    }
    return false;
  }

  public String getBanReason(String uuid) {
    return BanManager.getBanReason(uuid);
  }

  public ArrayList<BanReasons> getBanReasons() {
    return convertBanReasons(BanManager.getBanReasons());
  }

  private ArrayList<BanReasons> convertBanReasons(ArrayList<BanReasons> reasons) {
    ArrayList<BanReasons> finalReasons = new ArrayList<>();
      for (BanReasons reasons1 : reasons) {
          finalReasons.add(
              new BanReasons(
                  reasons1.getName(),
                  reasons1.getID(),
                  reasons1.getLength()));
      }
    return finalReasons;
  }

  public int getBans(String uuid) {
    return BanManager.getBans(uuid);
  }

  public String getEnd(String uuid) {
    return BanManager.getBanFinalEnd(uuid);
  }

  public void banPlayer(String targetuuid, String reason, String team)
      throws ReasonNotExistsExeption {
    if (existsBanReason(reason)) {
      BanManager.submitBan(targetuuid, reason, team);
    } else {
      throw new ReasonNotExistsExeption(reason);
    }
  }

  public void banIPAdress(String pluginame, Player player) {
    BanManager.banIPAddress(pluginame, player);
  }

  public boolean isIPBanned(String ipAddress) {
    return BanManager.isIPBanned(ipAddress);
  }

  public boolean isIpBanned(String playername) {
    return BanManager.isIpBanned(playername);
  }

  public boolean unbanIPAdress(String playername) {
    if (isIpBanned(playername)) {
      BanManager.unbanIpAddress(playername);
      return true;
    }
    return false;
  }
}
