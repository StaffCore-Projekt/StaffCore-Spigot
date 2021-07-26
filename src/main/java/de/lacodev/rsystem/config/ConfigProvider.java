package de.lacodev.rsystem.config;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.pojo.Config;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Arrays;

public class ConfigProvider extends Config {

    private final StaffCore staffCore;


    public ConfigProvider(StaffCore staffCore) {
        super(new File(staffCore.getDataFolder().getParent() + File.separator + "StaffCore" + File.separator + "Permission.yml"));
        this.staffCore = staffCore;
    }

    @Override
    public void load() {
        boolean toSave = false;

        ConfigurationSection generalSection = getYamlConfiguration().getConfigurationSection("General");
        if (generalSection == null) {
            generalSection = getYamlConfiguration().createSection("General");
            generalSection.set("System-Prefix", "&cSystem &8%double_arrow%");
            ConfigurationSection customMessagesSection = generalSection.getConfigurationSection("Custom-Messages");
            if (customMessagesSection == null) {
                customMessagesSection = generalSection.createSection("Custom-Messages");
                customMessagesSection.set("Enable", false);
                customMessagesSection.set("Rest-API-Key", "{Your API-Key from the Dashboard}");
                toSave = true;
            }
            generalSection.set("Language", "de");
            generalSection.set("Activate-BanSystem-Detection", false);
            generalSection.set("Include-Worldname", false);
            generalSection.set("Include-Vault", false);
            generalSection.set("Include-ActionBarAPI", false);
            generalSection.set("Include-MatrixAntiCheat", false);
            generalSection.set("Include-SpartanAntiCheat", false);
            generalSection.set("Force-SpectatorMode", true);
            generalSection.set("Sync-with-WebInterface", false);
            toSave = true;
        }

        ConfigurationSection mcLeaksBlockerSection = getYamlConfiguration().getConfigurationSection("MCLeaks-Blocker");
        if (mcLeaksBlockerSection == null) {
            mcLeaksBlockerSection = getYamlConfiguration().createSection("MCLeaks-Blocker");
            mcLeaksBlockerSection.set("Enable", true);
            mcLeaksBlockerSection.set("Cache-Updater.Enable", true);
            mcLeaksBlockerSection.set("Cache-Updater.Period-In-Minutes", 10);
            toSave = true;
        }

        ConfigurationSection banSystemSection = getYamlConfiguration().getConfigurationSection("Ban-System");
        if (banSystemSection == null) {
            banSystemSection = getYamlConfiguration().createSection("Ban-System");
            banSystemSection.set("Enable", true);
            banSystemSection.set("Per-Reason-Permission.Enable", true);
            banSystemSection.set("Per-Reason-Permission.Prefix-For-Permission", "rsystem.ban.reason.");
            toSave = true;
        }

        ConfigurationSection muteSystemSection = getYamlConfiguration().getConfigurationSection("Mute-System");
        if (muteSystemSection == null) {
            muteSystemSection = getYamlConfiguration().createSection("Mute-System");
            muteSystemSection.set("Per-Reason-Permission.Enable", true);
            muteSystemSection.set("Per-Reason-Permission.Prefix-For-Permission", "rsystem.mute.reason.");
            toSave = true;
        }

        ConfigurationSection banAnimationSection = getYamlConfiguration().getConfigurationSection("Ban-Animation");
        if (banAnimationSection == null) {
            banAnimationSection = getYamlConfiguration().createSection("Ban-Animation");
            banAnimationSection.set("Enable", true);
            banAnimationSection.set("Type", "GUARDIAN");
            banAnimationSection.set("Sound", "ENTITY_ENDER_DRAGON_DEATH");
            toSave = true;
        }

        ConfigurationSection vaultSection = getYamlConfiguration().getConfigurationSection("Vault.Rewards.Report");
        if (vaultSection == null) {
            vaultSection = getYamlConfiguration().createSection("Vault.Rewards.Report");
            vaultSection.set("MIN", 50);
            vaultSection.set("MAX", 150);
            toSave = true;
        }

        ConfigurationSection reportSpamSection = getYamlConfiguration().getConfigurationSection("Report-Spam");
        if (reportSpamSection == null) {
            reportSpamSection = getYamlConfiguration().createSection("Report-Spam");
            reportSpamSection.set("Duration-in-Seconds", 20);
            toSave = true;
        }

        ConfigurationSection ticketSystemSection = getYamlConfiguration().getConfigurationSection("TicketSystem");
        if (ticketSystemSection == null) {
            ticketSystemSection = getYamlConfiguration().createSection("TicketSystem");
            ticketSystemSection.set("Cooldown-In-Seconds", 120);
            ticketSystemSection.set("WebInterface-URL", "https://yourserver.com/Web-UI-v3.5.0/dashboard/tickets/view/?id=");
            toSave = true;
        }

        if (!getYamlConfiguration().contains("Chatfilter.Advertisment.Whitelist")){
            getYamlConfiguration().set("Chatfilter.Advertisment.Whitelist", Arrays.asList(
                    "yourserver.de",
                    "forum.yourserver.de",
                    "ts.yourserver.de",
                    "shop.yourserver.de"
            ));
            toSave = true;
        }

        if (!getYamlConfiguration().contains("Chatfilter.Advertisment.Blocked-Domains")){
            getYamlConfiguration().set("Chatfilter.Advertisment.Blocked-Domains", Arrays.asList(
                    ".ac",
                    ".ad",
                    ".ae",
                    ".aero",
                    ".af",
                    ".ag",
                    ".ai",
                    ".al",
                    ".am",
                    ".an",
                    ".ao",
                    ".aq",
                    ".ar",
                    ".as",
                    ".asia",
                    ".at",
                    ".au",
                    ".aw",
                    ".ax",
                    ".az",
                    ".de",
                    ".com",
                    ".net",
                    ".org",
                    ".eu",
                    ".be",
                    ".nl",
                    ".me",
                    ".ch",
                    ".info",
                    ".dev"
            ));
            toSave = true;
        }

        if (!getYamlConfiguration().contains("Unpermitted-OP.Kick-Player.Enable")){
            getYamlConfiguration().set("Unpermitted-OP.Kick-Player.Enable", true);
            toSave = true;
        }

        ConfigurationSection ipBanSection = getYamlConfiguration().getConfigurationSection("IP-Ban");
        if (ipBanSection == null) {
            ipBanSection = getYamlConfiguration().createSection("IP-Ban");
            ipBanSection.set("Ban-IP-When-Player-Gets-Banned", false);
            ipBanSection.set("Duration-in-Hours", 24);
            toSave = true;
        }

        if (!getYamlConfiguration().contains("Unpermitted-OP.Kick-Player.Enable")){
            getYamlConfiguration().set("Unpermitted-OP.Kick-Player.Enable", true);
            toSave = true;
        }

        if (toSave){
            save();
        }
    }

}
