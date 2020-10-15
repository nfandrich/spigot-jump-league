package de.nfandrich.net.listener;

import de.nfandrich.net.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(Main.main.alive.contains(p)) {
            Bukkit.getServer().broadcastMessage("§b" + p.getDisplayName() + "§8: §7" + e.getMessage());
        }else{
            for(Player all : Bukkit.getServer().getOnlinePlayers()) {
                if(!Main.main.alive.contains(all)){
                    all.sendMessage("§7[§4X§7]§8" + p.getDisplayName() + "§8: §7" + e.getMessage());
                }
            }
        }
        e.setCancelled(true);
    }

}
