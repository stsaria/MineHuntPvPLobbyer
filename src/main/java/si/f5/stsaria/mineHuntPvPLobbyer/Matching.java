package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class Matching {
    private final static String serversPath = System.getProperty("user.home")+"/manhuntServers/";
    private final static int[] ports = Matching.getPorts();
    private final static int gamePlayerMax = 4;

    private static ArrayList<Integer> startedPorts = new ArrayList<Integer>();

    private static ArrayList<Integer> standByPorts = new ArrayList<Integer>();
    private static ArrayList<ArrayList<Player>> standByPortsPlayers = new ArrayList<ArrayList<Player>>();

    private static ArrayList<UUID> playingPlayerUUIDs = new ArrayList<UUID>();

    private static final Object lock = new Object();

    public ArrayList<Player> standByPlayers;

    private final Player player;
    private int port = 0;

    public Matching(Player player){
        this.player = player;
    }
    private static int[] getPorts(){
        ArrayList<Integer> portsTemp = new ArrayList<Integer>();
        try {
            if (!Paths.get(serversPath).toFile().isDirectory()){
                return new int[0];
            }
            Stream<Path> dirStream = Files.list(Paths.get(serversPath));
            ArrayList<Path> dirList = new ArrayList<Path>();
            dirStream.forEach(dirList::add);
            for (Path dir : dirList){
                if (!(new File(dir.toString()+"/server.jar").isFile() && new File(dir.toString()+"/plugins/manhunt.jar").isFile())){
                    continue;
                }
                portsTemp.add(Integer.valueOf(dir.toString().split("/")[dir.toString().split("/").length-1]));
            }
            return portsTemp.stream().mapToInt(Integer::intValue).toArray();
        } catch (Exception e) {
            return new int[0];
        }
    }
    public boolean isStarted(){
        synchronized (lock) {
            return startedPorts.contains(this.port);
        }
    }
    public static void removeStartedPort(int port){
        synchronized (lock) {
            startedPorts.remove((Integer) port);
        }
    }
    public static void removePlayingPlayerUUID(UUID uuid){
        synchronized (lock) {
            playingPlayerUUIDs.remove(uuid);
        }
    }
    public void removeStandByPlayer(Player player){
        synchronized (lock) {
            for (int i = 0; i < standByPortsPlayers.size(); i++) {
                standByPortsPlayers.get(i).remove(player);
            }
        }
    }
    public int[] ifVacantAdd() {
        synchronized (lock) {
            if (playingPlayerUUIDs.contains(this.player.getUniqueId())){
                return new int[]{0, 0};
            }
            for (int i = 0; i < standByPorts.size(); i++) {
                if (standByPortsPlayers.get(i).size() <= gamePlayerMax) {
                    int port = standByPorts.get(i);
                    this.port = port;
                    playingPlayerUUIDs.add(player.getUniqueId());
                    if (standByPortsPlayers.get(i).size() + 1 >= gamePlayerMax) {
                        startedPorts.add(port);
                        standByPorts.remove(i);
                        this.standByPlayers = standByPortsPlayers.get(i);
                        standByPortsPlayers.remove(i);
                        return new int[]{port, 1};
                    } else {
                        return new int[]{port, 0};
                    }
                }
            }
            for (int portL : ports) {
                if (!startedPorts.contains(portL) && !standByPorts.contains(portL)) {
                    standByPorts.add(portL);
                    this.port = portL;
                    standByPortsPlayers.add(new ArrayList<Player>(List.of(this.player)));
                    playingPlayerUUIDs.add(player.getUniqueId());
                    if (standByPortsPlayers.get(standByPorts.indexOf(portL)).size() + 1 >= gamePlayerMax) {
                        startedPorts.add(portL);
                        this.standByPlayers = standByPortsPlayers.get(standByPorts.indexOf(portL));
                        standByPortsPlayers.remove(standByPorts.indexOf(portL));
                        standByPorts.remove((Integer) portL);
                        return new int[]{port, 1};
                    }
                    return new int[]{portL, 0};
                }
            }
        }
        return new int[]{0, 0};
    }
}
