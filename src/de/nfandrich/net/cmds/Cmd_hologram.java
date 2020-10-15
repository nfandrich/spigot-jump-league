package de.nfandrich.net.cmds;

import de.nfandrich.net.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmd_hologram implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if(s instanceof Player) {
            Player p = (Player) s;

            if(args.length != 0) {
                if(args[0].equalsIgnoreCase("new") && args[1].length() > 0) {
                    Main.main.hm.createHologram(p, args);
                }else{
                    p.sendMessage(Main.main.prefix + "§cDer Hologram-Text darf nicht leer sein.");
                }
                return true;
            }else{
                p.sendMessage(Main.main.prefix + "Verwendung: /hologram new <Zeile 1> <Zeile 2> <Zeile 3>");
            }
        }

        return false;
    }
}
