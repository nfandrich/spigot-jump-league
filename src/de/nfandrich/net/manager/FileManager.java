package de.nfandrich.net.manager;

import de.nfandrich.net.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager {

    public File file = new File("plugins/"+ Main.main.getName(), "config.yml");
    public FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    /**
     * Registers the config.
     */
    public void register() {
        cfg.options().copyDefaults(true);
        cfg.addDefault("MinPlayers", Main.main.min);
        cfg.addDefault("MaxPlayers", Main.main.max);
        saveCfg();
        // Main.main.min = cfg.getInt("MinPlayer");
        // Main.main.max = cfg.getInt("MaxPlayer");
    }

    /**
     * Saves the configuration file.
     */
    public void saveCfg() {
        try {
            cfg.save(file);
        }catch(Exception error) {
            error.printStackTrace();
        }
    }

}
