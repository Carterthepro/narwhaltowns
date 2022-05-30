package narwhal.narwhaltowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        int x = e.getPlayer().getLocation().getChunk().getX();
        int y = e.getPlayer().getLocation().getChunk().getZ();
        NarwhalPlayer player = NarwhalPlayer.convertPlayer(e.getPlayer());
        if(player==null)return;
        if(x==player.getCurrentChunkX()&&y== player.getCurrentChunkY())return;
        //In new chunk
        Chunk currentChunk = Chunk.getChunkFromCoords(x,y);
        Town newTown;
        if(currentChunk==null) newTown = null;
        else newTown = (Town) currentChunk.getOwner("town");

        Town oldTown;
        if(player.getCurrentChunk()==null) oldTown = null;
        else oldTown = (Town) player.getCurrentChunk().getOwner("town");


        player.setCurrentChunk(currentChunk);
        //ADD NATIONS AND STUFF LATER
        if(newTown == oldTown)return;
        if(newTown==null)return;//Exit town
        //New Town
        e.getPlayer().sendMessage("Entering: "+newTown.getName());
        e.getPlayer().sendTitle("    ", newTown.getName(), 5, 30, 5);

    }
}
