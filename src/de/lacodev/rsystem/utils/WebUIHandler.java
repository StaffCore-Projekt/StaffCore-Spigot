package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WebUIHandler {

  public static void setupStaffCoreUI(Player player) {

    player.sendMessage(
        Main.getPrefix() + ChatColor.GRAY + "Please make sure you " + ChatColor.RED + ChatColor.BOLD
            + "never performed " + ChatColor.GRAY + "this actions before!");
    player.sendMessage(
        Main.getPrefix() + ChatColor.GRAY + "There may occur some lags while setting up the WebUI");
    player.sendMessage(Main.getPrefix() + ChatColor.GRAY + "Creating MySQL-Tables...");

    // StaffCoreUI Accounts Table
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_accounts(id INT(6) AUTO_INCREMENT UNIQUE, EMAIL VARCHAR(255), "
            + "PASSWORD VARCHAR(255), USER_RANK VARCHAR(255), UUID VARCHAR(255), PLAYER_NAME VARCHAR(255), "
            + "REGISTERED_SINCE VARCHAR(255), LAST_LOGIN VARCHAR(255), ADMIN_ACCOUNT BOOLEAN)");

    // StaffCoreUI Notifications Table
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_notifications(id INT(6) AUTO_INCREMENT UNIQUE, USER_ID INT(6), "
            + "NOTIFY_TEXT VARCHAR(255), NOTIFY_LINK TEXT, SEEN BOOLEAN)");

    // StaffCoreUI Ranks
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_ranks(id INT(6) AUTO_INCREMENT UNIQUE, RANK_NAME VARCHAR(255), "
            + "RANK_VALUE INT(6), IS_STANDARD BOOLEAN)");

    // StaffCoreUI Ranks Permissions
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_ranks_permissions(id INT(6) AUTO_INCREMENT UNIQUE, PERMISSION VARCHAR(255), "
            + "MIN_RANK_VALUE INT(6))");

    // StaffCoreUI Tasks Table
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_tasks(id INT(6) AUTO_INCREMENT UNIQUE, RECIPIENT INT(6), "
            + "TASK_ID VARCHAR(255) UNIQUE, ASSIGNED_BY VARCHAR(255), STATUS INT(6), last_update VARCHAR(255))");

    // StaffCoreUI Task Comments Table
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_tasks_comments(id INT(6) AUTO_INCREMENT UNIQUE, TASK_ID INT(6), "
            + "COMMENT TEXT, AUTHOR VARCHAR(255), POSTED VARCHAR(255))");

    // StaffCoreUI Appeals Table
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_appeals(id INT(6) AUTO_INCREMENT UNIQUE, BAN_ID INT(11), UUID VARCHAR(255), "
            + "TEAM_UUID VARCHAR(255), TYPE VARCHAR(255), REASON VARCHAR(255), STATUS INT(6), Message VARCHAR(5000), "
            + "last_update VARCHAR(255))");

    // StaffCoreUI Appeal Comments Table
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_appeals_comments(id INT(6) AUTO_INCREMENT UNIQUE, APPEAL_ID INT(6), "
            + "COMMENT TEXT, AUTHOR VARCHAR(255), POSTED VARCHAR(255))");

    // StaffCoreUI Staff-Activities Table
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_staff_activities(id INT(6) AUTO_INCREMENT UNIQUE, USER_ID INT(6), "
            + "ACTIVITY_TEXT VARCHAR(500), PERFORMED VARCHAR(255))");

    // StaffCoreUI Settings Table
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_settings(id INT(6) AUTO_INCREMENT UNIQUE, TYPE VARCHAR(255), "
            + "VALUE TEXT)");

    // Sync between Plugin and StaffCoreUI
    MySQL.update(
        "CREATE TABLE IF NOT EXISTS staffcoreui_sync(id INT(6) AUTO_INCREMENT UNIQUE, SYNC_TYPE VARCHAR(255), "
            + "TARGET_UUID VARCHAR(255), DESCRIPTION VARCHAR(255), EXECUTOR_UUID VARCHAR(255))");

    player.sendMessage(
        Main.getPrefix() + ChatColor.GRAY + "MySQL-Tables were " + ChatColor.GREEN + "successfully "
            + ChatColor.GRAY + "created");
    player.sendMessage(Main.getPrefix() + ChatColor.GRAY + "Inserting Standard-Values...");

    MySQL.update(
        "INSERT INTO staffcoreui_ranks(id,RANK_NAME,RANK_VALUE,IS_STANDARD) VALUES ('1','MEMBER','1','1')");
    MySQL.update(
        "INSERT INTO staffcoreui_ranks(id,RANK_NAME,RANK_VALUE,IS_STANDARD) VALUES ('2','ADMIN','999999','0')");

    player.sendMessage(Main.getPrefix() + ChatColor.GRAY + "Standard-Values were " + ChatColor.GREEN
        + "successfully " + ChatColor.GRAY + "inserted");
  }

}
