package si.f5.stsaria.mineHuntPvPLobbyer;

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ManhuntServer extends Thread{
    private final static String serversPath = System.getProperty("user.home")+"/manhuntServers/";
    private final long serverTimeoutMinutes = 40;

    private final int port;
    private final ArrayList<Player> players;
    private final Logger logger;

    public ManhuntServer(int port, ArrayList<Player> players, Logger logger){
        this.port = port;
        this.players = players;
        this.logger = logger;
    }
    public void run(){
        try {
            for (String dirS : new ArrayList<String>(Arrays.asList("world", "world_nether"))){
                File dir = new File(serversPath + String.valueOf(port) + "/"+dirS+"/");
                if (dir.isDirectory()){
                    FileUtils.deleteDirectory(dir);
                }
            }
            FileWriter fw = new FileWriter(new File(serversPath + String.valueOf(port) + "/eula.txt"));
            fw.write("eula=true");
            fw.close();

            Paths.get("src/resources/").toFile();

            InJarFileUtils.copyResourcesFileToLocalFile("server.properties", serversPath + String.valueOf(port) + "/server.properties");
            InJarFileUtils.copyResourcesFileToLocalFile("spigot.yml", serversPath + String.valueOf(port) + "/spigot.yml");

            Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "java -jar server.jar --port "+String.valueOf(port)+" --nogui"}, null, new File(serversPath + String.valueOf(port)));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            for (Player player : this.players){
                String userName = player.getName();
                writer.write("whitelist add "+userName+"\n");
                writer.flush();
            }
            process.waitFor(serverTimeoutMinutes, TimeUnit.MINUTES);

            Matching.removeStartedPort(this.port);
            for (Player player : this.players){
                Matching.removePlayingPlayerUUID(player.getUniqueId());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
