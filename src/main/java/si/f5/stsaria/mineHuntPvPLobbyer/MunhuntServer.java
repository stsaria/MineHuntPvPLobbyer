package si.f5.stsaria.mineHuntPvPLobbyer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class MunhuntServer extends Thread{
    private final static String serversPath = "~/munhuntServers/";
    private final static String worldsPath = "~/munhuntWorlds/";
    private final static String[] worlds = {"ham", "pin", "bos"};
    private final static int memGB = 1;

    private final int port;

    public MunhuntServer(int port){
        this.port = port;
    }
    public void run(){
        String world = worlds[new Random().nextInt(0, worlds.length-1)];
        try {
            FileUtils.copyDirectory(new File(worldsPath + world + "/"), new File(serversPath + "world/"));
            FileWriter fw = new FileWriter(new File(worldsPath + world + "/eula.txt"));
            fw.write("eula=true");
            fw.close();
            Runtime.getRuntime().exec(new String[]{"jar", "-jar", "-Xmx" + String.valueOf(memGB) + "G", "server.jar", "port="+String.valueOf(port), "&"}, null, new File(serversPath));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
