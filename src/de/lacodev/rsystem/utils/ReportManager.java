package de.lacodev.rsystem.utils;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.TODO;
import de.lacodev.staffcore.api.events.ReportClaimEvent;
import de.lacodev.staffcore.api.events.ReportCreateEvent;
import de.lacodev.staffcore.api.events.ReportReasonCreateEvent;
import de.lacodev.staffcore.api.events.ReportReasonDeleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.objects.Reasons;
import me.rerere.matrix.api.HackType;
import me.rerere.matrix.api.MatrixAPI;
import me.rerere.matrix.api.MatrixAPIProvider;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ReportManager {
	
	public static Inventory reportinv;
	

	public static void openPagedReportInv(Player p, String name, int page) {

		String title = Main.getMSG("Messages.Report-System.Inventory.Title") + ChatColor.YELLOW + name;

		if(title.length() > 32) {

			title = title.substring(0, 32);

		}

		reportinv = p.getServer().createInventory(null, 54, title);





		if(MySQL.isConnected()) {

			ArrayList<Reasons> rows = new ArrayList<>();

			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'REPORT'");

			try {

				if(rs != null) {
					while(rs.next()) {

						rows.add(new Reasons(XMaterial.matchXMaterial(Material.getMaterial(rs.getString("REPORT_ITEM"))).parseMaterial(), rs.getString("NAME")));

					}
				}

			} catch (SQLException e) {

				e.printStackTrace();

			}



			for (int i2 = 0; i2 < 9; i2++) {
				reportinv.setItem(i2, Data.buildPlace());
			}

			if (PageManager.page.containsKey(p.getPlayer())) {

				PageManager.page.remove(p.getPlayer());

			}

			PageManager.page.put(p.getPlayer(), page);



			for (int i2 = 45; i2 < 54; i2++) {
				reportinv.setItem(i2, Data.buildPlace());
			}
			
			if (PageManager.isPageValid(rows, page - 1, 36)) {

				reportinv.setItem(46, Data.buildItem(XMaterial.LIME_STAINED_GLASS_PANE, Main.getMSG("Messages.Report-System.Inventory.PreviousPage.Available")));

			} else {

				reportinv.setItem(46, Data.buildItem(XMaterial.RED_STAINED_GLASS_PANE, Main.getMSG("Messages.Report-System.Inventory.PreviousPage.NotAvailable")));

			}

			if (PageManager.isPageValid(rows, page + 1, 36)) {

				reportinv.setItem(52, Data.buildItem(XMaterial.LIME_STAINED_GLASS_PANE, Main.getMSG("Messages.Report-System.Inventory.NextPage.Available")));

			} else {

				reportinv.setItem(52, Data.buildItem(XMaterial.RED_STAINED_GLASS_PANE, Main.getMSG("Messages.Report-System.Inventory.NextPage.NotAvailable")));

			}
			for (Reasons item : PageManager.getPageItems(rows, page, 36)) {

				reportinv.setItem(reportinv.firstEmpty(), Data.buildItemStack(item.getItem(), 1, 0, ChatColor.YELLOW + item.getName(), Main.getMSG("Messages.Report-System.Inventory.ReportItem-Lore.1").replace("%target%", name), Main.getMSG("Messages.Report-System.Inventory.ReportItem-Lore.2").replace("%reason%", item.getName())));

			}
		} else {

			reportinv.setItem(13, Data.buildItem(Material.BARRIER, 1, 0, ChatColor.RED + "No Connection"));

		}

		p.openInventory(reportinv);

	}
	
	public static ArrayList<String> getReportReasons() {
		ArrayList<String> reasons = new ArrayList<>();
		
		ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'REPORT'");
		
		try {
			if(rs != null) {
				while(rs.next()) {
					if(!reasons.contains(rs.getString("NAME").toLowerCase())) {
						reasons.add(rs.getString("NAME").toLowerCase());
					}
				}
			}
			return reasons;
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "MySQL" + ChatColor.DARK_GRAY + ") - " + ChatColor.RED + "ERROR while collecting data from MySQL-Table");
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "MySQL" + ChatColor.DARK_GRAY + ") - (" + ChatColor.GRAY + "Query: " + ChatColor.YELLOW + "getReportReasons" + ChatColor.DARK_GRAY + ")");
		}
		return reasons;
	}
	
	public static void createReport(String p, Player target, String reason) {
		if(!(p.matches(Main.getPermissionNotice("MatrixAntiCheat.Autoreport.Name")) || p.matches(Main.getPermissionNotice("Chatfilter.ReporterName"))  || p.matches(Main.getPermissionNotice("SpartanAntiCheat.Autoreport.Name")))) {
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
				try {
					PreparedStatement st = MySQL.getCon().prepareStatement("INSERT INTO ReportSystem_reportsdb(REPORTER_UUID,REPORTED_UUID,REASON,TEAM_UUID,STATUS) VALUES ('" + SystemManager.getUUIDByName(p) + "','" + target.getUniqueId().toString() + "','" + reason + "','null','0')");
					st.executeUpdate();

					new BukkitRunnable() {
						@Override
						public void run() {
							Bukkit.getServer().getPluginManager().callEvent(new ReportCreateEvent(SystemManager.getUUIDByName(p), target, reason));
						}
					}.runTask(Main.getInstance());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			
			addReport(target.getUniqueId().toString());

			Bukkit.getPlayer(p).sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.User.Report-Created"));
		} else {
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
				try {
					PreparedStatement st = MySQL.getCon().prepareStatement("INSERT INTO ReportSystem_reportsdb(REPORTER_UUID,REPORTED_UUID,REASON,TEAM_UUID,STATUS) VALUES ('" + p + "','" + target.getUniqueId().toString() + "','" + reason + "','null','0')");
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});


			Bukkit.getServer().getPluginManager().callEvent(new ReportCreateEvent(p, target, reason));

			addReport(target.getUniqueId().toString());
		}
		
		sendNotify("REPORT", p, target.getName(), reason);
	}

	private static void addReport(String uuid) {
		try {
			PreparedStatement st = MySQL.getCon().prepareStatement("UPDATE ReportSystem_playerdb SET REPORTS = '"+ (getReports(uuid) + 1) +"' WHERE UUID = '"+ uuid +"'");
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getReports(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT REPORTS FROM ReportSystem_playerdb WHERE UUID = '"+ uuid +"'");
		try {
			if(rs.next()) {
				return rs.getInt("REPORTS");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void sendNotify(String string, String p, String string2, String reason) {
		if(string.toUpperCase().equalsIgnoreCase("REPORT")) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(Main.getInstance().actionbar) {
					if(all.hasPermission(Main.getPermissionNotice("Permissions.Report.Notify")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
						ActionBarAPI.sendActionBar(all, Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.Team.Reported").replace("%player%", p).replace("%target%", string2) + ChatColor.DARK_GRAY +  " � " + ChatColor.YELLOW + reason, 60);
						if(Main.getInstance().getConfig().getBoolean("General.Include-MatrixAntiCheat")) {
							String matrixalert;
							
							MatrixAPI api = MatrixAPIProvider.getAPI();
							File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//logs//matrixlog.yml");
							YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
							if(Main.getInstance().matrix_hacktypes.contains(reason.toUpperCase())) {
								if(api.isEnable(HackType.valueOf(reason.toUpperCase()))) {
									matrixalert = ChatColor.GRAY + "VL " + ChatColor.DARK_GRAY + "� " + ChatColor.YELLOW + cfg.getInt("Log." + SystemManager.getUUIDByName(string2) + "." + HackType.valueOf(reason.toUpperCase()).toString().toUpperCase() + ".VL");
								} else {
									matrixalert = ChatColor.RED + "Matrix was unable to find any Violations!";
								}
								ActionBarAPI.sendActionBar(all, Main.getPrefix() + matrixalert, 120);
							}
						}
						if(Main.getInstance().getConfig().getBoolean("General.Include-Worldname")) {
							ActionBarAPI.sendActionBar(all, Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.Team.Worldname").replace("%worldname%", Bukkit.getPlayer(string2).getLocation().getWorld().getName()), 180);
						}
					}
					if(all.hasPermission(Main.getPermissionNotice("Permissions.Report.Claim")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
						TextComponent tc = new TextComponent();
						
						tc.setText(Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.Team.Teleport-Button"));
						tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/report claim " + SystemManager.getUUIDByName(string2)));
						
						all.sendMessage("");
						all.spigot().sendMessage(tc);
						all.sendMessage("");
					}
				} else {
					if(all.hasPermission(Main.getPermissionNotice("Permissions.Report.Notify")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
						all.sendMessage("");
						all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.Team.Reported").replace("%player%", p).replace("%target%", string2));
						all.sendMessage(Main.getPrefix() + ChatColor.YELLOW + reason);
						if(Main.getInstance().getConfig().getBoolean("General.Include-MatrixAntiCheat")) {
							String matrixalert;
							
							MatrixAPI api = MatrixAPIProvider.getAPI();
							File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//logs//matrixlog.yml");
							YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
							if(Main.getInstance().matrix_hacktypes.contains(reason.toUpperCase())) {
								if(api.isEnable(HackType.valueOf(reason.toUpperCase()))) {
									matrixalert = ChatColor.GRAY + "VL " + ChatColor.DARK_GRAY + "� " + ChatColor.YELLOW + cfg.getInt("Log." + SystemManager.getUUIDByName(string2) + "." + HackType.valueOf(reason.toUpperCase()).toString().toUpperCase() + ".VL");
								} else {
									matrixalert = ChatColor.RED + "Matrix was unable to find any Violations!";
								}
								all.sendMessage(Main.getPrefix() + matrixalert);
							}
						}
						if(Main.getInstance().getConfig().getBoolean("General.Include-Worldname")) {
							all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.Team.Worldname").replace("%worldname%", Bukkit.getPlayer(string2).getLocation().getWorld().getName()));
						}
						all.sendMessage("");
					}
					if(all.hasPermission(Main.getPermissionNotice("Permissions.Report.Claim")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
						TextComponent tc = new TextComponent();
						
						tc.setText(Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.Team.Teleport-Button"));
						tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/report claim " + SystemManager.getUUIDByName(string2)));
						
						all.spigot().sendMessage(tc);
						all.sendMessage("");
					}
				}
			}
		} else if(string.toUpperCase().equalsIgnoreCase("BAN")) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission(Main.getPermissionNotice("Permissions.Ban.Notify")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
					if(Main.getInstance().actionbar) {
						ActionBarAPI.sendActionBar(all, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Notify.Team.Banned").replace("%player%", p).replace("%target%", string2) + ChatColor.DARK_GRAY + " � " + ChatColor.YELLOW + reason);
					} else {
						all.sendMessage("");
						all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Notify.Team.Banned").replace("%player%", p).replace("%target%", string2));
						all.sendMessage(Main.getPrefix() + ChatColor.YELLOW + reason);
						all.sendMessage("");
					}
				}
			}
		} else if(string.toUpperCase().equalsIgnoreCase("MUTE")) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission(Main.getPermissionNotice("Permissions.Mute.Notify")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
					if(Main.getInstance().actionbar) {
						ActionBarAPI.sendActionBar(all, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Notify.Team.Muted").replace("%player%", p).replace("%target%", string2) + ChatColor.DARK_GRAY + " � " + ChatColor.YELLOW + reason);
					} else {
						all.sendMessage("");
						all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Notify.Team.Muted").replace("%player%", p).replace("%target%", string2));
						all.sendMessage(Main.getPrefix() + ChatColor.YELLOW + reason);
						all.sendMessage("");
					}
				}
			}
		} else if(string.toUpperCase().equalsIgnoreCase("UNBAN")) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission(Main.getPermissionNotice("Permissions.UnBan.Notify")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
					if(Main.getInstance().actionbar) {
						ActionBarAPI.sendActionBar(all, Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Notify.Team.Unban").replace("%player%", p).replace("%target%", string2));
					} else {
						all.sendMessage("");
						all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Notify.Team.Unban").replace("%player%", p).replace("%target%", string2));
						all.sendMessage("");
					}
				}
			}
		} else if(string.toUpperCase().equalsIgnoreCase("WARN")) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission(Main.getPermissionNotice("Permissions.Warn.Notify")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
					if(Main.getInstance().actionbar) {
						ActionBarAPI.sendActionBar(all, Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Notify").replace("%player%", p).replace("%target%", string2) + ChatColor.DARK_GRAY + " � " + ChatColor.YELLOW + reason);
					} else {
						all.sendMessage("");
						all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Warn-System.Warn.Notify").replace("%player%", p).replace("%target%", string2));
						all.sendMessage(Main.getPrefix() + ChatColor.YELLOW + reason);
						all.sendMessage("");
					}
				}
			}
		}
	}
	
	public static void createReportReason(String name, ItemStack item, String senderName) {
		if(MySQL.isConnected()) {
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			if(!existsReportReason(name)) {
				try {
					PreparedStatement st = MySQL.getCon().prepareStatement("INSERT INTO ReportSystem_reasonsdb(TYPE,NAME,REPORT_ITEM) VALUES ('REPORT','"+ name +"','"+ item.getType().toString() +"')");
					st.executeUpdate();

					new BukkitRunnable(){
						@Override
						public void run() {
							Bukkit.getServer().getPluginManager().callEvent(new ReportReasonCreateEvent(name, item));
						}
					}.runTask(Main.getInstance());

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			});
		}
	}
	
	public static boolean existsReportReason(String name) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT NAME FROM ReportSystem_reasonsdb WHERE NAME = '"+ name +"' AND TYPE = 'REPORT'");
			try {
				if(rs.next()) {
					return rs.getString("NAME") != null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static void deleteReportReason(String name, String senderName) {
		if(MySQL.isConnected()) {
			if(existsReportReason(name)) {
				Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
					try {
						PreparedStatement st = MySQL.getCon().prepareStatement("DELETE FROM ReportSystem_reasonsdb WHERE NAME = '"+ name +"' AND TYPE = 'REPORT'");
						st.executeUpdate();
						new BukkitRunnable() {
							@Override
							public void run() {
								Bukkit.getServer().getPluginManager().callEvent(new ReportReasonDeleteEvent(name));
							}
						}.runTask(Main.getInstance());

					} catch (SQLException e) {
						e.printStackTrace();
					}
				});

			}
		}
	}

	public static void claimReport(Player p, String targetuuid) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
		try {
			Player target = Bukkit.getPlayer(SystemManager.getUsernameByUUID(targetuuid));
			
			ArrayList<String> reporter_uuids = new ArrayList<>();
			
			if(isReportOpen(targetuuid)) {
				if(target != null) {
					PreparedStatement st = MySQL.getCon().prepareStatement("UPDATE ReportSystem_reportsdb SET STATUS = '1', TEAM_UUID = '"+ p.getUniqueId().toString() +"' WHERE REPORTED_UUID = '"+ targetuuid +"'");
					st.executeUpdate();

					new BukkitRunnable() {

						@Override
						public void run() {
							if(Main.getInstance().getConfig().getBoolean("General.Force-SpectatorMode")) {
								
								p.setGameMode(GameMode.SPECTATOR);	
								
							}
							Bukkit.getPluginManager().callEvent(new ReportClaimEvent(p, targetuuid));
							p.teleport(target.getLocation());
						}
						
					}.runTask(Main.getInstance());
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.Team.Claimed").replace("%target%", target.getName()));
					
					ResultSet rs1 = MySQL.getResult("SELECT * FROM ReportSystem_reportsdb WHERE REPORTED_UUID = '"+ targetuuid +"'");
					if(rs1.next()) {
						while(rs1.next()) {
							if(!reporter_uuids.contains(rs1.getString("REPORTER_UUID"))) {
								reporter_uuids.add(rs1.getString("REPORTER_UUID"));
							}
						}
						for(int i = 0; i < reporter_uuids.size(); i++) {
							if(SystemManager.existsPlayerData(reporter_uuids.get(i))) {
								Player reporter = Bukkit.getPlayer(SystemManager.getUsernameByUUID(reporter_uuids.get(i)));
								
								if(reporter != null) {
									reporter.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.User.Team-Claimed-Report"));
								} else {
									ActionManager.createAction("CLAIMED_REPORT", reporter_uuids.get(i), target.getName());
								}
							}
						}
					}
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Target-offline"));
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Notify.Team.Already-Claimed"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			
		}
		});

	}

	public static boolean isReportOpen(String targetuuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reportsdb WHERE REPORTED_UUID = '"+ targetuuid +"'");
			try {
				while(rs.next()) {
					if(rs.getInt("STATUS") == 0) {
						return true;
					} else if(rs.getInt("STATUS") == 1) {
						return true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
