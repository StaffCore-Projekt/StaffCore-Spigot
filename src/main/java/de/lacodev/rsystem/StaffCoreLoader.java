package de.lacodev.rsystem;

import de.lacodev.rsystem.commands.BanCommand;
import de.lacodev.rsystem.config.ConfigProvider;
import de.lacodev.rsystem.config.MySQLProvider;
import de.lacodev.rsystem.config.PermissionConfigProvider;
import de.lacodev.rsystem.config.extension.MatrixConfigProvider;
import de.lacodev.rsystem.config.extension.SpartanAntiCheatProvider;
import de.lacodev.rsystem.feature.TranslationHandler;
import de.lacodev.rsystem.hook.SpartanAnitCheatHook;
import de.lacodev.rsystem.hook.VaultHook;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.hook.ActionBarHook;
import de.lacodev.rsystem.utils.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.net.MalformedURLException;
import java.net.URL;

@Getter
public class StaffCoreLoader {

    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;


    public StaffCoreLoader(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    private TranslationHandler translationHandler;

    //Utils
    private MatrixAntiCheatUtils matrixAntiCheatUtils;
    private MySQL mySQL;

    //Config
    private ConfigProvider configProvider;
    private MatrixConfigProvider matrixConfigProvider;
    private SpartanAntiCheatProvider spartanAntiCheatProvider;
    private PermissionConfigProvider permissionConfigProvider;
    private MySQLProvider mySQLProvider;

    //Manager
    private ActionManager actionManager;
    private AntiMCLeaksHandler antiMCLeaksHandler;
    private BanManager banManager;
    private BugManager bugManager;
    private InventoryHandler inventoryHandler;
    private PageManager pageManager;
    private PanelManager panelManager;
    private ReasonEditManager reasonEditManager;
    private ReportManager reportManager;
    private SettingsManager settingsManager;
    private SystemManager systemManager;
    private TicketManager ticketManager;
    private WebUIHandler webUIHandler;

    //Hooks
    private ActionBarHook actionBarHook;
    private VaultHook vaultHook;
    private SpartanAnitCheatHook spartanAnitCheatHook;


    public void init(boolean b){
        sendUserInfos();
        initConfigs();
        initManager();
        if (!b) {
            initListener();
            initCommands();
        }
    }

    public void initHooks() {
        this.actionBarHook = new ActionBarHook(staffCore);
        this.actionBarHook.init();

        this.vaultHook = new VaultHook(staffCore);
        this.vaultHook.init();

        this.spartanAnitCheatHook = new SpartanAnitCheatHook(staffCore);
        this.spartanAnitCheatHook.setupSpartanAC();
    }

    private void initConfigs(){
        this.mySQLProvider = new MySQLProvider(staffCore);
        this.mySQLProvider.init();

        this.configProvider = new ConfigProvider(staffCore);
        this.configProvider.init();

        this.permissionConfigProvider = new PermissionConfigProvider(staffCore);
        this.permissionConfigProvider.init();

        this.spartanAntiCheatProvider = new SpartanAntiCheatProvider(staffCore);
        this.spartanAntiCheatProvider.init();

        this.matrixConfigProvider = new MatrixConfigProvider(staffCore);
        this.matrixConfigProvider.init();
    }

    private void initManager() {
        this.translationHandler = new TranslationHandler();
        this.mySQL = new MySQL(staffCore);

        this.actionManager = new ActionManager(staffCore);

        this.antiMCLeaksHandler = new AntiMCLeaksHandler(staffCore);
        //this.antiMCLeaksHandler.init();

        this.banManager = new BanManager(staffCore);

        this.bugManager = new BugManager(staffCore);

        this.inventoryHandler = new InventoryHandler(staffCore);

        this.pageManager = new PageManager(staffCore);

        this.panelManager = new PanelManager(staffCore);

        this.reasonEditManager = new ReasonEditManager(staffCore);

        this.reportManager = new ReportManager(staffCore);

        this.systemManager = new SystemManager(staffCore);

        this.ticketManager = new TicketManager(staffCore);

        this.webUIHandler = new WebUIHandler(staffCore);
    }

    private void initListener(){

    }

    private void initCommands(){
        new BanCommand(staffCore);
    }

    public void unload() {
        this.mySQL.disconnect();
    }

    public String getPrefix(){
        return ChatColor.translateAlternateColorCodes('&', this.configProvider.getString("General.System-Prefix").replace("8%double_arrow%", "\u00BB"));
    }

    public String getMessage(String message){
        return this.translationHandler.getTranslation(message);
    }

    public String getPermission(String permission){
        return this.permissionConfigProvider.getString(permission);
    }


    public void sendUserInfos(){
        checkMatrix();
        this.mySQL.connect();
        this.mySQL.createTable();
        for (Material value : Material.values()) {
            this.mySQL.updateMaterial(value);
        }
    }

    public void checkMatrix(){
        if (this.configProvider.getBoolean("General.Include-MatrixAntiCheat")) {
            this.matrixAntiCheatUtils = new MatrixAntiCheatUtils(staffCore);
            if (this.matrixAntiCheatUtils.setupMatrix()) {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.GREEN + "Successfully " + ChatColor.GRAY + "added Matrix to our system!");
                Bukkit.getConsoleSender().sendMessage("");
            }else{
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.RED + "Failed " + ChatColor.GRAY + "added Matrix to our system! Please make sure you have Matrix installed and set up properly!");
                Bukkit.getConsoleSender().sendMessage("");
            }
        }
    }



}
