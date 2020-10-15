package de.nfandrich.net.manager;

import de.nfandrich.net.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class LocationManager {

    private static File file;
    private static File fileTwo;
    private static FileConfiguration locationsFile;
    private static FileConfiguration deathMatchFile;

    /**
     * Finds or generates the locations.yml
     */
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("CMDPlugin").getDataFolder(), "locations.yml");
        fileTwo = new File(Bukkit.getServer().getPluginManager().getPlugin("CMDPlugin").getDataFolder(), "deathmatch.yml");

        if(!file.exists()) {
            try{
                file.createNewFile();
            }catch(IOException e){
                System.out.println(e);
            }
        }

        if(!fileTwo.exists()) {
            try{
                fileTwo.createNewFile();
            }catch(IOException e){
                System.out.println(e);
            }
        }
        locationsFile = YamlConfiguration.loadConfiguration(file);
        deathMatchFile = YamlConfiguration.loadConfiguration(fileTwo);
    }

    /**
     * Returns file configuration
     * @return The locationsFile
     */
    public static FileConfiguration get() {
        return locationsFile;
    }

    /**
     * Reloads the locations file
     */
    public static void reload() {
        locationsFile = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Sets a new location;
     * @param name The world name
     * @param loc The location
     */
    public void setLocation(String name, Location loc) {
        locationsFile.set(name + ".world", loc.getWorld().getName());
        locationsFile.set(name + ".x",     loc.getX());
        locationsFile.set(name + ".y",     loc.getY());
        locationsFile.set(name + ".z",     loc.getZ());
        locationsFile.set(name + ".yaw",   loc.getYaw());
        locationsFile.set(name + ".pitch", loc.getPitch());
        save();
    }

    /**
     * Sets a spawn location
     * @param num The spawn ID
     * @param loc The location
     */
    public void setSpawn(int num, Location loc) {
        String name = "Spawn";
        locationsFile.set(name + "." + num + ".world", loc.getWorld().getName());
        locationsFile.set(name + "." + num + ".x",     loc.getX());
        locationsFile.set(name + "." + num + ".y",     loc.getY());
        locationsFile.set(name + "." + num + ".z",     loc.getZ());
        locationsFile.set(name + "." + num + ".yaw",   loc.getYaw());
        locationsFile.set(name + "." + num + ".pitch", loc.getPitch());
        save();
    }

    /**
     * Sets a death match spawn location
     * @param num The spawn ID
     * @param loc The location
     */
    public void setDeathMatchSpawn(int num, Location loc) {
        String name = "Spawn";
        deathMatchFile.set(name + "." + num + ".world", loc.getWorld().getName());
        deathMatchFile.set(name + "." + num + ".x",     loc.getX());
        deathMatchFile.set(name + "." + num + ".y",     loc.getY());
        deathMatchFile.set(name + "." + num + ".z",     loc.getZ());
        deathMatchFile.set(name + "." + num + ".yaw",   loc.getYaw());
        deathMatchFile.set(name + "." + num + ".pitch", loc.getPitch());
        save();
    }

    /**
     * Gets an existing location;
     * @param name The world name
     * @return The location
     */
    public Location getLocation(String name) {
        // Main.main.log(locationsFile.getString(name + ".world"));

        // Get requested world
        World world = Bukkit.getWorld(locationsFile.getString(name + ".world"));
        double x = locationsFile.getDouble(name + ".x");
        double y = locationsFile.getDouble(name + ".y");
        double z = locationsFile.getDouble(name + ".z");

        // Create location from world data
        Location loc = new Location(world, x, y, z);
        loc.setYaw(locationsFile.getInt(name + ".yaw"));
        loc.setPitch(locationsFile.getInt(name + ".pitch"));
        return loc;
    }

    /**
     * Gets an existing spawn location
     * @param num The spawn ID
     * @return The location
     */
    public Location getSpawn(int num) {
        String name = "Spawn";

        // Get requested world
        World world = Bukkit.getWorld(locationsFile.getString(name + "." + num + ".world"));
        double x = locationsFile.getDouble(name + "." + num + ".x");
        double y = locationsFile.getDouble(name + "." + num + ".y");
        double z = locationsFile.getDouble(name + "." + num + ".z");

        // Create location from world data
        Location loc = new Location(world, x, y, z);
        loc.setYaw(locationsFile.getInt(name + "." + num + ".yaw"));
        loc.setPitch(locationsFile.getInt(name + "." + num + ".pitch"));

        return loc;
    }

    /**
     * Gets an existing death match spawn location
     * @param num The spawn ID
     * @return The location
     */
    public Location getDeathMatchSpawn(int num) {
        String name = "Spawn";

        // Get requested world
        World world = Bukkit.getWorld(deathMatchFile.getString(name + "." + num + ".world"));
        double x = deathMatchFile.getDouble(name + "." + num + ".x");
        double y = deathMatchFile.getDouble(name + "." + num + ".y");
        double z = deathMatchFile.getDouble(name + "." + num + ".z");

        // Create location from world data
        Location loc = new Location(world, x, y, z);
        loc.setYaw(deathMatchFile.getInt(name + "." + num + ".yaw"));
        loc.setPitch(deathMatchFile.getInt(name + "." + num + ".pitch"));

        return loc;
    }

    /**
     * Saves the locations file
     */
    public static void save() {
        try{
            locationsFile.save(file);
            deathMatchFile.save(fileTwo);
        }catch(IOException e){
            Main.main.log(e.getMessage());
        }
    }

    /**
     * Teleports player to player spawn
     */
    public void mapTeleport() {
        int count = 1;
        for(Player alive : Main.main.alive) {
            Main.main.playerSpawns.put(alive.getDisplayName(), getSpawn(count));
            alive.teleport(getSpawn(count));
            count++;
        }
    }

    /**
     * Teleports player to player spawn
     */
    public void deathMatchTeleport() {
        int count = 1;
        for(Player alive : Main.main.alive) {
            Main.main.playerSpawns.put(alive.getDisplayName(), getSpawn(count));
            alive.teleport(getDeathMatchSpawn(count));
            count++;
        }
    }

    /**
     * Teleport died player to his game spawn
     * @param p The player
     */
    public void teleportToPlayerSpawn(Player p) {
        if(Main.main.checkpoints.containsKey(p.getDisplayName())) {
            Location location = Main.main.checkpoints.get(p.getDisplayName());
            location.setPitch(1);
            location.setYaw(1);

            p.teleport(location);
        }else{
            if(Main.main.playerSpawns.containsKey(p.getDisplayName())) {
                p.teleport(Main.main.playerSpawns.get(p.getDisplayName()));
            }
        }
    }

}
