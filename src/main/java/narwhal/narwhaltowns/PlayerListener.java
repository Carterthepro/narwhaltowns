package narwhal.narwhaltowns;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
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

    @EventHandler
    public void EntityPickUpItemEvent(EntityPickupItemEvent e) {
        if (ItemManager.isMoney(e.getItem().getItemStack()) && e.getEntity() instanceof Player) {
            NarwhalPlayer.convertPlayer(((Player)e.getEntity())).addMoney(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER));
        }
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
        if (ItemManager.isMoney(e.getItemDrop().getItemStack())) {
            NarwhalPlayer.convertPlayer(e.getPlayer()).removeMoney(e.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER));
        }
    }


    @EventHandler
    public void JoinEvent(PlayerJoinEvent e){
        //it would probably be quicker to save how much money the player has and access it on join but this works for now
        for(ItemStack item : e.getPlayer().getInventory().getContents()){
            if(ItemManager.isMoney(item)){
                NarwhalPlayer.convertPlayer(e.getPlayer()).addMoney(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER)* item.getAmount());
            }
        }
    }

}
