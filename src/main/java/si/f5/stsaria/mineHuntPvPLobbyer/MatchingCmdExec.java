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
        Player player = (Player)s;
        player.sendTitle("Matching....", null);
        MatchingManager matchingManager = new MatchingManager(player);
        int[] result = matchingManager.ifVacantAdd();
        int port = result[0];
        int needStart = result[1];
        while(player.isOnline()){
            try {
                if (matchingManager.isStarted()){
                    break;
                }
                Thread.sleep(1000);
            } catch(Exception e) {
                break;
            }
        }
        if (!player.isOnline()){
            return false;
        }
        if (needStart == 1){
            MunhuntServer munhuntServer = new MunhuntServer(port);
            munhuntServer.start();
        }
        BungeeCordMoveServer bungeeCordMoveServer = new BungeeCordMoveServer();
        bungeeCordMoveServer.move(player, String.valueOf(port));
        return true;
    }
}
