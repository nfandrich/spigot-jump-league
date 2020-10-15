package de.nfandrich.net.countdowns;

import de.nfandrich.net.main.GameState;
import de.nfandrich.net.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Countdown {

    public static int lobbycd;
    public int gamecd;
    public int deathmatchcd;
    public int restartcd;

    public int lobby;
    public int game;
    public int deathmatch;
    public int restart;

    /**
     * Starts the lobby countdown
     */
    public void startLobbyCountdown() {
        lobby = Main.main.lobby;

        if(!Main.main.lobbyStarted) {
            Main.main.lobbyStarted = true;

            // The countdown task
            lobbycd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.main, new Runnable() {
                @Override
                public void run() {
                    if (lobby >= 1) {

                        if (lobby == 60 || lobby == 30 || lobby == 15 || (lobby <= 5)) {
                            Bukkit.broadcastMessage(Main.main.prefix + "Das Spiel startet in " + lobby + " §3Sekunden.");
                        }
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.setLevel(lobby);
                            if(lobby <= 5) {
                                all.playSound(all.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                            }
                        }
                    }else if (lobby == 0) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.setLevel(0);
                        }
                        Main.main.lm.mapTeleport();
                        startGameCountdown();
                        Bukkit.getScheduler().cancelTask(lobbycd);
                    }
                    lobby--;
                }
            }, 0, 20L);
        }
    }

    /**
     * Starts the game countdown
     */
    public void startGameCountdown() {
        Main.main.state = GameState.INGAME;
        game = Main.main.ingame;

        gamecd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.main, new Runnable() {
            @Override
            public void run() {

                if(game == 0) {
                    Bukkit.getScheduler().cancelTask(gamecd);
                    startDeathmatchCountdown();
                }
                game--;
            }
        }, 0, 20L);

    }

    /**
     * Starts the deathmatch countdown
     */
    public void startDeathmatchCountdown() {
        Main.main.state = GameState.DEATHMATCH;
        deathmatch = Main.main.deathmatch;

        deathmatchcd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.main, new Runnable() {
            @Override
            public void run() {

                if (deathmatch >= 1) {

                    if (deathmatch == 10 || (deathmatch <= 5)) {
                        Bukkit.broadcastMessage(Main.main.prefix + "Das Deathmatch startet in " + deathmatch + " §3Sekunden.");
                    }
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.setLevel(lobby);
                        if(lobby <= 5) {
                            all.playSound(all.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        }
                    }
                }else if(deathmatch == 0) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.setLevel(0);
                    }
                    Main.main.lm.deathMatchTeleport();
                    Bukkit.getScheduler().cancelTask(gamecd);
                }
                deathmatch--;
            }
        }, 0, 20L);

    }

    /**
     * Starts the restart countdown
     */
    public void startRestartCountdown() {
        Main.main.state = GameState.RESTARTING;
        restart = Main.main.restart;

        if(!Main.main.restartStarted) {
            Main.main.restartStarted = true;

            // The countdown task
            restartcd = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.main, new Runnable() {
                @Override
                public void run() {
                    if (restart >= 1) {
                        if (restart == 60 || restart == 30 || restart == 15 || (restart <= 5)) {
                            Bukkit.broadcastMessage(Main.main.prefix + "§cDer Server startet neu in §6" + restart + " §cSekunden.");
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.setLevel(restart);
                            }
                        }
                    } else if (restart == 0) {
                        Bukkit.getScheduler().cancelTask(restartcd);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
                    }
                    restart--;
                }
            }, 0, 20L);
        }
    }

    public void cancelLobbyCountdown() {
        // Cancel lobby runnable
        Bukkit.getScheduler().cancelTask(lobbycd);

        // Set level 0
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setLevel(0);
        }

        // Debug output
        Main.main.log("§cDer Countdown wurde angehalten (" + Bukkit.getOnlinePlayers().size() + "/" + Main.main.max + ")");
    }

}
