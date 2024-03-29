package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.objects.MuteReasons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PanelManager {

    public static Inventory settings;
    private Inventory mainmenu;
    private Inventory playermanagement;
    private Inventory onlineplayermenu;
    private Inventory protect;
    private Inventory playermenu;
    
    private final StaffCore staffCore;

    public PanelManager(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    //TRANSLATE START
    public void openMuteReasonUtils(Player p, int reasonId) {
        String title = staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Reason-Title")
                .replace("%reason%", "" + reasonId);
        if (title.length() > 32) {
            System.out.println("Sorry, but you have to many Reasons Created!");
        }

        Inventory muteReasonUtils = p.getServer().createInventory(null, 9, title);
        for (int i = 0; i < 9; i++) {
            muteReasonUtils.setItem(i, Data.buildPlace());
        }

        ItemStack menu = Data.getHead("arrow");
        assert menu != null;
        ItemMeta meta = menu.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "◄ Go back");
        menu.setItemMeta(meta);

        muteReasonUtils.setItem(0, menu);

        if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("BanManager.editreason")) || p
                .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
            ItemStack edit = Data.getHead("graye");
            assert edit != null;
            ItemMeta editM = edit.getItemMeta();

            assert editM != null;
            editM.setDisplayName(ChatColor.GRAY + "Edit");

            edit.setItemMeta(editM);

            muteReasonUtils.setItem(4, edit); //EDIT

        }

        if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("BanManager.removereason")) || p
                .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
            ItemStack delete = Data.getHead("redd");
            assert delete != null;
            ItemMeta deleteM = delete.getItemMeta();
            assert deleteM != null;
            deleteM.setDisplayName(ChatColor.RED + "DELETE");
            delete.setItemMeta(deleteM);

            muteReasonUtils.setItem(6, delete); //DELETE
        }

        p.openInventory(muteReasonUtils);
    }

    public void openMuteReasonEdit(Player p, int reasonId) {
        String title = staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Edit-Title")
                .replace("%reason%", "" + reasonId);
        if (title.length() > 32) {
            System.out.println("Sorry, but you have to many Reasons Created!");
        }

        Inventory muteReasonEdit = p.getServer().createInventory(null, 9, title);
        for (int i = 0; i < 9; i++) {
            muteReasonEdit.setItem(i, Data.buildPlace());
        }

        ItemStack menu = Data.getHead("arrow");
        assert menu != null;
        ItemMeta meta = menu.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "◄ Go back");
        menu.setItemMeta(meta);

        muteReasonEdit.setItem(0, menu);

        ItemStack editN = Data.getHead("grayn");
        assert editN != null;
        ItemMeta editNM = editN.getItemMeta();
        assert editNM != null;
        editNM.setDisplayName(ChatColor.GRAY + "Rename Reason");
        editN.setItemMeta(editNM);

        muteReasonEdit.setItem(4, editN);

        ItemStack editD = Data.getHead("grayd");
        assert editD != null;
        ItemMeta editDM = editD.getItemMeta();
        assert editDM != null;
        editDM.setDisplayName(ChatColor.GRAY + "Edit Duration");
        editD.setItemMeta(editDM);

        muteReasonEdit.setItem(6, editD);

        p.openInventory(muteReasonEdit);
    }

    public void openBanReasonEdit(Player player, int reasonId) {
        String title = staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Ban-Edit-Title")
                .replace("%reason%", "" + reasonId);
        if (title.length() > 32) {
            System.out.println("Sorry, but you have to many Reasons Created!");
        }

        Inventory banReasonEdit = player.getServer().createInventory(null, 9, title);

        for (int i = 0; i < 9; i++) {
            banReasonEdit.setItem(i, Data.buildPlace());
        }

        ItemStack menu = Data.getHead("arrow");
        assert menu != null;
        ItemMeta meta = menu.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "◄ Go back");
        menu.setItemMeta(meta);

        banReasonEdit.setItem(0, menu);

        ItemStack editN = Data.getHead("grayn");
        assert editN != null;
        ItemMeta editNM = editN.getItemMeta();
        assert editNM != null;
        editNM.setDisplayName(ChatColor.GRAY + "Rename Reason");
        editN.setItemMeta(editNM);
        banReasonEdit.setItem(4, editN);

        ItemStack editD = Data.getHead("grayd");
        assert editD != null;
        ItemMeta editDM = editD.getItemMeta();
        assert editDM != null;
        editDM.setDisplayName(ChatColor.GRAY + "Edit Duration");
        editD.setItemMeta(editDM);
        banReasonEdit.setItem(6, editD);

        player.openInventory(banReasonEdit);
    }

    public void openBanReasonUtils(Player player, int reasonid) {
        String title = staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Ban-Reason-Title")
                .replace("%reason%", "" + reasonid);
        if (title.length() > 32) {
            System.out.println("Sorry, but you have to many Reasons Created!");
        }

        Inventory banReasonGui = player.getServer().createInventory(null, 9, title);
        for (int i = 0; i < 9; i++) {
            banReasonGui.setItem(i, Data.buildPlace());
        }

        ItemStack menu = Data.getHead("arrow");
        assert menu != null;
        ItemMeta meta = menu.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "◄ Go back");
        menu.setItemMeta(meta);

        banReasonGui.setItem(0, menu);

        if (player.hasPermission(staffCore.getStaffCoreLoader().getPermission("BanManager.editreason"))
                || player.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
            ItemStack edit = Data.getHead("graye");
            assert edit != null;
            ItemMeta editM = edit.getItemMeta();

            assert editM != null;
            editM.setDisplayName(ChatColor.GRAY + "Edit");

            edit.setItemMeta(editM);

            banReasonGui.setItem(4, edit); //EDIT
        }

        if (player.hasPermission(staffCore.getStaffCoreLoader().getPermission("BanManager.removereason"))
                || player.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
            ItemStack delete = Data.getHead("redd");
            assert delete != null;
            ItemMeta deleteM = delete.getItemMeta();
            assert deleteM != null;
            deleteM.setDisplayName(ChatColor.RED + "DELETE");
            delete.setItemMeta(deleteM);

            banReasonGui.setItem(6, delete); //DELETE
        }

        player.openInventory(banReasonGui);
    }
