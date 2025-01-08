package si.f5.stsaria.mineHuntPvPLobbyer;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchingManager {
    private final static int[] ports = {25570, 25571, 25572};
    private final static int gamePlayerMax = 4;

    private static ArrayList<Integer> usingPorts = new ArrayList<Integer>();

    private static ArrayList<Integer> standByPorts = new ArrayList<Integer>();
    private static ArrayList<ArrayList<Player>> standByPlayers = new ArrayList<ArrayList<Player>>();
    private final Object lock = new Object();

    public Player p;

    public MatchingManager(Player p){
        this.p = p;
    }
    public int getPort(int i){
        return ports[i];
    }
    public int[] ifVacantAdd() {
        synchronized (lock) {
            for (int i = 0; i < standByPorts.size(); i++) {
                if (standByPlayers.get(i).size() >= gamePlayerMax) {
                    int port = standByPorts.get(i);
                    if (standByPlayers.get(i).size() + 1 >= gamePlayerMax) {
                        usingPorts.add(port);
                        standByPorts.remove(i);
                        standByPlayers.remove(i);
                        return new int[]{port, 1};
                    } else {
                        return new int[]{port, 0};
                    }
                }
            }
            for (int port : ports) {
                if (!usingPorts.contains(port) && !standByPorts.contains(port)) {
                    standByPorts.add(port);
                    standByPlayers.add(new ArrayList<Player>(Arrays.asList(this.p)));
                }
                return new int[]{port, 0};
            }
        }
        return new int[]{0, 0};
    }
}
