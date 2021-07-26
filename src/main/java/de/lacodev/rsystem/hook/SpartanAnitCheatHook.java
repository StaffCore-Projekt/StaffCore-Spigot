package de.lacodev.rsystem.hook;

import de.lacodev.rsystem.StaffCore;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class SpartanAnitCheatHook {
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;
    private boolean enabled;

    public SpartanAnitCheatHook(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.enabled = false;
    }

    public void setupSpartanAC() {
        enabled = staffCore.getServer().getPluginManager().getPlugin("SpartanAPI") != null
                || staffCore.getServer().getPluginManager().getPlugin("Spartan") != null;
    }
}
