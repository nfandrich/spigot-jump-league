package de.nfandrich.net.cmds;

import de.nfandrich.net.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmd_setdmspawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if(s instanceof Player) {
            Player p = (Player) s;

            if(args.length != 1) {
                p.sendMessage(Main.main.prefix + "/setdmspawn <Nummer>");
                return true;
            }
            try {
                int number = Integer.parseInt(args[0]);
                Main.main.lm.setDeathMatchSpawn(number, p.getLocation());
                p.sendMessage(Main.main.prefix + "Du hast den Deathmatch Spawn [" + number +"] erfolgreich gesetzt!");
            }catch (Exception error) {
                p.sendMessage(Main.main.prefix + "§cDu musst eine Zahl übergeben!");
                // error.printStackTrace();
            }
        }

        return false;
    }

}
