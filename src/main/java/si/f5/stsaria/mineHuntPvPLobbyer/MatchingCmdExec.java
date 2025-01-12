package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchingCmdExec implements CommandExecutor {
    private final static int serverMoveWaitSec = 60;
    private final Plugin plugin;
    private final Logger logger;
    public MatchingCmdExec(Plugin plugin, Logger logger){
        this.plugin = plugin;
        this.logger = logger;
    }
    public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        Plugin plugin = this.plugin;
        AtomicBoolean atmicResult = new AtomicBoolean(false);
        try {
            CompletableFuture.supplyAsync(() -> {
                try {
                    if (!(s instanceof Player player)) {
                        return false;
                    }
                    player.sendTitle("Matching now....", null);
                    Matching matching = new Matching(player);
                    int[] result = matching.ifVacantAdd();
                    int port = result[0];
                    int needStart = result[1];
                    if (port == 0) {
                        matching.removeStandByPlayer(player);
                        player.sendTitle("Failed Match", null);
                        return false;
                    }
                    while (player.isOnline()) {
                        if (matching.isStarted()) {
                            break;
                        }
                    }
                    if (!(player.isOnline() && matching.isStarted())) {
                        matching.removeStandByPlayer(player);
                        player.sendTitle("Failed Match", null);
                        return false;
                    }
                    if (needStart == 1) {
                        ManhuntServer manhuntServer = new ManhuntServer(port, matching.standByPlayers, this.logger);
                        manhuntServer.start();
                    }
                    if (matching.isStarted()){
                        player.sendTitle("Matched!", "Please wait for server to start...");
                        Thread.sleep(2000);

                        for (int i = 0; i < serverMoveWaitSec-2; i++){
                            Thread.sleep(1000);
                            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 200f, 1f);
                            player.sendTitle(String.valueOf(serverMoveWaitSec-i-3), null);
                        }
                        player.playSound(player, Sound.ENTITY_IRON_GOLEM_STEP, 200f, 1f);
                        BungeeCorder.moveServer(plugin, player, "munhunt-" + String.valueOf(port));
                    }
                } catch (Exception e) {
                    this.logger.log(Level.SEVERE, e.toString());
                    return false;
                }
                return true;
            }).thenAcceptAsync(result -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    atmicResult.set(result);
                });
            });
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.toString());
        }
        return atmicResult.get();
    }
}
