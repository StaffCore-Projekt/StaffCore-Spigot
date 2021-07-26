package de.lacodev.rsystem.config.extension;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.pojo.Config;

import java.io.File;

public class SpartanAntiCheatProvider extends Config {
    private final StaffCore staffCore;

    public SpartanAntiCheatProvider(StaffCore staffCore) {
        super(new File(staffCore.getDataFolder().getParent() + File.separator + "StaffCore" + File.separator + "Extension" + File.separator + "SpartanAntiCheat.yml"));
        this.staffCore = staffCore;
    }

    @Override
    public void load() {

    }

    //        getConfig().addDefault("SpartanAntiCheat.Autoreport.Enable", false);
    //        getConfig().addDefault("SpartanAntiCheat.Autoreport.Name", "SpartanAC");
    //        getConfig().addDefault("SpartanAntiCheat.Autoreport.Log.Reset-Violations-On-Join", true);
    //        if (setupSpartanAC()) {
    //            for (HackType h : HackType.values()) {
    //                getConfig().addDefault("SpartanAntiCheat.Autoreport." + h.toString() + ".Displayname",
    //                        "" + h.toString().substring(0, 1).toUpperCase() + h.toString().substring(1)
    //                                .toLowerCase());
    //                getConfig()
    //                        .addDefault("SpartanAntiCheat.Autoreport." + h + ".Violationslevel", 20);
    //            }
    //        }
}
