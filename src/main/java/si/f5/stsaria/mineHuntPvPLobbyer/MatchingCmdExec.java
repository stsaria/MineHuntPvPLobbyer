package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MatchingCmdExec implements CommandExecutor {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if (!(s instanceof Player)){
            return false;
        }
        Player p = (Player)s;

        return true;
    }
}
