package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchingManager {
    private final static int[] ports = {25570, 25571, 25572};
    private final static int gamePlayerMax = 4;

    private static ArrayList<Integer> startedPorts = new ArrayList<Integer>();

    private static ArrayList<Integer> standByPorts = new ArrayList<Integer>();
    private static ArrayList<ArrayList<Player>> standByPlayers = new ArrayList<ArrayList<Player>>();
    private final Object lock = new Object();

    private final Player player;
    private int port = 0;

    public MatchingManager(Player player){
        this.player = player;
    }
    public boolean isStarted(){
        return startedPorts.contains(this.port);
    }
    public int[] ifVacantAdd() {
        synchronized (lock) {
            for (int i = 0; i < standByPorts.size(); i++) {
                if (standByPlayers.get(i).size() >= gamePlayerMax) {
                    int port = standByPorts.get(i);
                    this.port = port;
                    if (standByPlayers.get(i).size() + 1 >= gamePlayerMax) {
                        startedPorts.add(port);
                        standByPorts.remove(i);
                        standByPlayers.remove(i);
                        return new int[]{port, 1};
                    } else {
                        return new int[]{port, 0};
                    }
                }
            }
            for (int port : ports) {
                if (!startedPorts.contains(port) && !standByPorts.contains(port)) {
                    standByPorts.add(port);
                    this.port = port;
                    standByPlayers.add(new ArrayList<Player>(Arrays.asList(this.player)));
                }
                return new int[]{port, 0};
            }
        }
        return new int[]{0, 0};
    }
}
