package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("NullableProblems")
public class MatchingCmdExec implements CommandExecutor {
    private final int serverMoveWaitSec;
    private final Plugin plugin;
    private final Logger logger;
    private final Configuration config;

    public MatchingCmdExec(Plugin plugin){
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.config = plugin.getConfig();
        serverMoveWaitSec = config.getInt("lobbyToMainServerMoveWaitSec");
    }
    public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        Plugin plugin = this.plugin;
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        try {
            CompletableFuture.supplyAsync(() -> {
                try {
                    if (!(s instanceof Player player)) {
                        return false;
                    }
                    player.playSound(player, Sound.MUSIC_DISC_CHIRP, 200f, 1f);
                    player.sendTitle("Matching now....", "", 20, 60, 2);
                    Matching matching = new Matching(player, this.config);
                    int[] result = matching.ifVacantAdd();
                    int port = result[0];
                    int needStart = result[1];
                    if (port == 0) {
                        Matching.removePlayingPlayerUUID(player.getUniqueId());
                        matching.removeStandByPlayer(player);
                        player.sendTitle("Failed Match", "", 20, 60, 2);
                        return false;
                    }
                    while (player.isOnline()) {
                        if (matching.isStarted()) {
                            break;
                        }
                    }
                    player.stopAllSounds();
                    if (!(player.isOnline() && matching.isStarted())) {
                        Matching.removePlayingPlayerUUID(player.getUniqueId());
                        matching.removeStandByPlayer(player);
                        player.sendTitle("Failed Match", "", 20, 60, 2);
                        return false;
                    }
                    if (needStart == 1) {
                        ManhuntServer manhuntServer = new ManhuntServer(port, matching.standByPlayers, this.plugin);
                        manhuntServer.start();
                    }
                    if (matching.isStarted()){
                        player.playSound(player, Sound.MUSIC_DISC_OTHERSIDE, 200f, 1f);
                        player.sendTitle("Matched!", "Please wait for server to start...", 20, 60, 2);
                        Thread.sleep(2000);

                        for (int i = 0; i < serverMoveWaitSec-2; i++) {
                            Thread.sleep(1000);
                            player.sendTitle(String.valueOf(serverMoveWaitSec - i - 3), "", 20, 60, 20);
                        }
                        player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 200f, 1f);
                        player.sendTitle("Server Started!", "Have fun!!", 20, 60, 2);
                        Thread.sleep(2500);
                        BungeeCorder.moveServer(this.plugin, player, "manhunt-" + port);
                    }
                } catch (Exception e) {
                    this.logger.log(Level.SEVERE, e.toString());
                    return false;
                }
                return true;
            }).thenAcceptAsync(result -> Bukkit.getScheduler().runTask(plugin, () -> atomicBoolean.set(result)));
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.toString());
        }
        return atomicBoolean.get();
    }
}
