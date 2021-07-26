package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import lombok.AccessLevel;
import lombok.Getter;
import me.rerere.matrix.api.HackType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MatrixAntiCheatUtils {
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;
    private final List<HackType> hackTypeList;
    private final List<String> hackTypes;
    private boolean enabled;

    public MatrixAntiCheatUtils(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.hackTypeList = new ArrayList<>();
        this.hackTypes = new ArrayList<>();
        this.enabled = false;
    }

    public boolean setupMatrix(){
        if (Bukkit.getPluginManager().getPlugin("Matrix") == null) {
            return false;
        } else {
            for (HackType value : HackType.values()) {
                hackTypeList.add(value);
                hackTypes.add(value.toString().toUpperCase());
            }
            enabled = true;
            return true;
        }
    }

}
