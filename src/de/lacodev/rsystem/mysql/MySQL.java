package de.lacodev.rsystem.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.SystemManager;

public class MySQL {
	
	   
    // Defines Login credentials for MySQL Database
    private static String host = Main.getInstance().host;
    private static String port = Main.getInstance().port;
    private static String username = Main.getInstance().username;
    private static String password = Main.getInstance().password;
    private static String database = Main.getInstance().database;
    
    private static Connection con;
    
    private static String mysql = "§cSystem §8(§7MySQL§8) §8- ";
    
    // Opens MySQL Connection
    public static void connect() {
        Bukkit.getConsoleSender().sendMessage(mysql + "§7Hooking §cDatabase Services§7...");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setCon(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoreconnect=true&useSSL=false", username, password));
            Bukkit.getConsoleSender().sendMessage(mysql + "§aSuccessfully §7hooked §aDatabase " + database);
        } catch (SQLException e) {
        	if(Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
        		getDebug(e);
        	} else {
                Bukkit.getConsoleSender().sendMessage(mysql + "§cFailed §7to hook §aDatabase Services");
                Bukkit.getConsoleSender().sendMessage(mysql + "§7May the service is in maintenance...");
                Bukkit.getConsoleSender().sendMessage(mysql + "§7Please check your database host!");
        	}
        } catch (ClassNotFoundException e) {
        	if(Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
        		getDebug(e);
        	} else {
                Bukkit.getConsoleSender().sendMessage(mysql + "§cFailed §7to hook §aDatabase Services");
                Bukkit.getConsoleSender().sendMessage(mysql + "§7May the service is in maintenance...");
                Bukkit.getConsoleSender().sendMessage(mysql + "§7Please check your database host!");
        	}
        }
    }
    
    public static void getDebug(ClassNotFoundException e) {
    	Bukkit.getConsoleSender().sendMessage(mysql + "§cFailed §7to hook §aDatabase Services");
    	Bukkit.getConsoleSender().sendMessage(mysql + "§cMySQL Driver not found! Please use §eJava 8 or above");
	}

	public static void getDebug(SQLException e) {
		Bukkit.getConsoleSender().sendMessage("§8==================================================");
		Bukkit.getConsoleSender().sendMessage(mysql + "§cFailed §7to hook §aDatabase Services");
		Bukkit.getConsoleSender().sendMessage(mysql + "§4§lSQLState: " + e.getSQLState());
		Bukkit.getConsoleSender().sendMessage(mysql + "§4§lPossible Issues: " + getMySQLError(e.getSQLState()));
		Bukkit.getConsoleSender().sendMessage("§8==================================================");
	}

	public static String getMySQLError(String sqlState) {
		if(sqlState.matches("08S01")) {
			return "\n - Cant get hostname for your address! \n - Bad handshake \n - Unknown command \n - Server shutdown in progress "
					+ "\n - Cant create IP socket \n - Aborted connection to db: " + database + " \n - Got a packet bigger than 'max_allowed_packet' bytes "
							+ "\n - Got a read error from connection pipe \n - Got packets out of order \n - Couldnt uncompress communication packet "
							+ "\n - Got an error reading communication packets \n - Got timeout reading communication packets "
							+ "\n - Got an error writing communication packets \n - Got timeout writing communication packets";
		}
		if(sqlState.matches("HY000")) {
			return "\n - Server couldnt process an action by the user";
		}
		if(sqlState.matches("HY001")) {
			return "\n - Out of memory; restart server and try again \n - Out of sort memory, consider increasing server sort buffer size";
		}
		if(sqlState.matches("8004")) {
			return "\n - Too many connections";
		}
		if(sqlState.matches("28000")) {
			return "\n - Access was denied for user " + username;
		}
		if(sqlState.matches("42000")) {
			return "\n - Action by the user was denied from the server \n - Syntax Error in any MySQL Statement";
		}
		return "Unknown Error!";
	}

	// Returns boolean if MySQL isConnected
    public static boolean isConnected() {
        return (getCon() == null ? false : true);
    }
    
    public static void disconnect() {
        if(isConnected()) {
            Bukkit.getConsoleSender().sendMessage(mysql + "§7Detaching from §cDatabase Services§7...");
            try {
                getCon().close();
                Bukkit.getConsoleSender().sendMessage(mysql + "§aSuccessfully §7detached from §aDatabase " + database);
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(mysql + "§cFailed §7to detach from §aDatabase Services");
                Bukkit.getConsoleSender().sendMessage(mysql + "§7May the service is not connected...");
                Bukkit.getConsoleSender().sendMessage(mysql + "§7Please check your database host!");
            }
        }
    }
    
