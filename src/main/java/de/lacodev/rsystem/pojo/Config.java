package de.lacodev.rsystem.pojo;

import de.lacodev.rsystem.StaffCore;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Getter
public abstract class Config {

    public Config(File file) {
        this.file = file;
    }

    private final File file;
    private YamlConfiguration yamlConfiguration;

    public void init(){
        if (!file.getParentFile().exists()){
            try {
                Files.createDirectories(file.getParentFile().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!file.exists()){
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB" + ChatColor.RED + " ERROR while loading " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + file.getName() + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
            e.printStackTrace();
        }
        load();
    }

    public abstract void load();

    public void save(){
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB" + ChatColor.GRAY + " Saving... " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + file.getName() + ChatColor.DARK_GRAY + ")");
        Bukkit.getConsoleSender().sendMessage("");
        try {
            yamlConfiguration.save(file);
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB" + ChatColor.GRAY + " Saved " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + file.getName() + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB" + ChatColor.RED + " ERROR while saving " + ChatColor.DARK_GRAY + "(" + ChatColor.YELLOW + file.getName() + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }
    }

    public String getString(String path){
        return yamlConfiguration.getString(path);
    }

    public boolean getBoolean(String path){
        return yamlConfiguration.getBoolean(path);
    }

    public Integer getInt(String path){
        return yamlConfiguration.getInt(path);
    }

    public List<String> getStringList(String path){
        return yamlConfiguration.getStringList(path);
    }


}
