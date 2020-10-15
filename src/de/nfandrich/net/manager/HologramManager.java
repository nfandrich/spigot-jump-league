package de.nfandrich.net.manager;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class HologramManager {

    public boolean createHologram(Player p, String[] args) {
        ArmorStand hologram = (ArmorStand) p.getWorld().spawnEntity(p.getLocation().add(0, -0.5, 0), EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setCustomNameVisible(true);
        hologram.setCustomName(args[1]);
        hologram.setGravity(false);
        return true;
    }

}
