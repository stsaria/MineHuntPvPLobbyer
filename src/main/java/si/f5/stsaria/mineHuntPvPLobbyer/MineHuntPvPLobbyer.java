package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Period;

public final class MineHuntPvPLobbyer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("matching").setExecutor(new MatchingCmdExec());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
