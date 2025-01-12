package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class MineHuntPvPLobbyer extends JavaPlugin {
    Logger logger = this.getLogger();

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("matching")).setExecutor(new MatchingCmdExec(this, this.logger));
    }

    @Override
    public void onDisable() {
    }
}
