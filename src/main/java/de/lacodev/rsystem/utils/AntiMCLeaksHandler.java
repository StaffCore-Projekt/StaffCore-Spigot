package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AntiMCLeaksHandler {

    // Handler for anti-mcleaks.lacodev.de Services
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;

    private final ArrayList<String> cache = new ArrayList<>();

    public AntiMCLeaksHandler(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    public void init(){
        if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("MCLeaks-Blocker.Enable")){
            cacheAccounts();
            if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("MCLeaks-Blocker.Cache-Updater.Enable")){
                startCacheTimer(staffCore.getStaffCoreLoader().getConfigProvider().getInt("MCLeaks-Blocker.Cache-Updater.Period-In-Minutes"));
            }
        }
    }

    public void cacheAccounts() {

        cache.clear();

        int totalAccounts = Integer.valueOf(
                readJsonFromUrl("https://anti-mcleaks.lacodev.de/api/v1/total/").get("total").toString());

        JSONObject allAccountsData = readJsonFromUrl(
                "https://anti-mcleaks.lacodev.de/api/v1/accounts/");

        for (int i = 1; i <= totalAccounts; i++) {
            JSONObject data = (JSONObject) allAccountsData.get(String.valueOf(i));

            cache.add(data.get("uuid").toString());
        }

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "Successfully "
                        + ChatColor.DARK_GRAY + "cached " + ChatColor.GREEN + totalAccounts + " Accounts");
        Bukkit.getConsoleSender().sendMessage("");

    }

    public boolean isAccountCached(String uuid) {
        return cache.contains(uuid);
    }

    public List<String> getAccountCache() {
        return cache;
    }

    private JSONObject readJsonFromUrl(String url) {
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser
                    .parse(new InputStreamReader(new URL(url).openStream(), StandardCharsets.UTF_8));

            return jsonObject;
        } catch (IOException | ParseException | ClassCastException e) {

        }
        return null;
    }

    public void startCacheTimer(Integer integer){
        Bukkit.getScheduler().runTaskTimerAsynchronously(staffCore, this::cacheAccounts, 0,integer*1200L);
    }
}
