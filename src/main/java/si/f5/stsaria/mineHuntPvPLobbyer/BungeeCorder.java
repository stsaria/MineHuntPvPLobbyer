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
        int count = 0;
        while (player.isOnline() && count < 15) {
            out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(destination);
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            Thread.sleep(3000);
            count++;
        }
        if (count >= 15){
            player.sendMessage("§cSorry.... Failed start server or move server");
        }
    }
}
