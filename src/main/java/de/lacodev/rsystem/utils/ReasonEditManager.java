package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.objects.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReasonEditManager {
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;

    public List<BanManagerPlayerInput> banManagerPlayerInputs = new ArrayList<>();
    public List<ReasonRename> reasonRename = new ArrayList<>();
    public List<ReasonEDuration> reasonEDurations = new ArrayList<>();

    public ReasonEditManager(StaffCore staffCore) {
        this.staffCore = staffCore;
    }
}
