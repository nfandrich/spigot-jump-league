package de.nfandrich.net.cmds;

import de.nfandrich.net.main.GameState;
import de.nfandrich.net.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Cmd_start implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
        if(cmd.getName().equalsIgnoreCase("start")) {
            if(args.length == 0 && Main.main.state == GameState.LOBBY) {
                if(s.isOp()) {
                    if(Main.main.cd.lobby > 5) {
                        Main.main.cd.lobby = 5;
                        s.sendMessage("§aDu hast das Spiel vorzeitig gestartet.");
                    }else{
                        s.sendMessage("§cDas Spiel startet bereits.");
                    }
                }
            }else{
                s.sendMessage("§cDas Spiel läuft bereits.");
            }
        }
        return false;
    }

}
