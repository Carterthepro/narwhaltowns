package narwhal.narwhaltowns;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private int oldChunkX;
    private int oldChunkY;
    private Town oldTown = null;

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        if(NarwhalPlayer.Players.size()==0)return;
        int x = e.getPlayer().getLocation().getChunk().getX();
        int y = e.getPlayer().getLocation().getChunk().getZ();

        if(oldChunkX!=x || oldChunkY!=y) {
            Chunk playerChunk = Chunk.getChunkFromCoords(x, y);
            if(playerChunk!=null){
                Town town = (Town) playerChunk.getOwner("town");
                if(oldTown == town)return;
                e.getPlayer().sendMessage("Entering "+town.getName());
                e.getPlayer().sendTitle("Entering "+town.getName(),town.getName(),5,30,5);
                oldTown = town;
            }else{
                if(oldTown != null) {
                    if(!oldTown.hasBeenDeleted()) {
                        e.getPlayer().sendMessage("Exiting " + oldTown.getName());
                        e.getPlayer().sendTitle("Exiting " + oldTown.getName(), oldTown.getName(), 5, 30, 5);
                    }
                }
                oldTown = null;
            }
        }
        oldChunkY = y;
        oldChunkX = x;

    }
}
