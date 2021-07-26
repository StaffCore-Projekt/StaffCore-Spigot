package de.lacodev.rsystem.config.extension;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.pojo.Config;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MatrixConfigProvider extends Config {

    private final StaffCore staffCore;

    public MatrixConfigProvider(StaffCore staffCore) {
        super(new File(staffCore.getDataFolder().getParent() + File.separator + "StaffCore" + File.separator + "Extension" + File.separator + "MatrixAntiCheat.yml"));
        this.staffCore = staffCore;
    }


    @Override
    public void load() {
        ConfigurationSection matrixAntiCheatSection = getYamlConfiguration().getConfigurationSection("MatrixAntiCheat");
        if (matrixAntiCheatSection == null) {
            matrixAntiCheatSection = getYamlConfiguration().createSection("MatrixAntiCheat");
            matrixAntiCheatSection.set("Autoreport.Enable", false);
            matrixAntiCheatSection.set("Autoreport.Name", "MatrixAC");
            matrixAntiCheatSection.set("Autoreport.Log.Reset-Violations-On-Join", true);
            if (staffCore.getStaffCoreLoader().getMatrixAntiCheatUtils().setupMatrix()){
                for (me.rerere.matrix.api.HackType type : me.rerere.matrix.api.HackType.values()){
                    matrixAntiCheatSection.set("AutoReport." + type.toString() + ".Displayname",
                            type.toString().substring(0,1).toUpperCase() + type.toString().substring(1).toLowerCase());
                    matrixAntiCheatSection.set("AutoReport." + type + ".Violationslevel", 20);
                }
            }
            save();
        }
    }

}
