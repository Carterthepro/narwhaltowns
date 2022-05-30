package narwhal.narwhaltowns;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerMoveListener implements Listener {
    HashMap<Player, List<Integer>> oldChunk = new HashMap<>();
    HashMap<Player, Town> oldTownMap = new HashMap<>();
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        if(NarwhalPlayer.onlinePlayers.size()==0)return;
        int x = e.getPlayer().getLocation().getChunk().getX();
        int y = e.getPlayer().getLocation().getChunk().getZ();
        if(oldChunk.get(e.getPlayer())!=null) {
            int oldChunkX = oldChunk.get(e.getPlayer()).get(0);
            int oldChunkY = oldChunk.get(e.getPlayer()).get(1);

            if (oldChunkX != x || oldChunkY != y) {
                Town oldTown = oldTownMap.get(e.getPlayer());
                Chunk playerChunk = Chunk.getChunkFromCoords(x, y);
                if (playerChunk != null) {
                    Town town = (Town) playerChunk.getOwner("town");
                    oldTownMap.putIfAbsent(e.getPlayer(), town);
                    if (oldTown == town) return;
                    e.getPlayer().sendMessage("Entering " + town.getName());
                    e.getPlayer().sendTitle("Entering " + town.getName(), town.getName(), 5, 30, 5);
                    oldTown = town;
                } else {
                    if (oldTown != null) {
                        if (!oldTown.hasBeenDeleted()) {
                            e.getPlayer().sendMessage("Exiting " + oldTown.getName());
                            e.getPlayer().sendTitle("Exiting " + oldTown.getName(), oldTown.getName(), 5, 30, 5);
                        }
                    }
                    oldTown = null;
                }
                oldTownMap.replace(e.getPlayer(),oldTown);
            }
            oldChunk.get(e.getPlayer()).set(0,x);
            oldChunk.get(e.getPlayer()).set(1,y);
        }else {
            oldChunk.put(e.getPlayer(), new ArrayList<>());
            oldChunk.get(e.getPlayer()).add(x);
            oldChunk.get(e.getPlayer()).add(y);
        }

    }
}
