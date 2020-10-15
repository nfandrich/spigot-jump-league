package de.nfandrich.net.cmds;

import de.nfandrich.net.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmd_setlobby implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if(s instanceof Player) {
            Player p = (Player) s;

            if(args.length != 0) {
                return true;
            }
            Main.main.lm.setLocation("lobby", p.getLocation());
            p.sendMessage(Main.main.prefix + "Du hast die Lobby erfolgreich gesetzt!");
        }

        return false;
    }
}
