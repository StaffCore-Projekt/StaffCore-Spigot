package de.lacodev.rsystem.config;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.pojo.Config;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class MySQLProvider extends Config {

    private final StaffCore staffCore;

    public MySQLProvider(StaffCore staffCore) {
        super(new File(staffCore.getDataFolder().getParent() + File.separator + "StaffCore" + File.separator + "MySQL.yml"));
        this.staffCore = staffCore;
    }

    @Override
    public void load() {
        ConfigurationSection section = getYamlConfiguration().getConfigurationSection("MySQL");
        if (section == null) {
            section = getYamlConfiguration().createSection("MySQL");
            section.set("Host", "localhost");
            section.set("Port", 3306);
            section.set("Username", "root");
            section.set("Password", "");
            section.set("Database", "StaffCore");
            section.set("Debug", true);
        }
    }
}
