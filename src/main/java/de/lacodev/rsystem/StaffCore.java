package de.lacodev.rsystem;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class StaffCore extends JavaPlugin {
    private StaffCoreLoader staffCoreLoader;

    @Override
    public void onEnable() {
        this.staffCoreLoader = new StaffCoreLoader(this);
        this.staffCoreLoader.init(true);
    }


    @Override
    public void onDisable() {
        this.staffCoreLoader.unload();
    }


    /*public static StaffCore instance;
    public static String prefix;
    public static AntiMCLeaksHandler antimcleakshandler;
    public static TranslationHandler translator;
    public static ArrayList<BanManagerPlayerInput> banManagerPlayerInputs = new ArrayList<>();
    public static ArrayList<ReasonRename> reasonRename = new ArrayList<>();
    public static ArrayList<ReasonEDuration> reasonEDurations = new ArrayList<>();
    private static Economy econ = null;
    public double lizenz;
    public double version;
    public boolean latest = false;
    public boolean experimental = false;
    public boolean actionbar = false;
    public boolean bansystem = true;
    public String host;
    public String port;
    public String database;
    public String username;
    public String password;
    public PluginManager pm = Bukkit.getPluginManager();
    public API api = null;
    public ArrayList<String> matrix_hacktypes = new ArrayList<>();*/



    /*public void onEnable() {
        instance = this;
        antimcleakshandler = new AntiMCLeaksHandler();
        translator = new TranslationHandler();

        loadConfigs();
        applyConfigs();

        if (getConfig().getBoolean("General.Include-MatrixAntiCheat")) {
            if (setupMatrix()) {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY
                                + "added Matrix to our system");
                Bukkit.getConsoleSender().sendMessage("");
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY
                        + "to add Matrix to our system! Please make sure you have Matrix installed and set up properly!");
                Bukkit.getConsoleSender().sendMessage("");
            }
        }

        if (getConfig().getBoolean("General.Include-SpartanAntiCheat")) {
            if (setupSpartanAC()) {
                api = new API();
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY
                                + "added SpartanAntiCheat to our system");
                Bukkit.getConsoleSender().sendMessage("");
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY
                        + "to add SpartanAntiCheat to our system! Please make sure you have SpartanAntiCheat installed and set up properly!");
                Bukkit.getConsoleSender().sendMessage("");
            }
        }

        MySQL.connect();
        MySQL.createTable();

        for (Material mat : Material.values()) {
            MySQL.updateMaterial(mat);
        }

        if (MySQL.isConnected()) {
            if (initBanSystemDetectionService()) {
                registerCommands();
                registerEvents();
                startSync();
                startAntiMcLeaksServices();

                for (Sound s : Sound.values()) {
                    BanManager.sound_enums.add(s.name().toUpperCase());
                }

                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Environment: " + ChatColor.GRAY
                                + Bukkit.getVersion());
                if (experimental) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                } else if (latest) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                } else {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                }
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + ""
                                + ChatColor.BOLD + "SUCCESS " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN
                                + "PLUGIN STARTED" + ChatColor.DARK_GRAY + ")");
                Bukkit.getConsoleSender().sendMessage("");
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY
                                + "Environment: " + Bukkit.getVersion());
                if (experimental) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                } else if (latest) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                } else {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                }
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ""
                                + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED
                                + "BanSystemDetectionService CRASHED" + ChatColor.DARK_GRAY + ")");
                Bukkit.getConsoleSender().sendMessage("");
            }
        } else {
            pm.registerEvents(new Listener_NoConnection(), this);

            new BukkitRunnable() {

                @Override
                public void run() {
                    if (!MySQL.isConnected()) {
                        Bukkit.getConsoleSender().sendMessage("");
                        Bukkit.getConsoleSender().sendMessage(
                                ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ""
                                        + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED
                                        + "NO CONNECTION" + ChatColor.DARK_GRAY + ")");
                        Bukkit.getConsoleSender().sendMessage(
                                ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY
                                        + "Please make sure to set up your database correctly!");
                        Bukkit.getConsoleSender().sendMessage("");
                    }
                }
            }.runTaskTimerAsynchronously(this, 0, 30 * 20);

            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Environment: " + ChatColor.GRAY
                            + Bukkit.getVersion());
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ""
                            + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED
                            + "NO CONNECTION" + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }
    }*/

    /*private void startAntiMcLeaksServices() {
        if (getConfig().getBoolean("MCLeaks-Blocker.Enable")) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.DARK_GRAY
                            + "Activating MCLeaks-Blocker...");

            if (getConfig().getBoolean("MCLeaks-Blocker.Cache-Updater.Enable")) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        getAntiMCLeaksHandler().cacheAccounts();
                    }

                }.runTaskTimerAsynchronously(instance, 0,
                        getConfig().getInt("MCLeaks-Blocker.Cache-Updater.Period-In-Minutes") * 20 * 60);
            } else {
                getAntiMCLeaksHandler().cacheAccounts();
            }
        }
    }*/

    /*public void onReload() {

        loadConfigs();
        applyConfigs();

        try {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Checking version... "
                            + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "" + this.getDescription().getVersion()
                            + "" + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
            checkLizenz(new URL("https://api.lacodev.de/staffcore/versions/"));
        } catch (MalformedURLException e) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ""
                            + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                            + "Versioncheck" + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }

        if (getConfig().getBoolean("General.Include-Vault")) {
            if (setupEconomy()) {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY
                                + "added Vault to our system");
                Bukkit.getConsoleSender().sendMessage("");
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY
                        + "to add Vault to our system! Please make sure you have Vault installed!");
                Bukkit.getConsoleSender().sendMessage("");
            }
        }

        if (getConfig().getBoolean("General.Include-ActionBarAPI")) {
            if (setupActionBar()) {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY
                                + "added ActionBarAPI to our system");
                Bukkit.getConsoleSender().sendMessage("");
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY
                        + "to add ActionBarAPI to our system! Please make sure you have ActionBarAPI installed!");
                Bukkit.getConsoleSender().sendMessage("");
            }
        }

        if (getConfig().getBoolean("General.Include-MatrixAntiCheat")) {
            if (setupMatrix()) {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY
                                + "added Matrix to our system");
                Bukkit.getConsoleSender().sendMessage("");
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY
                        + "to add Matrix to our system! Please make sure you have Matrix installed and set up properly!");
                Bukkit.getConsoleSender().sendMessage("");
            }
        }

        if (getConfig().getBoolean("General.Include-SpartanAntiCheat")) {
            if (setupSpartanAC()) {
                api = new API();
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY
                                + "added SpartanAntiCheat to our system");
                Bukkit.getConsoleSender().sendMessage("");
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY
                        + "to add SpartanAntiCheat to our system! Please make sure you have SpartanAntiCheat installed and set up properly!");
                Bukkit.getConsoleSender().sendMessage("");
            }
        }

        MySQL.connect();
        MySQL.createTable();

        if (MySQL.isConnected()) {
            if (initBanSystemDetectionService()) {
                startAntiMcLeaksServices();

                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Environment: " + ChatColor.GRAY
                                + Bukkit.getVersion());
                if (experimental) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                } else if (latest) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                } else {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                }
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN
                                + ChatColor.BOLD + "SUCCESS " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN
                                + "PLUGIN STARTED" + ChatColor.DARK_GRAY + ")");
                Bukkit.getConsoleSender().sendMessage("");
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY
                                + "Environment: " + Bukkit.getVersion());
                if (experimental == true) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
                } else if (latest == true) {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
                } else {
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY
                                    + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "("
                                    + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
                }
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ""
                                + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED
                                + "BanSystemDetectionService CRASHED" + ChatColor.DARK_GRAY + ")");
                Bukkit.getConsoleSender().sendMessage("");
            }
        } else {
            pm.registerEvents(new Listener_NoConnection(), this);

            new BukkitRunnable() {

                @Override
                public void run() {
                    if (!MySQL.isConnected()) {
                        Bukkit.getConsoleSender().sendMessage("");
                        Bukkit.getConsoleSender().sendMessage(
                                ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ""
                                        + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED
                                        + "NO CONNECTION" + ChatColor.DARK_GRAY + ")");
                        Bukkit.getConsoleSender().sendMessage(
                                ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY
                                        + "Please make sure to set up your database correctly!");
                        Bukkit.getConsoleSender().sendMessage("");
                    }
                }
            }.runTaskTimerAsynchronously(this, 0, 30 * 20);

            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Environment: " + ChatColor.GRAY
                            + Bukkit.getVersion());
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ""
                            + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED
                            + "NO CONNECTION" + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }
    }*/

    /*public void startSync() {
        if (getConfig().getBoolean("General.Sync-with-WebInterface")) {
            new BukkitRunnable() {

                @Override
                public void run() {

                    ResultSet rs = MySQL.getResultSync("SELECT * FROM staffcoreui_sync");

                    try {
                        while (rs.next()) {
                            if (rs.getString("SYNC_TYPE").matches("BAN_PLAYER")) {
                                BanManager.submitBan(rs.getString("TARGET_UUID"), rs.getString("DESCRIPTION"),
                                        rs.getString("EXECUTOR_UUID"));
                                MySQL.update("DELETE FROM staffcoreui_sync WHERE id = '" + rs.getInt("id") + "'");
                            }
                            if (rs.getString("SYNC_TYPE").matches("MUTE_PLAYER")) {
                                BanManager.submitMute(rs.getString("TARGET_UUID"), rs.getString("DESCRIPTION"),
                                        rs.getString("EXECUTOR_UUID"));
                                MySQL.update("DELETE FROM staffcoreui_sync WHERE id = '" + rs.getInt("id") + "'");
                            }
                            if (rs.getString("SYNC_TYPE").matches("CLAIM_REPORT")) {
                                Player team = Bukkit.getPlayer(rs.getString("EXECUTOR_UUID"));
                                if (team != null) {
                                    ReportManager.claimReport(team, rs.getString("TARGET_UUID"));
                                    MySQL.update("DELETE FROM staffcoreui_sync WHERE id = '" + rs.getInt("id") + "'");
                                }
                            }
                            if (rs.getString("SYNC_TYPE").matches("VERIFY_WEBACCOUNT")) {
                                Player team = Bukkit.getPlayer(rs.getString("EXECUTOR_UUID"));
                                if (team != null) {
                                    SystemManager.sendVerificationToPlayer(team);
                                }
                            }
                        }
                    } catch (SQLException e) {
                        Bukkit.getConsoleSender().sendMessage("");
                        Bukkit.getConsoleSender().sendMessage(
                                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED
                                        + "Problem with staffcoreui_sync detected!");
                        Bukkit.getConsoleSender().sendMessage("");
                    }
                }
            }.runTaskTimerAsynchronously(getInstance(), 5 * 20, 5 * 20);

            new BukkitRunnable() {

                @Override
                public void run() {
                    long current = System.currentTimeMillis();
                    ResultSet unbancheck = MySQL.getResult("SELECT * FROM ReportSystem_bansdb");

                    try {
                        while (unbancheck.next()) {
                            if (unbancheck.getLong("BAN_END") != -1) {
                                if (unbancheck.getLong("BAN_END") < current) {
                                    BanManager.unban(unbancheck.getString("BANNED_UUID"));
                                    BanManager.sendConsoleNotify("UNBAN", unbancheck.getString("MUTED_UUID"));
                                }
                            }
                        }
                    } catch (SQLException e) {

                    }

                    ResultSet unmutecheck = MySQL.getResult("SELECT * FROM ReportSystem_mutesdb");

                    try {
                        while (unmutecheck.next()) {
                            if (unmutecheck.getLong("MUTE_END") != -1) {
                                if (unmutecheck.getLong("MUTE_END") < current) {
                                    BanManager.unmute(unmutecheck.getString("MUTED_UUID"));
                                    BanManager.sendConsoleNotify("UNMUTE", unmutecheck.getString("MUTED_UUID"));
                                }
                            }
                        }
                    } catch (SQLException e) {

                    }

                }
            }.runTaskTimerAsynchronously(getInstance(), 5 * 20, 5 * 20);
        }
    }*/

    /*public boolean initBanSystemDetectionService() {

        if (getInstance().getConfig().getBoolean("Ban-System.Enable") == false) {
            bansystem = false;

            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore BanSystem "
                            + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "DEACTIVATED" + ChatColor.DARK_GRAY
                            + ")");
        }

        ArrayList<String> detectservice = new ArrayList<>();

        detectservice.add("LiteBans");
        detectservice.add("ProfessionalBans Reloaded");
        detectservice.add("AdvancedBan");
        detectservice.add("BansPlus");
        detectservice.add("maxbans-plus");
        detectservice.add("BanSystem-Bukkit");

        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (detectservice.contains(p.getName())) {
                if (getInstance().getConfig().getBoolean("General.Activate-BanSystem-Detection")) {
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Detected BanSystem "
                                    + ChatColor.DARK_GRAY + "(" + ChatColor.LIGHT_PURPLE + "" + p.getName() + ""
                                    + ChatColor.DARK_GRAY + ")");
                    deactivateBanningSystem();
                }
                return true;
            } else {
                return true;
            }
        }
        return false;

    }*/

    /*private void deactivateBanningSystem() {
        bansystem = false;

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore BanSystem "
                        + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "DEACTIVATED" + ChatColor.DARK_GRAY
                        + ")");
    }*/

    /*public boolean setupSpartanAC() {
        return getServer().getPluginManager().getPlugin("SpartanAPI") != null
                || getServer().getPluginManager().getPlugin("Spartan") != null;
    }*/

    /*private void registerEvents() {
        pm.registerEvents(new Listener_JoinQuit(), this);
        pm.registerEvents(new Listener_Inventories(), this);
        pm.registerEvents(new Listener_Login(), this);
        pm.registerEvents(new Listener_Chat(), this);
        pm.registerEvents(new Listener_ChatLog(), this);
        pm.registerEvents(new Listener_GuardianDMG(), this);
        pm.registerEvents(new Listener_PanelManager(), this);
        if (setupMatrix()) {
            pm.registerEvents(new Listener_Matrix(), this);
        }
        if (setupSpartanAC()) {
            pm.registerEvents(new Listener_Spartan(), this);
        }

    }*/

    /*private void registerCommands() {
        // StaffCore Command
        getCommand("staffcore").setExecutor(new CMD_StaffCore());
        getCommand("staffcore").setTabCompleter(new Completer_StaffCore());

        // Report Command
        getCommand("report").setExecutor(new CMD_Report());
        getCommand("report").setTabCompleter(new Completer_Report());

        // Reports Command
        getCommand("reports").setExecutor(new CMD_Reports());

        // Check Command
        getCommand("check").setExecutor(new CMD_Check());

        // ReportManager Command
        getCommand("reportmanager").setExecutor(new CMD_ReportManager());
        getCommand("reportmanager").setTabCompleter(new Completer_ReportManager());

        if (bansystem == true) {
            // BanManager Command
            getCommand("banmanager").setExecutor(new CMD_BanManager());
            getCommand("banmanager").setTabCompleter(new Completer_BanManager());

            // Ban Command
            getCommand("ban").setExecutor(new CMD_Ban());
            getCommand("ban").setTabCompleter(new Completer_Ban());

            // Mute Command
            getCommand("mute").setExecutor(new CMD_Mute());
            getCommand("mute").setTabCompleter(new Completer_Mute());

            // Unban Command
            getCommand("unban").setExecutor(new CMD_Unban());

            // Unmute Command
            getCommand("unmute").setExecutor(new CMD_Unmute());

            // BanList Command
            getCommand("banlist").setExecutor(new CMD_BanList());

            // BanIP Command
            getCommand("banip").setExecutor(new CMD_BanIP());

            // UnBanIP Command
            getCommand("unbanip").setExecutor(new CMD_UnBanIP());

            // BanHistory Command
            getCommand("banhistory").setExecutor(new CMD_BanHistory());
        }

        // Chatfilter Command
        getCommand("chatfilter").setExecutor(new CMD_ChatFilter());

        // ChatLog Command (DROPPED)
        // getCommand("chatlog").setExecutor(new CMD_ChatLog());

        // CheckAlts Command
        getCommand("checkalts").setExecutor(new CMD_CheckAlts());

        // Warn Command
        getCommand("warn").setExecutor(new CMD_Warn());

        // Warns Command
        getCommand("warns").setExecutor(new CMD_Warns());

        // StaffUI Command
        getCommand("staffui").setExecutor(new CMD_StaffUI());

        // Ticket Command
        getCommand("ticket").setExecutor(new CMD_Ticket());

        //ChatClear Command
        getCommand("chatclear").setExecutor(new CMD_ChatClear());

        //Broadcast Command
        getCommand("broadcast").setExecutor(new CMD_BroadCast());

        //GlobalMute Command
        getCommand("globalmute").setExecutor(new CMD_MuteGlobal());
        getCommand("globalmute").setTabCompleter(new Completer_MuteGlobal());

        getCommand("staffchat").setExecutor(new CMD_StaffChat());

        getCommand("maintenance").setExecutor(new CMD_Maintenance());
        getCommand("maintenance").setTabCompleter(new Completer_Maintenance());

        getCommand("bug").setExecutor(new CMD_Bug());
        getCommand("bug").setTabCompleter(new Completer_Bug());

        getCommand("kick").setExecutor(new CMD_Kick());
        getCommand("kick").setTabCompleter(new Completer_Kick());
    }*/

    /*public void onDisable() {
        MySQL.disconnect();
    }*/

    /*private void loadConfigs() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB Generating... "
                        + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + "config.yml" + ChatColor.DARK_GRAY
                        + ")");
        Bukkit.getConsoleSender().sendMessage("");

        getConfig().options().copyDefaults(true);



        saveConfig();

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Generated " + ChatColor.DARK_GRAY + "("
                        + ChatColor.YELLOW + "config.yml" + ChatColor.DARK_GRAY + ")");
        Bukkit.getConsoleSender().sendMessage("");

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Fetching... " + ChatColor.DARK_GRAY
                        + "(" + ChatColor.YELLOW + "Translations from translate.lacodev.de"
                        + ChatColor.DARK_GRAY + ")");
        Bukkit.getConsoleSender().sendMessage("");

        translator.init();

        translator.fetch(getConfig().getString("General.Language").substring(0, 2));

        if (getConfig().getBoolean("General.Custom-Messages.Enable")) {
            translator.fetchCustom(getConfig().getString("General.Custom-Messages.Rest-API-Key"));
        }

        File mysql = new File("plugins//" + this.getDescription().getName() + "//mysql.yml");
        YamlConfiguration mcfg = YamlConfiguration.loadConfiguration(mysql);

        if (!mysql.exists()) {
            mcfg.set("MySQL.HOST", "host");
            mcfg.set("MySQL.PORT", "3306");
            mcfg.set("MySQL.DATABASE", "database");
            mcfg.set("MySQL.USERNAME", "username");
            mcfg.set("MySQL.PASSWORD", "password");

            try {
                mcfg.save(mysql);
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Generated " + ChatColor.DARK_GRAY
                                + "(" + ChatColor.YELLOW + "mysql.yml" + ChatColor.DARK_GRAY + ")");
                Bukkit.getConsoleSender().sendMessage("");
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED
                                + "ERROR while generating file " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW
                                + "mysql.yml" + ChatColor.DARK_GRAY + ")");
                Bukkit.getConsoleSender().sendMessage("");
            }
        }
    }*/



}
