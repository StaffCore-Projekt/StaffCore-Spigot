package de.lacodev.rsystem.mysql;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.SystemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public class MySQL {

    // Defines Login credentials for MySQL Database
    private static final String host = Main.getInstance().host;
    private static final String port = Main.getInstance().port;
    private static final String username = Main.getInstance().username;
    private static final String password = Main.getInstance().password;
    private static final String database = Main.getInstance().database;

    private static Connection con;

    private static final String mysql =
            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "MySQL"
                    + ChatColor.DARK_GRAY + ") - ";

    // Opens MySQL Connection
    public static void connect() {
        Bukkit.getConsoleSender().sendMessage(
                mysql + ChatColor.GRAY + "Hooking " + ChatColor.RED + "Database Services" + ChatColor.GRAY
                        + "...");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            setCon(DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoreconnect=true&useSSL=false",
                    username, password));
            Bukkit.getConsoleSender().sendMessage(
                    mysql + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "hooked " + ChatColor.GREEN
                            + "Database " + database);
        } catch (SQLException e) {
            if (Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
                getDebug(e);
            } else {
                Bukkit.getConsoleSender().sendMessage(
                        mysql + ChatColor.RED + "Failed " + ChatColor.GRAY + "to hook " + ChatColor.GREEN
                                + "Database Services");
                Bukkit.getConsoleSender()
                        .sendMessage(mysql + ChatColor.GRAY + "May the service is in maintenance...");
                Bukkit.getConsoleSender()
                        .sendMessage(mysql + ChatColor.GRAY + "Please check your database host!");
            }
        } catch (ClassNotFoundException e) {
            if (Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
                getDebug(e);
            } else {
                Bukkit.getConsoleSender().sendMessage(
                        mysql + ChatColor.RED + "Failed " + ChatColor.GRAY + "to hook " + ChatColor.GREEN
                                + "Database Services");
                Bukkit.getConsoleSender()
                        .sendMessage(mysql + ChatColor.GRAY + "May the service is in maintenance...");
                Bukkit.getConsoleSender()
                        .sendMessage(mysql + ChatColor.GRAY + "Please check your database host!");
            }
        }
    }

    public static void getDebug(ClassNotFoundException e) {
        Bukkit.getConsoleSender().sendMessage(
                mysql + ChatColor.RED + "Failed " + ChatColor.GRAY + "to hook " + ChatColor.GREEN
                        + "Database Services");
        Bukkit.getConsoleSender().sendMessage(
                mysql + ChatColor.RED + "MySQL Driver not found! Please use " + ChatColor.YELLOW
                        + "Java 8 or above");
    }

    public static void getDebug(SQLException e) {
        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.DARK_GRAY + "==================================================");
        Bukkit.getConsoleSender().sendMessage(
                mysql + ChatColor.RED + "Failed " + ChatColor.GRAY + "to hook " + ChatColor.GREEN
                        + "Database Services");
        Bukkit.getConsoleSender()
                .sendMessage(mysql + ChatColor.DARK_RED + ChatColor.BOLD + "SQLState: " + e.getSQLState());
        Bukkit.getConsoleSender().sendMessage(
                mysql + ChatColor.DARK_RED + ChatColor.BOLD + "Possible Issues: " + getMySQLError(
                        e.getSQLState()));
        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.DARK_GRAY + "==================================================");
    }

    public static String getMySQLError(String sqlState) {
        if (sqlState.matches("08S01")) {
            return
                    "\n - Cant get hostname for your address! \n - Bad handshake \n - Unknown command \n - Server shutdown in progress "
                            + "\n - Cant create IP socket \n - Aborted connection to db: " + database
                            + " \n - Got a packet bigger than 'max_allowed_packet' bytes "
                            + "\n - Got a read error from connection pipe \n - Got packets out of order \n - Couldnt uncompress communication packet "
                            + "\n - Got an error reading communication packets \n - Got timeout reading communication packets "
                            + "\n - Got an error writing communication packets \n - Got timeout writing communication packets";
        }
        if (sqlState.matches("HY000")) {
            return "\n - Server couldnt process an action by the user";
        }
        if (sqlState.matches("HY001")) {
            return "\n - Out of memory; restart server and try again \n - Out of sort memory, consider increasing server sort buffer size";
        }
        if (sqlState.matches("8004")) {
            return "\n - Too many connections";
        }
        if (sqlState.matches("28000")) {
            return "\n - Access was denied for user " + username;
        }
        if (sqlState.matches("42000")) {
            return "\n - Action by the user was denied from the server \n - Syntax Error in any MySQL Statement";
        }
        return "Unknown Error!";
    }

    // Returns boolean if MySQL isConnected
    public static boolean isConnected() {
        return (getCon() != null);
    }

    public static void disconnect() {
        if (isConnected()) {
            Bukkit.getConsoleSender().sendMessage(
                    mysql + ChatColor.GRAY + "Detaching from " + ChatColor.RED + "Database Services"
                            + ChatColor.GRAY + "...");
            try {
                getCon().close();
                Bukkit.getConsoleSender().sendMessage(
                        mysql + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "detached from "
                                + ChatColor.GREEN + "Database " + database);
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(
                        mysql + ChatColor.RED + "Failed " + ChatColor.GRAY + "to detach from " + ChatColor.GREEN
                                + "Database Services");
                Bukkit.getConsoleSender()
                        .sendMessage(mysql + ChatColor.GRAY + "May the service is not connected...");
                Bukkit.getConsoleSender()
                        .sendMessage(mysql + ChatColor.GRAY + "Please check your database host!");
            }
        }
    }

    // Creates MySQL Table
    public static void createTable() {
        if (isConnected()) {
            try {
                PreparedStatement st1 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_playerdb(id INT(6) AUTO_INCREMENT UNIQUE, UUID VARCHAR(255), PLAYERNAME VARCHAR(255), BANS INT(6), MUTES INT(6), REPORTS INT(6), WARNS INT(6), LAST_KNOWN_IP VARCHAR(255), LAST_ONLINE LONG)");
                st1.executeUpdate();
                PreparedStatement st2 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_reportsdb(id INT(6) AUTO_INCREMENT UNIQUE, REPORTER_UUID VARCHAR(255), REPORTED_UUID VARCHAR(255), REASON VARCHAR(255), TEAM_UUID VARCHAR(255), STATUS INT(6), reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
                st2.executeUpdate();
                PreparedStatement st3 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_bansdb(id INT(6) AUTO_INCREMENT UNIQUE, BANNED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), BAN_END LONG)");
                st3.executeUpdate();
                PreparedStatement st4 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_mutesdb(id INT(6) AUTO_INCREMENT UNIQUE, MUTED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), MUTE_END LONG)");
                st4.executeUpdate();
                PreparedStatement st5 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_actionsdb(id INT(6) AUTO_INCREMENT UNIQUE, ACTION VARCHAR(255), EXECUTOR_UUID VARCHAR(255), DESCRIPTION VARCHAR(255))");
                st5.executeUpdate();
                PreparedStatement st6 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_reasonsdb(id INT(6) AUTO_INCREMENT UNIQUE, TYPE VARCHAR(255), NAME VARCHAR(255), BAN_LENGTH LONG, REPORT_ITEM VARCHAR(255))");
                st6.executeUpdate();
                PreparedStatement st7 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_materialsdb(id INT(6) AUTO_INCREMENT UNIQUE, TYPE VARCHAR(255) UNIQUE)");
                st7.executeUpdate();
                PreparedStatement st9 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_messages(id INT(6) AUTO_INCREMENT UNIQUE, SENDER_UUID VARCHAR(255), MESSAGE VARCHAR(256), reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
                st9.executeUpdate();
                PreparedStatement st10 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_ipbans(id INT(6) AUTO_INCREMENT UNIQUE, IP_ADDRESS VARCHAR(255), END LONG)");
                st10.executeUpdate();
                PreparedStatement st11 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_banhistory(id INT(6) AUTO_INCREMENT UNIQUE, BANNED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), BAN_START LONG, BAN_END LONG)");
                st11.executeUpdate();
                PreparedStatement st12 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_warnsdb(id INT(6) AUTO_INCREMENT UNIQUE, UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255))");
                st12.executeUpdate();
                PreparedStatement st13 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_ticketdb(id INT(6) AUTO_INCREMENT UNIQUE, CREATOR_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
                st13.executeUpdate();
                PreparedStatement st14 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_ticketdb_messages(id INT(6) AUTO_INCREMENT UNIQUE, TICKET_ID INT(6), COMMENT TEXT, AUTHOR VARCHAR(255), POSTED VARCHAR(255))");
                st14.executeUpdate();
                PreparedStatement st15 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_settings( `id` INT(6) AUTO_INCREMENT UNIQUE , `KEY` VARCHAR(255) , `VALUE` VARCHAR(255) )");
                st15.executeUpdate();
                PreparedStatement st16 = getCon().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS ReportSystem_bugs( id INT(6) AUTO_INCREMENT UNIQUE, CREATOR_NAME VARCHAR(255), CREATOR_UUID VARCHAR(255), MESSAGE VARCHAR(255))");
                st16.executeUpdate();
                Bukkit.getConsoleSender().sendMessage(
                        mysql + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "created/loaded "
                                + ChatColor.GREEN + "MySQL-Table");
            } catch (SQLException e) {
                if (Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
                    getDebug(e);
                } else {
                    Bukkit.getConsoleSender()
                            .sendMessage(mysql + ChatColor.RED + "ERROR while creating/loading MySQL-Table!");
                }
            }

            MySQL.updateTable("ALTER TABLE ReportSystem_playerdb ADD LAST_ONLINE LONG");
            MySQL.updateTable("ALTER TABLE ReportSystem_playerdb ADD WARNS INT(6)");
            MySQL.updateTable("ALTER TABLE ReportSystem_playerdb ADD PROTECTED INT(6)");
            MySQL.updateTable("ALTER TABLE ReportSystem_ipbans ADD PLAYERNAME VARCHAR(255)");
            MySQL.updateTable(
                    "ALTER TABLE ReportSystem_reportsdb ADD reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
        }
    }

    public static void update(String qry) {
        if (isConnected()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        getCon().createStatement().executeUpdate(qry);
                    } catch (SQLException e) {
                        if (Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
                            getDebug(e);
                        } else {
                            Bukkit.getConsoleSender()
                                    .sendMessage(mysql + ChatColor.RED + "ERROR while updating MySQL-Table");
                        }
                    }
                }

            }.runTaskAsynchronously(Main.getInstance());
        }
    }

    public static void updateMaterial(Material mat) {
        if (isConnected()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        if (!SystemManager.existsMaterial(mat.toString())) {
                            getCon().createStatement().executeUpdate(
                                    "INSERT INTO ReportSystem_materialsdb(TYPE) VALUES ('" + mat + "')");
                        }
                    } catch (SQLException e) {
                        if (Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
                            getDebug(e);
                        } else {
                            Bukkit.getConsoleSender()
                                    .sendMessage(mysql + ChatColor.RED + "ERROR while updating MySQL-Table");
                        }
                    }
                }

            }.runTaskAsynchronously(Main.getInstance());
        }
    }

    public static void updateTable(String qry) {
        if (isConnected()) {
            try {
                getCon().createStatement().executeUpdate(qry);

            } catch (SQLException e) {

            }
        }
    }

    public static ResultSet getResult(String qry) {
        if (isConnected()) {
            try {
                return getCon().createStatement().executeQuery(qry);
            } catch (SQLException e) {
                if (Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
                    getDebug(e);
                } else {
                    Bukkit.getConsoleSender()
                            .sendMessage(mysql + ChatColor.RED + "ERROR while collecting data from MySQL-Table");
                    Bukkit.getConsoleSender().sendMessage(
                            mysql + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Query: " + ChatColor.YELLOW
                                    + qry + ChatColor.DARK_GRAY + ")");
                }

            }
        }
        return null;
    }

    public static ResultSet getResultSync(String qry) {
        if (isConnected()) {
            try {
                return getCon().createStatement().executeQuery(qry);
            } catch (SQLException e) {
                if (Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
                    getDebug(e);
                } else {
                    Bukkit.getConsoleSender().sendMessage(
                            mysql + ChatColor.GRAY + "You didn't set up the Web-UI, please " + ChatColor.RED
                                    + "disable Sync-with-WebInterface " + ChatColor.GRAY + "or " + ChatColor.RED
                                    + "setup the WebInterface");
                }

            }
        }
        return null;
    }

    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection con) {
        MySQL.con = con;
    }


}