    // Creates MySQL Table
    public static void createTable() {
        if(isConnected()) {
        	try {
            	PreparedStatement st1 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_playerdb(id INT(6) AUTO_INCREMENT UNIQUE, UUID VARCHAR(255), PLAYERNAME VARCHAR(255), BANS INT(6), MUTES INT(6), REPORTS INT(6), WARNS INT(6), LAST_KNOWN_IP VARCHAR(255), LAST_ONLINE LONG)");
            	st1.executeUpdate();
            	PreparedStatement st2 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_reportsdb(id INT(6) AUTO_INCREMENT UNIQUE, REPORTER_UUID VARCHAR(255), REPORTED_UUID VARCHAR(255), REASON VARCHAR(255), TEAM_UUID VARCHAR(255), STATUS INT(6), reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
            	st2.executeUpdate();
            	PreparedStatement st3 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_bansdb(id INT(6) AUTO_INCREMENT UNIQUE, BANNED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), BAN_END LONG)");
            	st3.executeUpdate();
            	PreparedStatement st4 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_mutesdb(id INT(6) AUTO_INCREMENT UNIQUE, MUTED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), MUTE_END LONG)");
            	st4.executeUpdate();
                PreparedStatement st5 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_actionsdb(id INT(6) AUTO_INCREMENT UNIQUE, ACTION VARCHAR(255), EXECUTOR_UUID VARCHAR(255), DESCRIPTION VARCHAR(255))");
                st5.executeUpdate();
            	PreparedStatement st6 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_reasonsdb(id INT(6) AUTO_INCREMENT UNIQUE, TYPE VARCHAR(255), NAME VARCHAR(255), BAN_LENGTH LONG, REPORT_ITEM VARCHAR(255))");
            	st6.executeUpdate();
            	PreparedStatement st7 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_materialsdb(id INT(6) AUTO_INCREMENT UNIQUE, TYPE VARCHAR(255) UNIQUE)");
            	st7.executeUpdate();
            	PreparedStatement st9 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_messages(id INT(6) AUTO_INCREMENT UNIQUE, SENDER_UUID VARCHAR(255), MESSAGE VARCHAR(256), reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
            	st9.executeUpdate();
            	PreparedStatement st10 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_ipbans(id INT(6) AUTO_INCREMENT UNIQUE, IP_ADDRESS VARCHAR(255), END LONG)");
            	st10.executeUpdate();
            	PreparedStatement st11 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_banhistory(id INT(6) AUTO_INCREMENT UNIQUE, BANNED_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255), BAN_START LONG, BAN_END LONG)");
            	st11.executeUpdate();
            	PreparedStatement st12 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_warnsdb(id INT(6) AUTO_INCREMENT UNIQUE, UUID VARCHAR(255), TEAM_UUID VARCHAR(255), REASON VARCHAR(255))");
            	st12.executeUpdate();
            	PreparedStatement st13 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_ticketdb(id INT(6) AUTO_INCREMENT UNIQUE, CREATOR_UUID VARCHAR(255), TEAM_UUID VARCHAR(255), reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
            	st13.executeUpdate();
            	PreparedStatement st14 = getCon().prepareStatement("CREATE TABLE IF NOT EXISTS ReportSystem_ticketdb_messages(id INT(6) AUTO_INCREMENT UNIQUE, TICKET_ID INT(6), COMMENT TEXT, AUTHOR VARCHAR(255), POSTED VARCHAR(255))");
            	st14.executeUpdate();
                Bukkit.getConsoleSender().sendMessage(mysql + "§aSuccessfully §7created/loaded §aMySQL-Table");
            } catch (SQLException e) {
            	if(Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
            		getDebug(e);
            	} else {
            		Bukkit.getConsoleSender().sendMessage(mysql + "§cERROR while creating/loading MySQL-Table!");
            	}
               
            }
        	
        	MySQL.updateTable("ALTER TABLE ReportSystem_playerdb ADD LAST_ONLINE LONG");
        	MySQL.updateTable("ALTER TABLE ReportSystem_playerdb ADD WARNS INT(6)");
        	MySQL.updateTable("ALTER TABLE ReportSystem_playerdb ADD PROTECTED INT(6)");
        	MySQL.updateTable("ALTER TABLE ReportSystem_ipbans ADD PLAYERNAME VARCHAR(255)");
        	MySQL.updateTable("ALTER TABLE ReportSystem_reportsdb ADD reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
        }
    }
    public static void update(String qry) {
        if(isConnected()) {
        	new BukkitRunnable() {
				@Override
				public void run() {
					try {
						getCon().createStatement().executeUpdate(qry);
					} catch (SQLException e) {
		            	if(Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
		            		getDebug(e);
		            	} else {
		            		Bukkit.getConsoleSender().sendMessage(mysql + "§cERROR while updating MySQL-Table");
		            	}
					}
				}
            	
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
    
    public static void updateMaterial(Material mat) {
        if(isConnected()) {
        	new BukkitRunnable() {
				@Override
				public void run() {
					try {
						if(!SystemManager.existsMaterial(mat.toString())) {
							getCon().createStatement().executeUpdate("INSERT INTO ReportSystem_materialsdb(TYPE) VALUES ('"+ mat.toString() +"')");
						}
					} catch (SQLException e) {
		            	if(Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
		            		getDebug(e);
		            	} else {
		            		Bukkit.getConsoleSender().sendMessage(mysql + "§cERROR while updating MySQL-Table");
		            	}
					}
				}
            	
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
    
    public static void updateTable(String qry) {
        if(isConnected()) {
            try {
                getCon().createStatement().executeUpdate(qry);
                
            } catch (SQLException e) {
            	
            }
        }
    }
    
    public static ResultSet getResult(String qry) {        
        if(isConnected()) {
            try {
            	return getCon().createStatement().executeQuery(qry);
            } catch (SQLException e) {
            	if(Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
            		getDebug(e);
            	} else {
            		Bukkit.getConsoleSender().sendMessage(mysql + "§cERROR while collecting data from MySQL-Table");
            		Bukkit.getConsoleSender().sendMessage(mysql + "§8(§7Query: §e" + qry + "§8)");
            	}
            	
            }
        }
        return null;
    }
    
    public static ResultSet getResultSync(String qry) {        
        if(isConnected()) {
            try {
            	return getCon().createStatement().executeQuery(qry);
            } catch (SQLException e) {
            	if(Main.getInstance().getConfig().getBoolean("General.MySQL.Debug")) {
            		getDebug(e);
            	} else {
            		Bukkit.getConsoleSender().sendMessage(mysql + "§7You didn't set up the Web-UI, please §cdisable Sync-with-WebInterface §7or §csetup the WebInterface");
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