//TRANSLATE STOP

    public void openBanManagerManu(Player player) {
        Inventory banManagerGui = player.getServer()
                .createInventory(null, 6 * 9, staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Main-Gui-Title"));
        for (int i = 0; i < 9; i++) {
            banManagerGui.setItem(i, Data.buildPlace());
        }

        for (int i = 45; i < 54; i++) {
            banManagerGui.setItem(i, Data.buildPlace());
        }

        if (player.hasPermission(staffCore.getStaffCoreLoader().getPermission("BanManager.addreason")) || player
                .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
            ItemStack b = Data.getHead("grayb");
            assert b != null;
            ItemMeta bmeta = b.getItemMeta();
            assert bmeta != null;
            bmeta.setDisplayName(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason"));
            b.setItemMeta(bmeta);

            banManagerGui.setItem(48, b);

            ItemStack m = Data.getHead("graym");
            assert m != null;
            ItemMeta mmeta = m.getItemMeta();
            assert mmeta != null;
            mmeta.setDisplayName(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason-Mute"));
            m.setItemMeta(mmeta);

            banManagerGui.setItem(50, m);
        }

        ArrayList<BanReasons> breasons = staffCore.getStaffCoreLoader().getBanManager().getBanReasons();

        for (int i = 0; i < breasons.size(); i++) {
            BanReasons breason = breasons.get(i);
            List<String> blore = new ArrayList<>();
            if (staffCore.getStaffCoreLoader().getBanManager().getBanLength(breason.getLength())
                    .equalsIgnoreCase(staffCore.getStaffCoreLoader().getMessage("Messages.Layouts.Ban.Length-Values.Permanently"))) {
                blore.add(ChatColor.GRAY + "Length: " + ChatColor.RED + ChatColor.BOLD + ChatColor
                        .stripColor(staffCore.getStaffCoreLoader().getBanManager().getBanLength(breason.getLength())));
            } else {
                blore.add(ChatColor.GRAY + "Length: " + ChatColor.YELLOW + staffCore.getStaffCoreLoader().getBanManager()
                        .getBanLength(breason.getLength()));
            }

            ItemStack redbb = Data.getHead("redb");
            assert redbb != null;
            ItemMeta redbbItemMeta = redbb.getItemMeta();
            assert redbbItemMeta != null;
            redbbItemMeta.setDisplayName(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Ban-Reason-Button-Title")
                    .replace("%reason%", breason.getName()));
            //redbbItemMeta.setDisplayName("§7Ban Reason: " + ChatColor.YELLOW + breason.getName());
            redbbItemMeta.setLore(blore);
            redbb.setItemMeta(redbbItemMeta);
            banManagerGui.setItem(i + 9, redbb);
        }

        ArrayList<MuteReasons> mReasons = staffCore.getStaffCoreLoader().getBanManager().getMuteReasons();

        for (int i = 0; i < mReasons.size(); i++) {
            MuteReasons mReason = mReasons.get(i);
            List<String> mlore = new ArrayList<>();
            mlore.add(ChatColor.GRAY + "Length: " + ChatColor.YELLOW + staffCore.getStaffCoreLoader().getBanManager()
                    .getBanLength(mReason.getLength()));

            ItemStack redm = Data.getHead("redm");
            assert redm != null;
            ItemMeta mRedM = redm.getItemMeta();
            assert mRedM != null;
            mRedM.setDisplayName(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Reason-Button-Title")
                    .replace("%reason%", mReason.getName()));
            //mRedM.setDisplayName(ChatColor.GRAY + "Mute Reason: " + ChatColor.YELLOW + mReason.getName());
            mRedM.setLore(mlore);
            redm.setItemMeta(mRedM);
            banManagerGui.setItem((i + 9 + breasons.size()), redm);
        }

        player.openInventory(banManagerGui);
    }

    public void openMainMenu(Player player) {
        mainmenu = player.getServer().createInventory(null, 27,
                ChatColor.RED + "Staffcore" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "MainMenu");

        for (int i = 0; i < mainmenu.getSize(); i++) {
            mainmenu.setItem(i, Data.buildPlace());
        }

        mainmenu
                .setItem(10, Data.buildPlayerHead(ChatColor.GRAY + "Player-Management", player.getName()));
        mainmenu.setItem(13,
                Data.buildItem(XMaterial.COMMAND_BLOCK.parseMaterial(), 1, 0, ChatColor.GRAY + "Settings"));

        mainmenu.setItem(26,
                Data.buildItemStackLore(Material.PAPER, 1, 0, ChatColor.RED + "Report a Bug",
                        ChatColor.GRAY + "If you find any error which",
                        ChatColor.GRAY + "might be caused by " + ChatColor.RED + "StaffCore",
                        ChatColor.GRAY + "Report it with clicking on this item"));

        player.openInventory(mainmenu);
    }

    public void openPlayerManagement(Player player) {
        playermanagement = player.getServer()
                .createInventory(null, 27, ChatColor.RED + "Player - Management");

        for (int i = 0; i < playermanagement.getSize(); i++) {
            playermanagement.setItem(i, Data.buildPlace());
        }

        playermanagement.setItem(13,
                Data.buildPlayerHead(ChatColor.GRAY + "Manage OnlinePlayers", player.getName(),
                        ChatColor.GRAY + "Online " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + Bukkit
                                .getOnlinePlayers().size() + " Player(s)"));
        playermanagement.setItem(16,
                Data.buildItem(XMaterial.DIAMOND_CHESTPLATE, ChatColor.GRAY + "Protect a player"));

        ItemStack menu = Data.getHead("arrow");
        assert menu != null;
        ItemMeta meta = menu.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "◄ Go back");
        menu.setItemMeta(meta);

        playermanagement.setItem(18, menu);

        player.openInventory(playermanagement);
    }

    public void openOnlinePlayerMenu(Player player, int page) {
        onlineplayermenu = player.getServer()
                .createInventory(null, 54, ChatColor.RED + "Manage OnlinePlayers");

        for (int i = 0; i < 9; i++) {
            onlineplayermenu.setItem(i, Data.buildPlace());
        }
        for (int i = 45; i < 54; i++) {
            onlineplayermenu.setItem(i, Data.buildPlace());
        }

        ArrayList<ItemStack> items = new ArrayList<>();
        for (Player all : Bukkit.getOnlinePlayers()) {
            items.add(Data.buildPlayerHead(ChatColor.GRAY + all.getName(), all.getName()));
        }

        staffCore.getStaffCoreLoader().getPageManager().getPage().remove(player.getPlayer());
        staffCore.getStaffCoreLoader().getPageManager().getPage().put(player.getPlayer(), page);

        ItemStack forward = Data.getHead("oak_arrow_right");
        ItemMeta fmeta = forward.getItemMeta();
        fmeta.setDisplayName(ChatColor.GREEN + "►");
        forward.setItemMeta(fmeta);

        ItemStack back = Data.getHead("oak_arrow_left");
        ItemMeta bmeta = back.getItemMeta();
        bmeta.setDisplayName(ChatColor.GREEN + "◄");
        back.setItemMeta(bmeta);

        if (staffCore.getStaffCoreLoader().getPageManager().isProtectValid(items, page - 1, 36)) {
            onlineplayermenu.setItem(46, back);
        }

        if (staffCore.getStaffCoreLoader().getPageManager().isProtectValid(items, page + 1, 36)) {
            onlineplayermenu.setItem(52, forward);
        }

        for (ItemStack item : staffCore.getStaffCoreLoader().getPageManager().getPageProtect(items, page, 36)) {
            onlineplayermenu.setItem(onlineplayermenu.firstEmpty(), item);
        }

        ItemStack menu = Data.getHead("arrow");
        ItemMeta meta = menu.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "◄ Go back");
        menu.setItemMeta(meta);

        onlineplayermenu.setItem(45, menu);

        player.openInventory(onlineplayermenu);
    }

    public void openProtectionInventory(Player p, int page) {
        String title =
                ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Protections";

        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        protect = p.getServer().createInventory(null, 54, title);

        new BukkitRunnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {

                    ArrayList<ItemStack> rows = new ArrayList<>();
                    ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT * FROM ReportSystem_playerdb");
                    try {
                        while (rs.next()) {

                            ItemStack player = XMaterial.PLAYER_HEAD.parseItem();
                            SkullMeta meta = (SkullMeta) player.getItemMeta();
                            meta.setOwner(rs.getString("PLAYERNAME"));
                            if (staffCore.getStaffCoreLoader().getSystemManager().isProtected(rs.getString("UUID"))) {
                                meta.setDisplayName(
                                        ChatColor.GRAY + rs.getString("PLAYERNAME") + ChatColor.DARK_GRAY + " | "
                                                + ChatColor.GREEN + "Protected");
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                            } else {
                                meta.setDisplayName(
                                        ChatColor.GRAY + rs.getString("PLAYERNAME") + ChatColor.DARK_GRAY + " | "
                                                + ChatColor.RED + "Unprotected");
                            }
                            player.setItemMeta(meta);

                            rows.add(player);

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    for (int i2 = 0; i2 < 9; i2++) {
                        protect.setItem(i2, Data.buildPlace());

                    }
                    staffCore.getStaffCoreLoader().getPageManager().getPage().remove(p.getPlayer());
                    staffCore.getStaffCoreLoader().getPageManager().getPage().put(p.getPlayer(), page);

                    for (int i2 = 45; i2 < 54; i2++) {
                        protect.setItem(i2, Data.buildPlace());
                    }
                    ItemStack forward = Data.getHead("oak_arrow_right");
                    ItemMeta fmeta = forward.getItemMeta();
                    fmeta.setDisplayName(ChatColor.GREEN + "►");
                    forward.setItemMeta(fmeta);

                    ItemStack back = Data.getHead("oak_arrow_left");
                    ItemMeta bmeta = back.getItemMeta();
                    bmeta.setDisplayName(ChatColor.GREEN + "◄");
                    back.setItemMeta(bmeta);

                    if (staffCore.getStaffCoreLoader().getPageManager().isProtectValid(rows, page - 1, 36)) {
                        protect.setItem(46, back);
                    }

                    if (staffCore.getStaffCoreLoader().getPageManager().isProtectValid(rows, page + 1, 36)) {
                        protect.setItem(52, forward);
                    }

                    for (ItemStack item : staffCore.getStaffCoreLoader().getPageManager().getPageProtect(rows, page, 36)) {
                        protect.setItem(protect.firstEmpty(), item);
                    }
                } else {
                    protect
                            .setItem(13, Data.buildItem(Material.BARRIER, 1, 0, ChatColor.RED + "No Connection"));
                }

                ItemStack menu = Data.getHead("arrow");
                ItemMeta meta = menu.getItemMeta();
                meta.setDisplayName(ChatColor.RED + "◄ Go back");
                menu.setItemMeta(meta);

                protect.setItem(45, menu);
            }

        }.runTaskAsynchronously(staffCore);

        p.openInventory(protect);
    }

    public void openPlayerMenu(Player p, String target) {
        playermenu = p.getServer().createInventory(null, 9,
                target + ChatColor.DARK_GRAY + "- " + ChatColor.RED + "StaffCore");

        for (int i = 0; i < 9; i++) {
            playermenu.setItem(i, Data.buildPlace());
        }

        ItemStack menu = Data.getHead("arrow");
        ItemMeta meta = menu.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "◄ Go back");
        menu.setItemMeta(meta);

        playermenu.setItem(0, menu);

        playermenu.setItem(3,
                Data.buildItemStack(XMaterial.EXPERIENCE_BOTTLE, ChatColor.RED + "Check Player",
                        ChatColor.GRAY + "Get all important information", ChatColor.GRAY + "about the player"));
        playermenu.setItem(5, Data.buildItem(XMaterial.BOOK, ChatColor.RED + "Mute Player"));
        playermenu.setItem(7, Data.buildItem(XMaterial.RED_DYE, ChatColor.RED + "Ban Player"));

        p.openInventory(playermenu);
    }

    public void openSettingsMenu(Player p) {
        settings = p.getServer().createInventory(null, 54,
                ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Settings");

        for (int i = 0; i < 54; i++) {
            settings.setItem(i, Data.buildPlace());
        }

        settings.setItem(12, Data.buildHead("globe", ChatColor.GREEN + "Download Latest Release",
                ChatColor.GRAY + "This will download the latest release",
                ChatColor.GRAY + "which has been " + ChatColor.GREEN + "successfully tested "
                        + ChatColor.GRAY + "in it's current state"));
        settings.setItem(14, Data.buildHead("warn", ChatColor.GOLD + "Download Latest Experimental",
                ChatColor.GRAY + "This will download the latest dev-build",
                ChatColor.GRAY + "which is " + ChatColor.RED + "not finished " + ChatColor.GRAY
                        + "in it's current state"));

        settings.setItem(29, Data.buildHead("oak_arrow_left", ChatColor.GREEN + "◄"));

        settings.setItem(31, getLanguageHead());

        settings.setItem(33, Data.buildHead("oak_arrow_right", ChatColor.GREEN + "►"));

        settings.setItem(45, Data.buildHead("arrow", ChatColor.RED + "◄ Go back"));
        settings.setItem(49,
                Data.buildHead("reload", ChatColor.GRAY + "Reload " + ChatColor.RED + "StaffCore",
                        ChatColor.GRAY + "This will completly restart StaffCore",
                        ChatColor.GRAY + "including MySQL reconnect and Config reload"));
        settings.setItem(53,
                Data.buildHead("webui", ChatColor.GRAY + "Setup " + ChatColor.RED + "StaffCore-UI",
                        ChatColor.GRAY + "This will create all MySQL-Tables which",
                        ChatColor.GRAY + "are required for StaffCore-UI to work!"));

        p.openInventory(settings);
    }

    public ItemStack getLanguageHead() {
        return Data.buildHead(staffCore.getStaffCoreLoader().getConfigProvider().getString("General.Language"),
                ChatColor.GREEN + staffCore.getStaffCoreLoader().getConfigProvider().getString("General.Language"));
    }

    public Inventory getMainMenu() {
        return mainmenu;
    }

    public Inventory getPlayerManagement() {
        return playermanagement;
    }

    public Inventory getOnlinePlayerMenu() {
        return onlineplayermenu;
    }

    public Inventory getProtectMenu() {
        return protect;
    }

    public Inventory getPlayerMenu() {
        return playermenu;
    }


}
