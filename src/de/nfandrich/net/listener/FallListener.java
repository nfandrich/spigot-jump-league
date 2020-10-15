package de.nfandrich.net.listener;

import de.nfandrich.net.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class FallListener implements Listener {

    @EventHandler
    public void onFall(PlayerDeathEvent e) {
        Player player = e.getEntity();
        // Check if fall distance exceeds 1
        if(player.getFallDistance() > 1) {
            Main.main.lm.teleportToPlayerSpawn(player);
        }
    }

}