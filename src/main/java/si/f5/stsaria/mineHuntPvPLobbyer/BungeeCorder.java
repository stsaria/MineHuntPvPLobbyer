package si.f5.stsaria.mineHuntPvPLobbyer;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BungeeCorder {
    public static void moveServer(Plugin plugin, Player player, String destination) throws InterruptedException {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        ByteArrayDataOutput out;
        while (player.isOnline()) {
            out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(destination);
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            Thread.sleep(2000);
        }
    }
}
