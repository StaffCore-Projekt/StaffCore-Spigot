package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.objects.MuteReasons;
import de.lacodev.rsystem.objects.Reasons;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PageManager {
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;

    public PageManager(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    private Map<Player, Integer> page = new HashMap<>();

    //items.subList((page * spaces) - spaces, page*spaces)
    public List<Reasons> getPageItems(List<Reasons> items, int page, int spaces) {

        int upperBound = page * spaces;

        int lowerBound = upperBound - spaces;

        List<Reasons> reasons = new ArrayList<>();

        for (int i = lowerBound; i < upperBound; i++) {
            try {
                reasons.add(new Reasons(items.get(i).getItem(), items.get(i).getName()));
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        return reasons;

    }


    public boolean isPageValid(List<?> items, int page, int spaces) {

        if (page <= 0) {

            return false;

        }

        int upperBound = page * spaces;

        int lowerBound = upperBound - spaces;

        return items.size() > lowerBound;

    }

    public List<ItemStack> getPageProtect(List<ItemStack> items, int page, int spaces) {
        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        List<ItemStack> reasons = new ArrayList<>();
        for (int i = lowerBound; i < upperBound; i++) {
            try {
                reasons.add(items.get(i));
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }

        return reasons;

    }


    public boolean isProtectValid(List<?> items, int page, int spaces) {

        if (page <= 0) {

            return false;

        }

        int upperBound = page * spaces;

        int lowerBound = upperBound - spaces;

        return items.size() > lowerBound;

    }

    public List<BanReasons> getPageItems2(List<BanReasons> items, int page, int spaces) {

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        List<BanReasons> reasons = new ArrayList<>();
        for (int i = lowerBound; i < upperBound; i++) {
            try {
                reasons.add(new BanReasons(items.get(i).getName(), items.get(i).getID(),
                        staffCore.getStaffCoreLoader().getBanManager().getRawBanLength(items.get(i).getName())));
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
        return reasons;
    }

    public List<MuteReasons> getPageItems3(List<MuteReasons> items, int page, int spaces) {

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        List<MuteReasons> reasons = new ArrayList<>();
        for (int i = lowerBound; i < upperBound; i++) {
            try {
                reasons.add(new MuteReasons(items.get(i).getName(), items.get(i).getID(),
                        items.get(i).getLength()));
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
        return reasons;
    }


}
