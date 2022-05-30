package narwhal.narwhaltowns;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlaceOrBreakListener implements Listener {

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e){
        NarwhalPlayer player = NarwhalPlayer.convertPlayer(e.getPlayer());
        if(player==null)return;
        if(!player.CanInteractWith(e.getBlock(), TownPerms.breakAndPlace))e.setCancelled(true);

    }
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e){
        NarwhalPlayer player = NarwhalPlayer.convertPlayer(e.getPlayer());
        if(player==null)return;
        if(!player.CanInteractWith(e.getBlock(), TownPerms.breakAndPlace))e.setCancelled(true);
    }

}
