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

        this.config.addDefault("lobbyToMainServerMoveWaitSec", 60);
        this.config.addDefault("mainServerTimeoutMinutes", 34);
        this.config.addDefault("mainServerPlayer", 4);
        this.config.addDefault("mainServerXmxGB", 1);
        this.config.addDefault("mainServerXmsGB", 1);
        this.config.addDefault("mainServerPorts", new ArrayList<>(Arrays.asList(25570, 25571)));
        this.config.addDefault("mainServerOps", new ArrayList<>(List.of("plssetyourname")));
        this.config.addDefault("mainServerJavaPath", "/usr/bin/java");
        this.config.addDefault("manhuntMainDownloadURL", "https://github.com/stsaria/MineHuntPvPMain/releases/download/v1.3/ManhuntEarthMain-1.3.jar");
        config.options().copyDefaults(true);
        this.saveConfig();

        Objects.requireNonNull(getCommand("matching")).setExecutor(new MatchingCmdExec(this));
    }

    @Override
    public void onDisable() {
    }
}
