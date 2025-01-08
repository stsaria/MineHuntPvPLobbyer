package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.plugin.java.JavaPlugin;

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
