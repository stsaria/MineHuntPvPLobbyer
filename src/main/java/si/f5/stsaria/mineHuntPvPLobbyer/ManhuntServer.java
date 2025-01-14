package si.f5.stsaria.mineHuntPvPLobbyer;

import org.apache.commons.io.FileUtils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManhuntServer extends Thread{
    private final String workingDir;
    private final long serverTimeoutMinutes;

    private final int port;
    private final ArrayList<Player> players;
    private final Logger logger;
    private final Configuration config;

    public ManhuntServer(int port, ArrayList<Player> players, Plugin plugin){
        this.port = port;
        this.players = players;
        this.logger = plugin.getLogger();
        this.config = plugin.getConfig();
        this.serverTimeoutMinutes = plugin.getConfig().getInt("mainServerTimeoutMinutes");
        this.workingDir = plugin.getDataFolder().getAbsolutePath()+"/"+port;
        Path workingDirPath = Paths.get(workingDir);
        try{
            if (workingDirPath.toFile().isDirectory()){
                FileUtils.deleteDirectory(workingDirPath.toFile());
            }
            Files.createDirectory(workingDirPath);
            Files.createDirectory(Paths.get(workingDir+"/plugins"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }
    public void run(){
        try {
            PaperDownloader.downloadLatestBuild(Bukkit.getVersion().split("-")[0], this.workingDir + "/server.jar");
            httpGet.download(this.config.getString("manhuntMainDownloadURL"), workingDir + "/plugins/manhunt.jar");

            FileWriter fw = new FileWriter(this.workingDir + "/eula.txt");
            fw.write("eula=true");
            fw.close();

            InJarFileUtils.copyResourcesFileToLocalFile("server.properties", this.workingDir + "/server.properties");
            InJarFileUtils.copyResourcesFileToLocalFile("spigot.yml", this.workingDir + "/spigot.yml");

            Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", this.config.getString("mainServerJavaPath")+" -Xmx"+this.config.getInt("mainServerXmxGB")+"G -Xms"+this.config.getInt("mainServerXmsGB")+"G -jar server.jar --port " + port +" --nogui"}, null, new File(this.workingDir));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            for (String userName : this.config.getStringList("mainServerOps")){
                writer.write("op " + userName + "\n");
                writer.flush();
            }
            for (Player player : this.players){
                String userName = player.getName();
                writer.write("whitelist add " + userName + "\n");
                writer.flush();
            }
            process.waitFor(serverTimeoutMinutes, TimeUnit.MINUTES);

            Matching.removeStartedPort(this.port);
            for (Player player : this.players){
                Matching.removePlayingPlayerUUID(player.getUniqueId());
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }
}
