package de.nfandrich.net.listener;

import de.nfandrich.net.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Main.main.log("found player " + p.getDisplayName());
        if(Main.main.alive.contains(p)) {
            Main.main.alive.remove(p);
            if(Main.main.lastdmg.containsKey(p)) {
                e.setDeathMessage(Main.main.prefix + p.getDisplayName() + " §cwurde von " + Main.main.lastdmg.get(p).getDisplayName() + " getötet.");
            }else{
                e.setDeathMessage(Main.main.prefix + p.getDisplayName() + " ist gestorben.");
            }
            for(Player alive : Main.main.alive) {
                try {
                    alive.hidePlayer(Main.main, p);
                }catch(Exception error){
                    Main.main.log(error.getMessage());
                }
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!p.isDead()) {
                        p.spigot().respawn();
                    }
                }
            }.runTaskLater(Main.main, 20);

        }else{
            e.setDeathMessage(null);
        }
        Main.main.windetection();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        ItemMeta compassmeta = compass.getItemMeta();
        compassmeta.setDisplayName("§bTeleporter");
        compass.setItemMeta(compassmeta);
        p.getInventory().addItem(compass);

        p.setAllowFlight(true);
        p.setFlying(true);
    }

}
