package de.nfandrich.net.main;

import de.nfandrich.net.cmds.*;
import de.nfandrich.net.countdowns.Countdown;
import de.nfandrich.net.listener.*;
import de.nfandrich.net.manager.FileManager;
import de.nfandrich.net.manager.HologramManager;
import de.nfandrich.net.manager.LocationManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static Main main;
    public GameState state;
    public ArrayList<Player> alive;
    public String prefix = "§7[§bJumpWorld§7] §3";
    public FileManager fm;
    public LocationManager lm;
    public HologramManager hm;
    public Countdown cd;
    public Utils utils;
    public ChestListener cl;

    public int min = getConfig().getInt("MinPlayers");
    public int max = getConfig().getInt("MaxPlayers");

    public int lobby = 30;
    public int ingame = 30*60;
    public int deathmatch = 15*60;
    public int restart = 15;

    public boolean lobbyStarted = false;
    public boolean restartStarted = false;

    public HashMap<Player, Player> lastdmg;
    public HashMap<String, Location> playerSpawns;
    public HashMap<String, Location> playerDeathMatchSpawns;
    public HashMap<String, Location> checkpoints;
    public ArrayList<Block> checkpointBlocks;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        LocationManager.setup();
        LocationManager.get().options().copyDefaults();
        LocationManager.save();

        main = this;
        utils = new Utils();

        // Set game state
        state = GameState.LOBBY;

        // Get alive players
        alive = new ArrayList<>();

        // Get damaging players
        lastdmg = new HashMap<>();

        // Get player spawns
        playerSpawns = new HashMap<>();

        // Get player deathmatch spawns
        playerDeathMatchSpawns = new HashMap<>();

        // Get checked checkpoints
        checkpoints = new HashMap<>();

        // Get checked checkpoint blocks
        checkpointBlocks = new ArrayList<>();

        // Enable Managers
        fm = new FileManager();
        lm = new LocationManager();
        hm = new HologramManager();
        cd = new Countdown();
        cl = new ChestListener();
        fm.saveCfg();
        fm.register();
        cl.registerLoot();

        // Register commands
        this.getCommand("setlobby").setExecutor(new Cmd_setlobby());
        this.getCommand("setspawn").setExecutor(new Cmd_setspawn());
        this.getCommand("setdmspawn").setExecutor(new Cmd_setdmspawn());
        this.getCommand("start").setExecutor(new Cmd_start());
        this.getCommand("hologram").setExecutor(new Cmd_hologram());

        // Register Events
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new OnlineListener(), this);
        pm.registerEvents(new EnvironmentListener(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new NavigatorListener(), this);
        pm.registerEvents(new DeathListener(), this);
        pm.registerEvents(new FallListener(), this);
        pm.registerEvents(cl, this);
        cl.registerLoot();

        log("The Plugin has been enabled!");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void windetection() {
        if(state == GameState.INGAME || state == GameState.DEATHMATCH) {
            if(Bukkit.getServer().getOnlinePlayers().size() == 0) {
                Bukkit.shutdown();
                cd.startRestartCountdown();
            }else{
                if(alive.size() == 1) {
                    Player winner = alive.get(0);
                    Bukkit.getServer().broadcastMessage(winner.getDisplayName() + " §ehat das Spiel gewonnen!");
                    cd.startRestartCountdown();
                }
            }
        }

        if(state == GameState.LOBBY){
            if(alive.size() == 1) {
                // Cancel lobby countdown
                cd.cancelLobbyCountdown();

                // Set lobby countdown to default
                this.lobbyStarted = false;
            }
        }
    }

    public void log(String message) {
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage(this.prefix + message);
    }
}
