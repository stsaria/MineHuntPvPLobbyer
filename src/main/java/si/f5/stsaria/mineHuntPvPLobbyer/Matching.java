package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Matching {
    private final int gamePlayer;

    private static ArrayList<Integer> startedPorts = new ArrayList<>();

    private static ArrayList<Integer> standByPorts = new ArrayList<>();
    private static ArrayList<ArrayList<Player>> standByPortsPlayers = new ArrayList<>();

    private static ArrayList<UUID> playingPlayerUUIDs = new ArrayList<>();

    private static final Object lock = new Object();

    public ArrayList<Player> standByPlayers;

    private final Player player;
    private final Configuration config;

    private int port = 0;

    public Matching(Player player, Configuration config){
        this.player = player;
        this.config = config;
        gamePlayer = config.getInt("mainServerPlayer");
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
                if (standByPortsPlayers.get(i).contains(player)) {
                    standByPortsPlayers.get(i).remove(player);
                    if (standByPortsPlayers.get(i).isEmpty()) {
                        standByPortsPlayers.remove(i);
                        standByPorts.remove(i);
                    }
                }
            }
        }
    }
    public int[] ifVacantAdd() {
        synchronized (lock) {
            if (playingPlayerUUIDs.contains(this.player.getUniqueId())){
                return new int[]{0, 0};
            }
            for (int i = 0; i < standByPorts.size(); i++) {
                if (standByPortsPlayers.get(i).size() <= gamePlayer) {
                    int port = standByPorts.get(i);
                    this.port = port;
                    standByPortsPlayers.get(i).add(this.player);
                    playingPlayerUUIDs.add(this.player.getUniqueId());
                    if (standByPortsPlayers.get(i).size() >= gamePlayer) {
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
            for (int portL : config.getIntegerList("mainServerPorts")) {
                if (!startedPorts.contains(portL) && !standByPorts.contains(portL)) {
                    standByPorts.add(portL);
                    this.port = portL;
                    standByPortsPlayers.add(new ArrayList<>(List.of(this.player)));
                    playingPlayerUUIDs.add(this.player.getUniqueId());
                    System.out.println(standByPortsPlayers.get(standByPorts.indexOf(portL)));
                    if (standByPortsPlayers.get(standByPorts.indexOf(portL)).size() >= gamePlayer) {
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