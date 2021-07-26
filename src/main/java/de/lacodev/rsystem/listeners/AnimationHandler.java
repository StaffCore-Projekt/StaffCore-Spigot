package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AnimationHandler implements Listener {

    private final StaffCore staffCore;

    public AnimationHandler(StaffCore staffCore) {
        this.staffCore = staffCore;
        Bukkit.getPluginManager().registerEvents(this, staffCore);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity p = e.getEntity();
        Entity d = e.getDamager();

        if (p instanceof Player) {
            if (d instanceof Guardian) {
                Guardian g = (Guardian) d;

                if (g.getCustomName() != null) {
                    if (g.getCustomName().equalsIgnoreCase("G1") || g.getCustomName().equalsIgnoreCase("G2")
                            || g.getCustomName().equalsIgnoreCase("G3")) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onGuardianDmg(EntityDeathEvent e) {
        Entity en = e.getEntity();

        if (en instanceof Guardian) {
            Guardian g = (Guardian) en;

            if (g.getCustomName() != null) {
                if (g.getCustomName().equalsIgnoreCase("G1") || g.getCustomName().equalsIgnoreCase("G2")
                        || g.getCustomName().equalsIgnoreCase("G3")) {
                    e.getDrops().clear();
                    e.setDroppedExp(0);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity en = e.getEntity();

        if (en instanceof Zombie) {
            if (en.getCustomName() != null) {
                if (e.getEntity().getCustomName()
                        .startsWith(ChatColor.RED + "Ban " + ChatColor.DARK_GRAY + "� " + ChatColor.GRAY)) {
                    e.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (staffCore.getStaffCoreLoader().getBanManager().freezed.contains(p)) {
            p.teleport(e.getFrom());
        }
    }

    @EventHandler
    public void onZombie(EntityTargetLivingEntityEvent e) {
        if (e.getEntity().getCustomName() != null) {
            if (e.getEntity().getCustomName()
                    .startsWith(ChatColor.RED + "Ban " + ChatColor.DARK_GRAY + "� " + ChatColor.GRAY)) {
                e.setCancelled(true);
            }
        }
    }

}
