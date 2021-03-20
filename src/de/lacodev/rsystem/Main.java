package de.lacodev.rsystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.lacodev.rsystem.commands.*;
import de.lacodev.rsystem.completer.*;
import de.lacodev.rsystem.objects.BanManagerPlayerInput;
import de.lacodev.rsystem.objects.ReasonEDuration;
import de.lacodev.rsystem.objects.ReasonRename;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.lacodev.rsystem.listeners.Listener_Chat;
import de.lacodev.rsystem.listeners.Listener_ChatLog;
import de.lacodev.rsystem.listeners.Listener_GuardianDMG;
import de.lacodev.rsystem.listeners.Listener_Inventories;
import de.lacodev.rsystem.listeners.Listener_JoinQuit;
import de.lacodev.rsystem.listeners.Listener_Login;
import de.lacodev.rsystem.listeners.Listener_Matrix;
import de.lacodev.rsystem.listeners.Listener_NoConnection;
import de.lacodev.rsystem.listeners.Listener_PanelManager;
import de.lacodev.rsystem.listeners.Listener_Spartan;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.AntiMCLeaksHandler;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.rsystem.utils.SystemManager;
import de.lacodev.rsystem.utils.TranslationHandler;
import me.vagdedes.spartan.api.API;
import me.vagdedes.spartan.system.Enums.HackType;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{

	public static Main instance;
	private static Economy econ = null;
	
	public static String prefix;
	
	public double lizenz;
	public double version;
	
	public boolean latest = false;
	public boolean experimental = false;
	public boolean actionbar = false;
	public boolean bansystem = true;
	
	public static AntiMCLeaksHandler antimcleakshandler;
	public static TranslationHandler translator;
	
	public String host;
	public String port;
	public String database;
	public String username;
	public String password;
	
	public PluginManager pm = Bukkit.getPluginManager();
	public API api = null;
	
	public ArrayList<String> matrix_hacktypes = new ArrayList<>();
	public static ArrayList<BanManagerPlayerInput> banManagerPlayerInputs = new ArrayList<>();
	public static ArrayList<ReasonRename> reasonRename = new ArrayList<>();
	public static ArrayList<ReasonEDuration> reasonEDurations = new ArrayList<>();

	
	public void onEnable() {
		instance = this;
		antimcleakshandler = new AntiMCLeaksHandler();
		translator = new TranslationHandler();
		
		try {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Checking version... " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + this.getDescription().getVersion() + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
			checkLizenz(new URL("https://api.lacodev.de/staffcore/versions/"));
		} catch (MalformedURLException e) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Versioncheck" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
		}
		
		loadConfigs();
		applyConfigs();
		
		if(getConfig().getBoolean("General.Include-Vault")) {
			if(setupEconomy()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added Vault to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add Vault to our system! Please make sure you have Vault installed!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-ActionBarAPI")) {
			if(setupActionBar()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added ActionBarAPI to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add ActionBarAPI to our system! Please make sure you have ActionBarAPI installed!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-MatrixAntiCheat")) {
			if(setupMatrix()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added Matrix to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add Matrix to our system! Please make sure you have Matrix installed and set up properly!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-SpartanAntiCheat")) {
			if(setupSpartanAC()) {
				api = new API();
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added SpartanAntiCheat to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add SpartanAntiCheat to our system! Please make sure you have SpartanAntiCheat installed and set up properly!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		MySQL.connect();
		MySQL.createTable();
		
		for(Material mat : Material.values()) {
			MySQL.updateMaterial(mat);
		}
		
		if(MySQL.isConnected()) {
			if(initBanSystemDetectionService()) {
				registerCommands();
				registerEvents();
				startSync();
				startAntiMcLeaksServices();
				
				for(Sound s : Sound.values()) {
					BanManager.sound_enums.add(s.name().toUpperCase());
				}
				
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Environment: " + ChatColor.GRAY + Bukkit.getVersion());
				if(experimental) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
				} else if(latest) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
				} else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
				}
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "" + ChatColor.BOLD + "SUCCESS " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "PLUGIN STARTED" + ChatColor.DARK_GRAY + ")");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Environment: " + Bukkit.getVersion());
				if(experimental) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
				} else if(latest) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
				} else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
				}
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "" + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "BanSystemDetectionService CRASHED" + ChatColor.DARK_GRAY + ")");
				Bukkit.getConsoleSender().sendMessage("");
			}
		} else {
			pm.registerEvents(new Listener_NoConnection(), this);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					if(!MySQL.isConnected()) {
						Bukkit.getConsoleSender().sendMessage("");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "" + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "NO CONNECTION" + ChatColor.DARK_GRAY + ")");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Please make sure to set up your database correctly!");
						Bukkit.getConsoleSender().sendMessage("");
					}
				}
			}.runTaskTimerAsynchronously(this, 0, 30*20);
			
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Environment: " + ChatColor.GRAY + Bukkit.getVersion());
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "" + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "NO CONNECTION" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
		}
	}
	
	private void startAntiMcLeaksServices() {
		if(getConfig().getBoolean("MCLeaks-Blocker.Enable")) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.DARK_GRAY + "Activating MCLeaks-Blocker...");
			
			if(getConfig().getBoolean("MCLeaks-Blocker.Cache-Updater.Enable")) {
				new BukkitRunnable() {

					@Override
					public void run() {
						getAntiMCLeaksHandler().cacheAccounts();
					}
					
				}.runTaskTimerAsynchronously(instance, 0, getConfig().getInt("MCLeaks-Blocker.Cache-Updater.Period-In-Minutes") * 20 * 60);
			} else {
				getAntiMCLeaksHandler().cacheAccounts();
			}
		}
	}

	public void onReload() {
		
		loadConfigs();
		applyConfigs();
		
		try {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Checking version... " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + ""+ this.getDescription().getVersion() +"" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
			checkLizenz(new URL("https://api.lacodev.de/staffcore/versions/"));
		} catch (MalformedURLException e) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "" + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Versioncheck" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
		}
		
		if(getConfig().getBoolean("General.Include-Vault")) {
			if(setupEconomy()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added Vault to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add Vault to our system! Please make sure you have Vault installed!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-ActionBarAPI")) {
			if(setupActionBar()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added ActionBarAPI to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add ActionBarAPI to our system! Please make sure you have ActionBarAPI installed!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-MatrixAntiCheat")) {
			if(setupMatrix()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added Matrix to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add Matrix to our system! Please make sure you have Matrix installed and set up properly!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-SpartanAntiCheat")) {
			if(setupSpartanAC()) {
				api = new API();
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added SpartanAntiCheat to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "Failed " + ChatColor.GRAY + "to add SpartanAntiCheat to our system! Please make sure you have SpartanAntiCheat installed and set up properly!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		MySQL.connect();
		MySQL.createTable();
		
		if(MySQL.isConnected()) {
			if(initBanSystemDetectionService()) {
				startAntiMcLeaksServices();
				
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Environment: " + ChatColor.GRAY + Bukkit.getVersion());
				if(experimental) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
				} else if(latest) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
				} else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
				}
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + ChatColor.BOLD + "SUCCESS " + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "PLUGIN STARTED" + ChatColor.DARK_GRAY + ")");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Environment: " + Bukkit.getVersion());
				if(experimental == true) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.LIGHT_PURPLE + "EXPERIMENTAL" + ChatColor.DARK_GRAY + ")");
				} else if(latest == true) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.GREEN + "LATEST" + ChatColor.DARK_GRAY + ")");
				} else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore: " + ChatColor.GRAY + "v" + this.getDescription().getVersion() + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "OUTDATED" + ChatColor.DARK_GRAY + ")");
				}
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "" + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "BanSystemDetectionService CRASHED" + ChatColor.DARK_GRAY + ")");
				Bukkit.getConsoleSender().sendMessage("");
			}
		} else {
			pm.registerEvents(new Listener_NoConnection(), this);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					if(!MySQL.isConnected()) {
						Bukkit.getConsoleSender().sendMessage("");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "" + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "NO CONNECTION" + ChatColor.DARK_GRAY + ")");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Please make sure to set up your database correctly!");
						Bukkit.getConsoleSender().sendMessage("");
					}
				}
			}.runTaskTimerAsynchronously(this, 0, 30*20);
			
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Environment: " + ChatColor.GRAY + Bukkit.getVersion());
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "" + ChatColor.BOLD + "ERROR " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "NO CONNECTION" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
		}
	}
	
	public void startSync() {
		if(getConfig().getBoolean("General.Sync-with-WebInterface")) {
			new BukkitRunnable() {

				@Override
				public void run() {
					
					ResultSet rs = MySQL.getResultSync("SELECT * FROM staffcoreui_sync");
					
					try {
						while(rs.next()) {
							if(rs.getString("SYNC_TYPE").matches("BAN_PLAYER")) {
								BanManager.submitBan(rs.getString("TARGET_UUID"), rs.getString("DESCRIPTION"), rs.getString("EXECUTOR_UUID"));
								MySQL.update("DELETE FROM staffcoreui_sync WHERE id = '"+ rs.getInt("id") +"'");
							}
							if(rs.getString("SYNC_TYPE").matches("MUTE_PLAYER")) {
								BanManager.submitMute(rs.getString("TARGET_UUID"), rs.getString("DESCRIPTION"), rs.getString("EXECUTOR_UUID"));
								MySQL.update("DELETE FROM staffcoreui_sync WHERE id = '"+ rs.getInt("id") +"'");
							}
							if(rs.getString("SYNC_TYPE").matches("CLAIM_REPORT")) {
								Player team = Bukkit.getPlayer(rs.getString("EXECUTOR_UUID"));
								if(team != null) {
									ReportManager.claimReport(team, rs.getString("TARGET_UUID"));
									MySQL.update("DELETE FROM staffcoreui_sync WHERE id = '"+ rs.getInt("id") +"'");
								}
							}
							if(rs.getString("SYNC_TYPE").matches("VERIFY_WEBACCOUNT")) {
								Player team = Bukkit.getPlayer(rs.getString("EXECUTOR_UUID"));
								if(team != null) {
									SystemManager.sendVerificationToPlayer(team);
								}
							}
						}
					} catch (SQLException e) {
						Bukkit.getConsoleSender().sendMessage("");
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "Problem with staffcoreui_sync detected!");
						Bukkit.getConsoleSender().sendMessage("");
					}
				}
			}.runTaskTimerAsynchronously(getInstance(), 5*20, 5*20);

			new BukkitRunnable() {

				@Override
				public void run() {
					long current = System.currentTimeMillis();
					ResultSet unbancheck = MySQL.getResult("SELECT * FROM ReportSystem_bansdb");
					
					try {
						while(unbancheck.next()) {
							if(unbancheck.getLong("BAN_END") != -1) {
								if(unbancheck.getLong("BAN_END") < current) {
									BanManager.unban(unbancheck.getString("BANNED_UUID"));
									BanManager.sendConsoleNotify("UNBAN", unbancheck.getString("MUTED_UUID"));
								}
							}
						}
					} catch (SQLException e) {
						
					}
					
					ResultSet unmutecheck = MySQL.getResult("SELECT * FROM ReportSystem_mutesdb");
					
					try {
						while(unmutecheck.next()) {
							if(unmutecheck.getLong("MUTE_END") != -1) {
								if(unmutecheck.getLong("MUTE_END") < current) {
									BanManager.unmute(unmutecheck.getString("MUTED_UUID"));
									BanManager.sendConsoleNotify("UNMUTE", unmutecheck.getString("MUTED_UUID"));
								}
							}
						}
					} catch (SQLException e) {
						
					}

				}
			}.runTaskTimerAsynchronously(getInstance(), 5*20, 5*20);
		}
	}

	public boolean initBanSystemDetectionService() {
		
		if(getInstance().getConfig().getBoolean("Ban-System.Enable") == false) {
			bansystem = false;
			
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore BanSystem " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "DEACTIVATED" + ChatColor.DARK_GRAY + ")");
		}
		
		ArrayList<String> detectservice = new ArrayList<>();
		
		detectservice.add("LiteBans");
		detectservice.add("ProfessionalBans Reloaded");
		detectservice.add("AdvancedBan");
		detectservice.add("BansPlus");
		detectservice.add("maxbans-plus");
		detectservice.add("BanSystem-Bukkit");
		
		for(Plugin p : Bukkit.getPluginManager().getPlugins()) {
			if(detectservice.contains(p.getName())) {
				if(getInstance().getConfig().getBoolean("General.Activate-BanSystem-Detection")) {
					Bukkit.getConsoleSender().sendMessage("");
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Detected BanSystem " + ChatColor.DARK_GRAY + "(" + ChatColor.LIGHT_PURPLE + ""+ p.getName() +"" + ChatColor.DARK_GRAY + ")");
					deactivateBanningSystem();
				}
				return true;
			} else {
				return true;
			}
		}
		return false;
		
	}

	private void deactivateBanningSystem() {
		bansystem = false;

		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» StaffCore BanSystem " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + "DEACTIVATED" + ChatColor.DARK_GRAY + ")");
	}

	public boolean setupMatrix() {
		if (getServer().getPluginManager().getPlugin("Matrix") == null) {
			return false;
		} else {

			for(me.rerere.matrix.api.HackType matrix : me.rerere.matrix.api.HackType.values()) {
				matrix_hacktypes.add(matrix.name().toUpperCase());
			}

			return true;
		}
	}

	public boolean setupActionBar() {
        if (getServer().getPluginManager().getPlugin("ActionBarAPI") == null) {
            return false;
        } else {
        	actionbar = true;
        	return true;
        }
	}
	
	public boolean setupSpartanAC() {
        if (getServer().getPluginManager().getPlugin("SpartanAPI") == null && getServer().getPluginManager().getPlugin("Spartan") == null) {
            return false;
        } else {
        	return true;
        }
	}

	private void registerEvents() {
		pm.registerEvents(new Listener_JoinQuit(), this);
		pm.registerEvents(new Listener_Inventories(), this);
		pm.registerEvents(new Listener_Login(), this);
		pm.registerEvents(new Listener_Chat(), this);
		pm.registerEvents(new Listener_ChatLog(), this);
		pm.registerEvents(new Listener_GuardianDMG(), this);
		pm.registerEvents(new Listener_PanelManager(), this);
		if(setupMatrix()) {
			pm.registerEvents(new Listener_Matrix(), this);
		}
		if(setupSpartanAC()) {
			pm.registerEvents(new Listener_Spartan(), this);
		}

	}

	private void registerCommands() {
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
		
		if(bansystem == true) {
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
	}

	public void onDisable() {
		MySQL.disconnect();
	}

	private void loadConfigs() {
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB Generating... " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + "config.yml" + ChatColor.DARK_GRAY + ")");
		Bukkit.getConsoleSender().sendMessage("");
		
		getConfig().options().copyDefaults(true);
		
		getConfig().addDefault("General.System-Prefix", "&cSystem &8%double_arrow% ");
		getConfig().addDefault("General.Custom-Messages.Enable", false);
		getConfig().addDefault("General.Custom-Messages.Rest-API-Key", "{Your API-Key from the Dashboard}");
		getConfig().addDefault("General.Language", "de");
		getConfig().addDefault("General.MySQL.Debug", true);
		getConfig().addDefault("General.Activate-BanSystem-Detection", false);
		getConfig().addDefault("General.Include-Worldname", false);
		getConfig().addDefault("General.Include-Vault", false);
		getConfig().addDefault("General.Include-ActionBarAPI", false);
		getConfig().addDefault("General.Include-MatrixAntiCheat", false);
		getConfig().addDefault("General.Include-SpartanAntiCheat", false);
		getConfig().addDefault("General.Force-SpectatorMode", true);
		getConfig().addDefault("General.Sync-with-WebInterface", false);
		
		getConfig().addDefault("MCLeaks-Blocker.Enable", true);
		getConfig().addDefault("MCLeaks-Blocker.Cache-Updater.Enable", true);
		getConfig().addDefault("MCLeaks-Blocker.Cache-Updater.Period-In-Minutes", 10);
		
		getConfig().addDefault("Ban-System.Enable", true);
		getConfig().addDefault("Ban-System.Per-Reason-Permission.Enable", true);
		getConfig().addDefault("Ban-System.Per-Reason-Permission.Prefix-For-Permission", "rsystem.ban.reason.");
		
		getConfig().addDefault("Mute-System.Per-Reason-Permission.Enable", true);
		getConfig().addDefault("Mute-System.Per-Reason-Permission.Prefix-For-Permission", "rsystem.mute.reason.");
		
		getConfig().addDefault("Ban-Animation.Enable", true);
		getConfig().addDefault("Ban-Animation.Type", "GUARDIAN");
		getConfig().addDefault("Ban-Animation.Sound", "ENTITY_ENDER_DRAGON_DEATH");
		
		getConfig().addDefault("Vault.Rewards.Report.MIN", 50);
		getConfig().addDefault("Vault.Rewards.Report.MAX", 150);
		
		getConfig().addDefault("Report-Spam.Duration-in-Seconds", 20);
		
		getConfig().addDefault("TicketSystem.Cooldown-In-Seconds", 120);
		getConfig().addDefault("TicketSystem.WebInterface-URL", "https://yourserver.com/Web-UI-v3.5.0/dashboard/tickets/view/?id=");
		
		getConfig().addDefault("MatrixAntiCheat.Autoreport.Enable", false);
		getConfig().addDefault("MatrixAntiCheat.Autoreport.Name", "MatrixAC");
		getConfig().addDefault("MatrixAntiCheat.Autoreport.Log.Reset-Violations-On-Join", true);
		if(setupMatrix()) {
			for(me.rerere.matrix.api.HackType matrix : me.rerere.matrix.api.HackType.values()) {
				getConfig().addDefault("MatrixAntiCheat.Autoreport." + matrix.toString() + ".Displayname", "" + matrix.toString().substring(0, 1).toUpperCase() + matrix.toString().substring(1).toLowerCase());
				getConfig().addDefault("MatrixAntiCheat.Autoreport." + matrix.toString() + ".Violationslevel", 20);
			}
		}
		
		getConfig().addDefault("SpartanAntiCheat.Autoreport.Enable", false);
		getConfig().addDefault("SpartanAntiCheat.Autoreport.Name", "SpartanAC");
		getConfig().addDefault("SpartanAntiCheat.Autoreport.Log.Reset-Violations-On-Join", true);
		if(setupSpartanAC()) {
			for(HackType h : HackType.values()) {
				getConfig().addDefault("SpartanAntiCheat.Autoreport."+ h.toString() + ".Displayname", "" + h.toString().substring(0, 1).toUpperCase() + h.toString().substring(1).toLowerCase());
				getConfig().addDefault("SpartanAntiCheat.Autoreport."+ h.toString() + ".Violationslevel", 20);
			}
		}
		
		getConfig().addDefault("Unpermitted-OP.Kick-Player.Enable", true);
		
		getConfig().addDefault("IP-Ban.Ban-IP-When-Player-Gets-Banned", false);
		getConfig().addDefault("IP-Ban.Duration-in-Hours", 24);
		
		getConfig().addDefault("Permissions.Everything", "rsystem.*");
		getConfig().addDefault("Permissions.System.Notify", "rsystem.notify");
		getConfig().addDefault("Permissions.System.Adminpanel", "rsystem.adminpanel");
		getConfig().addDefault("Permissions.Allow-OP.Join", "rsystem.op.allow");
		getConfig().addDefault("Permissions.Allow-OP.Notify", "rsystem.op.notify");
		getConfig().addDefault("Permissions.IpBan.Use", "rsystem.ipban.use");
		getConfig().addDefault("Permissions.IpBan.Notify", "rsystem.ipban.notify");
		getConfig().addDefault("Permissions.IpUnBan.Use", "rsystem.ipunban.use");
		getConfig().addDefault("Permissions.IpUnBan.Notify", "rsystem.ipunban.notify");
		getConfig().addDefault("Permissions.Report.Notify", "rsystem.report.notify");
		getConfig().addDefault("Permissions.Report.Claim", "rsystem.report.claim");
		getConfig().addDefault("Permissions.Report.Spam.Bypass", "rsystem.report.spam.bypass");
		getConfig().addDefault("Permissions.ReportManager.addreason", "rsystem.reportmanager.addreason");
		getConfig().addDefault("Permissions.ReportManager.removereason", "rsystem.reportmanager.removereason");
		getConfig().addDefault("Permissions.Reports.See", "rsystem.reports.see");
		getConfig().addDefault("Permissions.Check.Use", "rsystem.check.use");
		getConfig().addDefault("Permissions.BanManager.addreason", "rsystem.banmanager.addreason");
		getConfig().addDefault("Permissions.BanManager.removereason", "rsystem.banmanager.removereason");
		getConfig().addDefault("Permissions.BanManager.editreason", "rsystem.banmanager.editreason");
		getConfig().addDefault("Permissions.Ban.Use", "rsystem.ban.use");
		getConfig().addDefault("Permissions.Ban.Notify", "rsystem.ban.notify");
		getConfig().addDefault("Permissions.Ban.List", "rsystem.ban.list");
		getConfig().addDefault("Permissions.Mute.Use", "rsystem.mute.use");
		getConfig().addDefault("Permissions.Mute.Notify", "rsystem.mute.notify");
		getConfig().addDefault("Permissions.Mute.Global.Change", "rsystem.mute.global.change");
		getConfig().addDefault("Permissions.Mute.Global.Write", "rsystem.mute.global.write");
		getConfig().addDefault("Permissions.UnBan.Use", "rsystem.unban.use");
		getConfig().addDefault("Permissions.UnBan.Notify", "rsystem.unban.notify");
		getConfig().addDefault("Permissions.UnMute.Use", "rsystem.unmute.use");
		getConfig().addDefault("Permissions.UnMute.Notify", "rsystem.unmute.notify");
		getConfig().addDefault("Permissions.Chatfilter.Manage", "rsystem.chatfilter.manage");
		getConfig().addDefault("Permissions.ChatLog.Notify", "rsystem.chatlog.notify");
		getConfig().addDefault("Permissions.BanHistory.See", "rsystem.banhistory.see");
		getConfig().addDefault("Permissions.CheckAlts.Use", "rsystem.checkalts.use");
		getConfig().addDefault("Permissions.Warn.Use", "rsystem.warn.use");
		getConfig().addDefault("Permissions.Warn.Notify", "rsystem.warn.notify");
		getConfig().addDefault("Permissions.Warns.See", "rsystem.warns.see");
		getConfig().addDefault("Permissions.ChatLogs.Use", "rsystem.chatlogs.use");
		getConfig().addDefault("Permissions.Chat.Clear", "rsystem.chatclear.clear");
		getConfig().addDefault("Permissions.Chat.Bypass", "rsystem.chatclear.bypass");
		getConfig().addDefault("Permissions.Bugs.Notify", "rsystem.bugs.notify");
		getConfig().addDefault("Permissions.Bugs.See", "rsystem.bugs.see");

		getConfig().addDefault("Permissions.BroadCast.Send", "rsystem.broadcast.send");
		getConfig().addDefault("Permissions.StaffChat.Read", "rsystem.staffchat.read");
		getConfig().addDefault("Permissions.StaffChat.Write", "rsystem.staffchat.write");
		getConfig().addDefault("Permissions.Maintenance.Change", "rsystem.maintenance.change");
		getConfig().addDefault("Permissions.Maintenance.Join", "rsystem.maintenance.join");

		getConfig().addDefault("Chatfilter.ReporterName", "ChatController");
		getConfig().addDefault("Chatfilter.Cursed-Words.Match-for-action-in-Percentage", 50);
		getConfig().addDefault("Chatfilter.Cursed-Words.AutoReport.Enable", true);
		getConfig().addDefault("Chatfilter.Cursed-Words.AutoReport.Reason", "Insult");
		getConfig().addDefault("Chatfilter.Cursed-Words.Block-Message.Enable", true);
		getConfig().addDefault("Chatfilter.Advertisment.AutoReport.Enable", true);
		getConfig().addDefault("Chatfilter.Advertisment.AutoReport.Reason", "Advertisment");
		getConfig().addDefault("Chatfilter.Advertisment.Block-Message.Enable", true);

		
		ArrayList<String> whitelist = new ArrayList<>();
		
		whitelist.add("yourserver.de");
		whitelist.add("forum.yourserver.de");
		whitelist.add("ts.yourserver.de");
		whitelist.add("shop.yourserver.de");
		
		getConfig().addDefault("Chatfilter.Advertisment.Whitelist", whitelist);
		
		ArrayList<String> blocked = new ArrayList<>();
		
		blocked.add(".ac");
		blocked.add(".ad");
		blocked.add(".ae");
		blocked.add(".aero");
		blocked.add(".af");
		blocked.add(".ag");
		blocked.add(".ai");
		blocked.add(".al");
		blocked.add(".am");
		blocked.add(".an");
		blocked.add(".am");
		blocked.add(".an");
		blocked.add(".ao");
		blocked.add(".aq");
		blocked.add(".ar");
		blocked.add(".as");
		blocked.add(".asia");
		blocked.add(".at");
		blocked.add(".au");
		blocked.add(".aw");
		blocked.add(".ax");
		blocked.add(".az");
		blocked.add(".de");
		blocked.add(".com");
		blocked.add(".net");
		blocked.add(".org");
		blocked.add(".eu");
		blocked.add(".be");
		blocked.add(".nl");
		blocked.add(".me");
		blocked.add(".ch");
		blocked.add(".info");
		blocked.add(".dev");
		
		getConfig().addDefault("Chatfilter.Advertisment.Blocked-Domains", blocked);
		
		saveConfig();
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Generated " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + "config.yml" + ChatColor.DARK_GRAY + ")");
		Bukkit.getConsoleSender().sendMessage("");
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Fetching... " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + "Translations from translate.lacodev.de" + ChatColor.DARK_GRAY + ")");
		Bukkit.getConsoleSender().sendMessage("");
		
		translator.init();
		
		translator.fetch(getConfig().getString("General.Language").substring(0, 2));
		
		if(getConfig().getBoolean("General.Custom-Messages.Enable")) {
			translator.fetchCustom(getConfig().getString("General.Custom-Messages.Rest-API-Key"));
		}

		
		File mysql = new File("plugins//" + this.getDescription().getName() + "//mysql.yml");
		YamlConfiguration mcfg = YamlConfiguration.loadConfiguration(mysql);
		
		if(!mysql.exists()) {
			mcfg.set("MySQL.HOST", "host");
			mcfg.set("MySQL.PORT", "3306");
			mcfg.set("MySQL.DATABASE", "database");
			mcfg.set("MySQL.USERNAME", "username");
			mcfg.set("MySQL.PASSWORD", "password");
			
			try {
				mcfg.save(mysql);
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Generated " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + "mysql.yml" + ChatColor.DARK_GRAY + ")");
				Bukkit.getConsoleSender().sendMessage("");
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "ERROR while generating file " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + "mysql.yml" + ChatColor.DARK_GRAY + ")");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
	}

	public void applyConfigs() {
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Applying values... " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + "config.yml" + ChatColor.DARK_GRAY + ")");
		Bukkit.getConsoleSender().sendMessage("");
		
		host = getMySQLFile().getString("MySQL.HOST");
		port = getMySQLFile().getString("MySQL.PORT");
		database = getMySQLFile().getString("MySQL.DATABASE");
		username = getMySQLFile().getString("MySQL.USERNAME");
		password = getMySQLFile().getString("MySQL.PASSWORD");
		
		if(getConfig().getString("General.System-Prefix").contains("%double_arrow%")) {
			prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("General.System-Prefix").replace("%double_arrow%", "»"));
		} else {
			prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("General.System-Prefix"));
		}
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» Applied values " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + "config.yml" + ChatColor.DARK_GRAY + ")");
		Bukkit.getConsoleSender().sendMessage("");
		
	}
	
	public static String getMSG(String key) {
		
		return ChatColor.translateAlternateColorCodes('&', translator.getTranslation(key)).replace("%newline%", "\n");
		
	}
	
	public YamlConfiguration getMySQLFile() {
		File mysql = new File("plugins//" + this.getDescription().getName() + "//mysql.yml");
		YamlConfiguration mcfg = YamlConfiguration.loadConfiguration(mysql);
		
		if(mysql.exists()) {
			return mcfg;
		} else {
			return null;
		}
	}
	
	public YamlConfiguration getLanguageFile() {
		
		File lang = new File("plugins//" + getInstance().getDescription().getName() + "//lang//"+ getInstance().getConfig().getString("General.Language") +".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(lang);
		
		File flang = new File("plugins//" + getInstance().getDescription().getName() + "//lang//en_US.yml");
		YamlConfiguration fcfg = YamlConfiguration.loadConfiguration(flang);
		
		if(lang.exists()) {
			return cfg;
		} else if(flang.exists()) {
			return fcfg;
		} else {
			return null;
		}
	}
	
	public static String getPermissionNotice(String config) {
		if(getInstance().getConfig().getString(config) != null) {
			return ChatColor.stripColor(getInstance().getConfig().getString(config));
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Message Output" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
			return "Permission failed to load";
		}
	}
	
	
	public JSONObject readJsonFromUrl(String url) {
		JSONParser parser = new JSONParser();
		
		try {
			
			JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(new URL(url + "?latest").openStream(), Charset.forName("UTF-8")));
			
			return jsonObject;
		} catch (IOException | ParseException | ClassCastException e) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + "" + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Versioncheck" + ChatColor.DARK_GRAY + ")");
			Bukkit.getConsoleSender().sendMessage("");
		}
		return null;
	}

	public void checkLizenz(URL url) {
		try {
			JSONObject ob = (JSONObject) readJsonFromUrl(url.toString()).get("1");
			String latestversion = (String) ob.get("version");
			
			lizenz = Double.valueOf(latestversion.replaceAll("\\D+", ""));
			version = Double.valueOf(this.getDescription().getVersion().replaceAll("\\D+", ""));
			
			if(version >= lizenz) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "" + ChatColor.BOLD + "SUCCESS " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Versioncheck" + ChatColor.DARK_GRAY + ")");
				if(version > lizenz) {
					experimental = true;
					Bukkit.getConsoleSender().sendMessage("");
					Bukkit.getConsoleSender().sendMessage("" + ChatColor.GRAY + "You are using an " + ChatColor.LIGHT_PURPLE + "experimental build" + ChatColor.GRAY + "!");
					Bukkit.getConsoleSender().sendMessage("");
				} else {
					latest = true;
					Bukkit.getConsoleSender().sendMessage("");
					Bukkit.getConsoleSender().sendMessage("" + ChatColor.GRAY + "You are using the " + ChatColor.GREEN + "latest build" + ChatColor.GRAY + "!");
					Bukkit.getConsoleSender().sendMessage("");
				}
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "" + ChatColor.BOLD + "SUCCESS " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Versioncheck" + ChatColor.DARK_GRAY + ")");
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There is an update available!");
				Bukkit.getConsoleSender().sendMessage("" + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "https://www.spigotmc.org/resources/staffcore-1-7-1-15.48655/updates");
				Bukkit.getConsoleSender().sendMessage("");
			}
		} catch(NullPointerException e) {
			
		}
	}
	
	public static String getPrefix() {
		return prefix;
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public static TranslationHandler getTranslator() {
    	return translator;
    }
    
    public static AntiMCLeaksHandler getAntiMCLeaksHandler() {
    	return antimcleakshandler;
    }
    
	public static Main getInstance() {
		return instance;
	}

	public static Economy getEconomy() {
		return econ;
	}
	
}
