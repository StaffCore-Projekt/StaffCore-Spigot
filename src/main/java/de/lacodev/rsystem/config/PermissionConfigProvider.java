package de.lacodev.rsystem.config;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.pojo.Config;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PermissionConfigProvider extends Config {

    private final StaffCore staffCore;

    public PermissionConfigProvider(StaffCore staffCore) {
        super(new File(staffCore.getDataFolder().getParent() + File.separator + "StaffCore" + File.separator + "Permission.yml"));
        this.staffCore = staffCore;
    }

    @Override
    public void load() {
        boolean toSave = false;
        if (!getYamlConfiguration().contains("Everything")){
            this.getYamlConfiguration().set("Everything", "rsystem.*");
            toSave = true;
        }

        ConfigurationSection systemConfig = getYamlConfiguration().getConfigurationSection("System");
        if (systemConfig == null){
            systemConfig = getYamlConfiguration().createSection("System");
            systemConfig.set("Notify", "rsystem.notify");
            systemConfig.set("Adminpanel", "rsystem.adminpanel");
            toSave = true;
        }

        ConfigurationSection opConfig = getYamlConfiguration().getConfigurationSection("Allow-OP");
        if (opConfig == null) {
            opConfig = getYamlConfiguration().createSection("Allow-OP");
            opConfig.set("Join", "rsystem.op.allow");
            opConfig.set("Notify", "rsystem.op.notify");
            toSave = true;
        }

        ConfigurationSection ipBanConfig = getYamlConfiguration().getConfigurationSection("IpBan");
        if (ipBanConfig == null) {
            ipBanConfig = getYamlConfiguration().createSection("IpBan");
            ipBanConfig.set("Use", "rsystem.ipban.use");
            ipBanConfig.set("Notify", "rsystem.ipban.notify");
            toSave = true;
        }

        ConfigurationSection ipUnBanConfig = getYamlConfiguration().getConfigurationSection("IpUnBan");
        if (ipUnBanConfig == null) {
            ipUnBanConfig = getYamlConfiguration().createSection("IpUnBan");
            ipUnBanConfig.set("Use", "rsystem.ipunban.use");
            ipUnBanConfig.set("Notify", "rsystem.ipunban.notify");
            toSave = true;
        }

        ConfigurationSection reportConfig = getYamlConfiguration().getConfigurationSection("Report");
        if (reportConfig == null) {
            reportConfig = getYamlConfiguration().createSection("Report");
            reportConfig.set("Notify", "rsystem.report.notify");
            reportConfig.set("Claim", "rsystem.report.claim");
            reportConfig.set("Spam.Bypass", "rsystem.report.spam.bypass");
            toSave = true;
        }

        ConfigurationSection reportManagerConfig = getYamlConfiguration().getConfigurationSection("ReportManager");
        if (reportManagerConfig == null) {
            reportManagerConfig = getYamlConfiguration().createSection("ReportManager");
            reportManagerConfig.set("addreason", "rsystem.reportmanager.addreason");
            reportManagerConfig.set("removereason", "rsystem.reportmanager.removereason");
            toSave = true;
        }

        ConfigurationSection reportsConfig = getYamlConfiguration().getConfigurationSection("Reports");
        if (reportsConfig == null) {
            reportsConfig = getYamlConfiguration().createSection("Reports");
            reportsConfig.set("See", "rsystem.reports.see");
            toSave = true;
        }

        ConfigurationSection banManagerConfig = getYamlConfiguration().getConfigurationSection("BanManager");
        if (banManagerConfig == null) {
            banManagerConfig = getYamlConfiguration().createSection("BanManager");
            banManagerConfig.set("AddReason", "rsystem.banmanager.addreason");
            banManagerConfig.set("RemoveReason", "rsystem.banmanager.removereason");
            banManagerConfig.set("EditReason", "rsystem.banmanager.editreason");
            toSave = true;
        }

        ConfigurationSection banConfig = getYamlConfiguration().getConfigurationSection("Ban");
        if (banConfig == null) {
            banConfig = getYamlConfiguration().createSection("Ban");
            banConfig.set("Use", "rsystem.ban.use");
            banConfig.set("Notify", "rsystem.ban.notify");
            banConfig.set("List", "rsystem.ban.list");
            toSave = true;
        }

        ConfigurationSection muteConfig = getYamlConfiguration().getConfigurationSection("Mute");
        if (muteConfig == null) {
            muteConfig = getYamlConfiguration().createSection("Mute");
            muteConfig.set("Use", "rsystem.mute.use");
            muteConfig.set("Notify", "rsystem.mute.notify");
            muteConfig.set("Global.Change", "rsystem.mute.global.change");
            muteConfig.set("Global.Write", "rsystem.mute.global.write");
            toSave = true;
        }

        ConfigurationSection unBanConfig = getYamlConfiguration().getConfigurationSection("UnBan");
        if (unBanConfig == null) {
            unBanConfig = getYamlConfiguration().createSection("UnBan");
            unBanConfig.set("Use", "rsystem.unban.use");
            unBanConfig.set("Notify", "rsystem.unban.notify");
            toSave = true;
        }

        ConfigurationSection unMuteConfig = getYamlConfiguration().getConfigurationSection("UnMute");
        if (unMuteConfig == null) {
            unMuteConfig = getYamlConfiguration().createSection("UnMute");
            unMuteConfig.set("Use", "rsystem.unmute.use");
            unMuteConfig.set("Notify", "rsystem.unmute.notify");
            toSave = true;
        }

        ConfigurationSection chatFilterConfig = getYamlConfiguration().getConfigurationSection("Chatfilter");
        if (chatFilterConfig == null) {
            chatFilterConfig = getYamlConfiguration().createSection("Chatfilter");
            chatFilterConfig.set("Manage", "rsystem.chatfilter.manage");
            toSave = true;
        }

        ConfigurationSection chatLogConfig = getYamlConfiguration().getConfigurationSection("ChatLog");
        if (chatLogConfig == null) {
            chatLogConfig = getYamlConfiguration().createSection("ChatLog");
            chatLogConfig.set("Notify", "rsystem.chatlog.notify");
            toSave = true;
        }

        ConfigurationSection banHistoryConfig = getYamlConfiguration().getConfigurationSection("BanHistory");
        if (banHistoryConfig == null) {
            banHistoryConfig = getYamlConfiguration().createSection("BanHistory");
            banHistoryConfig.set("See", "rsystem.banhistory.see");
            toSave = true;
        }

        ConfigurationSection checkAltsConfig = getYamlConfiguration().getConfigurationSection("CheckAlts");
        if (checkAltsConfig == null) {
            chatFilterConfig = getYamlConfiguration().createSection("CheckAlts");
            chatFilterConfig.set("Use", "rsystem.checkalts.use");
            toSave = true;
        }

        ConfigurationSection warnConfig = getYamlConfiguration().getConfigurationSection("Warn");
        if (warnConfig == null) {
            warnConfig = getYamlConfiguration().createSection("Warn");
            warnConfig.set("Use", "rsystem.warn.use");
            warnConfig.set("Notify", "rsystem.warn.notify");
            toSave = true;
        }

        ConfigurationSection warnsConfig = getYamlConfiguration().getConfigurationSection("Warns");
        if (warnsConfig == null) {
            warnsConfig = getYamlConfiguration().createSection("Warns");
            warnsConfig.set("See", "rsystem.warns.see");
            toSave = true;
        }

        ConfigurationSection chatLogsConfig = getYamlConfiguration().getConfigurationSection("ChatLogs");
        if (chatLogsConfig == null) {
            chatLogsConfig = getYamlConfiguration().createSection("ChatLogs");
            chatLogsConfig.set("Use", "rsystem.chatlogs.use");
            toSave = true;
        }

        ConfigurationSection chatClearConfig = getYamlConfiguration().getConfigurationSection("ChatClear");
        if (chatClearConfig == null) {
            chatClearConfig = getYamlConfiguration().createSection("ChatClear");
            chatClearConfig.set("Clear", "rsystem.chatclear.clear");
            chatClearConfig.set("Bypass", "rsystem.chatclear.bypass");
            toSave = true;
        }

        ConfigurationSection bugsConfig = getYamlConfiguration().getConfigurationSection("Bugs");
        if (bugsConfig == null) {
            bugsConfig = getYamlConfiguration().createSection("Bugs");
            bugsConfig.set("Notify", "rsystem.bugs.notify");
            bugsConfig.set("Remove", "rsystem.bugs.remove");
            bugsConfig.set("See", "rsystem.bugs.see");
            toSave = true;
        }

        ConfigurationSection broadCastConfig = getYamlConfiguration().getConfigurationSection("BroadCast");
        if (broadCastConfig == null) {
            broadCastConfig = getYamlConfiguration().createSection("BroadCast");
            broadCastConfig.set("Send", "rsystem.broadcast.send");
            toSave = true;
        }

        ConfigurationSection staffChatConfig = getYamlConfiguration().getConfigurationSection("StaffChat");
        if (staffChatConfig == null) {
            staffChatConfig = getYamlConfiguration().createSection("StaffChat");
            staffChatConfig.set("Read", "rsystem.staffchat.read");
            staffChatConfig.set("Write", "rsystem.staffchat.write");
            toSave = true;
        }

        ConfigurationSection maintenanceConfig = getYamlConfiguration().getConfigurationSection("Maintenance");
        if (maintenanceConfig == null) {
            maintenanceConfig = getYamlConfiguration().createSection("Maintenance");
            maintenanceConfig.set("Change", "rsystem.maintenance.change");
            maintenanceConfig.set("Join", "rsystem.maintenance.join");
            toSave = true;
        }

        ConfigurationSection kickConfig = getYamlConfiguration().getConfigurationSection("Kick");
        if (kickConfig == null) {
            kickConfig = getYamlConfiguration().createSection("Kick");
            kickConfig.set("Use", "rsystem.kick.use");
            kickConfig.set("Notify", "rsystem.kick.notify");
            toSave = true;
        }
        if (toSave){
            save();
        }
    }

}
