package narwhal.narwhaltowns;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerListener implements Listener {

    HashMap<Player, List<Integer>> oldChunk = new HashMap<>();
    HashMap<Player, Town> oldTownMap = new HashMap<>();

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
    @EventHandler
    public void EntityPickUpItemEvent(EntityPickupItemEvent e) {
        if (ItemManager.isMoney(e.getItem().getItemStack()) && e.getEntity() instanceof Player) {
            NarwhalPlayer.convertPlayer(((Player)e.getEntity())).addBills(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER));
        }
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        if (ItemManager.isMoney(e.getItemDrop().getItemStack())) {
            NarwhalPlayer.convertPlayer(e.getPlayer()).removeBills(e.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER));
        }
    }

    @EventHandler
    public void JoinEvent(PlayerJoinEvent e){
        //it would probably be quicker to save how much money the player has and access it on join but this works for now
        for(ItemStack item : e.getPlayer().getInventory().getContents()){
            if(ItemManager.isMoney(item)){
                NarwhalPlayer.convertPlayer(e.getPlayer()).addBills(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER)* item.getAmount());
            }
        }
    }
}