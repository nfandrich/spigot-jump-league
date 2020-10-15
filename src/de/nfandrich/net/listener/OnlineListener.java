package de.nfandrich.net.listener;

import de.nfandrich.net.countdowns.Countdown;
import de.nfandrich.net.main.GameState;
import de.nfandrich.net.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class OnlineListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Countdown cd = new Countdown();
        if(Main.main.state == GameState.LOBBY) {

            // Adds player to array "alive" if he was not add yet
            if(!Main.main.alive.contains(p)) {
                if(Main.main.alive.add(p)) {
                    // Main.main.log("Added player to list \"alive players\"");
                }
            }

            // Send message to chat
            e.setJoinMessage(p.getDisplayName() + " §7hat das Spiel betreten (" + Bukkit.getOnlinePlayers().size() + "/" + Main.main.max + ")");

            // Teleport player to lobby
            p.teleport(Main.main.lm.getLocation("lobby"));

            // Clear player's inventory
            Main.main.utils.clearPlayer(p);

            // Checks if game is "full"
            if(Bukkit.getOnlinePlayers().size() == Main.main.min) {
                cd.startLobbyCountdown();
            }
        }else {
            p.kickPlayer("§cDas Spiel hat schon begonnen!");
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if(Main.main.state != GameState.LOBBY) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "§cDas Spiel hat schon begonnen!");
            return;
        }else{
            if(Main.main.alive.size() >= Main.main.max) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cDas Spiel ist voll!");
            }
        }
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        e.setMaxPlayers(Main.main.max);
        if(Main.main.state == GameState.LOBBY) {
            e.setMotd("§6Lobby");
        }else if(Main.main.state == GameState.INGAME){
            e.setMotd("§2Ingame");
        }else if(Main.main.state == GameState.RESTARTING) {
            e.setMotd("§4Restarting");
        }else if(Main.main.state == GameState.DEATHMATCH) {
            e.setMotd("§2Deathmatch");
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(Main.main.state == GameState.INGAME) {
            e.setQuitMessage(p.getDisplayName() + " §7ist gestorben.");
        }else{
            e.setQuitMessage(p.getDisplayName() + " §7hat das Spiel verlassen.");
        }

        if(Main.main.alive.contains(p)) {
            if(Main.main.alive.remove(p)) {
            }
        }
        Main.main.windetection();
    }

}
