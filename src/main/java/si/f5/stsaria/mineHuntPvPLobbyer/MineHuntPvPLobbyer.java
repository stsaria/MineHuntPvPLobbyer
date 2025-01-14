package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MineHuntPvPLobbyer extends JavaPlugin {
    private final Logger logger = this.getLogger();
    private final FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        Path pluginDir = Paths.get(this.getDataFolder().getAbsolutePath());
        try{
            if (!pluginDir.toFile().exists()){
                Files.createDirectory(pluginDir);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }

        this.config.addDefault("gamePlayerMax", 4);
        this.config.addDefault("serverMoveWaitSec", 60);
        this.config.addDefault("serverTimeoutMinutes", 34);
        this.config.addDefault("manhuntMainDownloadURL", "https://github.com/stsaria/MineHuntPvPMain/releases/download/v1.0/ManhuntEarthMain-1.0-SNAPSHOT.jar");
        this.config.addDefault("ports", new ArrayList<>(Arrays.asList(25570, 25571)));
        this.config.addDefault("mainServerOps", new ArrayList<String>(List.of()));
        config.options().copyDefaults(true);
        this.saveConfig();

        if (this.config.getInt("gamePlayerMax") > 6){
            this.logger.log(Level.WARNING, "The maximum value for gamePlayer is 6, but you set it to "+this.config.getInt("gamePlayerMax") +". Therefore, it has been automatically adjusted to 8.");
            this.config.set("gamePlayerMax", 6);
        }

        Objects.requireNonNull(getCommand("matching")).setExecutor(new MatchingCmdExec(this));
    }

    @Override
    public void onDisable() {
    }
}
