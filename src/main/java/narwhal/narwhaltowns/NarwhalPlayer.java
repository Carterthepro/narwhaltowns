package narwhal.narwhaltowns;

import narwhal.narwhaltowns.Files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NarwhalPlayer {
    public int money = 0;
    public NarwhalPlayer(Player player, DataManager data){
        this.player =player;
        onlinePlayers.add(this);
        Bukkit.getLogger().info("created narwhal player "+player.getName());
        this.data = data;
    }

    private final DataManager data;
    public void onDisconnect(PlayerQuitEvent event){
        save();
        NarwhalPlayer.onlinePlayers.remove(this);
    }
    public void save(){
        data.getConfig().set(player.getUniqueId()+".title",title);
        List<String> permList = new ArrayList<String>();
        for (Perms perm:perms) {
            permList.add(perm.toString());
        }
        data.getConfig().set(player.getUniqueId()+".perms",permList.toArray(new String[0]));
        List<String> territoryNames = new ArrayList<>();
        for (Territory territory:territories){
            territoryNames.add(territory.getName());
        }
        data.getConfig().set(player.getUniqueId()+".territories",territoryNames);

        data.saveConfig();
    }
    public static List<NarwhalPlayer> onlinePlayers = new ArrayList<NarwhalPlayer>();
    public static NarwhalPlayer convertPlayer(Player player){
        for (NarwhalPlayer narwhalPlayer: onlinePlayers) {
            if(narwhalPlayer.player == player){
                return narwhalPlayer;
            }
        }
        Bukkit.getLogger().severe("Cannot convert to narwhal player as player is not in NarwhalPlayer.Players");
        return null;
    }
    public static NarwhalPlayer getPlayerFromUUID(String uuid){
        for(NarwhalPlayer player: onlinePlayers){
            if(player.getPlayer().getUniqueId().toString().equalsIgnoreCase(uuid)){
                return player;
            }
        }
        return null;
    }
    public static NarwhalPlayer getPlayerFromUUID(UUID uuid){
        for(NarwhalPlayer player: onlinePlayers){
            if(player.getPlayer().getUniqueId().equals(uuid)){
                return player;
            }
        }
        return null;
    }

    private List<Territory> territories = new ArrayList<Territory>();
    public void addTerritory(Territory territory){
        for (Territory _territory: territories) {
            if(_territory.getType() == territory.getType()){
                Bukkit.getLogger().warning("Cannot add territory as it is already an owner of the chunk");
                return;
            }
        }
        territories.add(territory);
    }
    public void setTerritory(Territory territory){
        int i = 0;
        for (Territory _territory: territories) {
            if(_territory.getType() == territory.getType()){
                territories.set(i,territory);
                return;
            }
            i++;
        }
    }
    public void removeTerritory(Territory territory){
        territories.remove(territory);
    }
    public Territory getTerritory(String type){
        for (Territory territory: territories) {
            if(territory.getType() == type){
                return territory;
            }
        }
        return null;
    }

    private List<Perms> perms = new ArrayList<Perms>();
    private final Player player;
    public Player getPlayer(){return player;}
    public void setPerms(Perms[] perms){
        this.perms.clear();
        this.perms.addAll(Arrays.asList(perms));
    }
    public void clearPerms(){
        this.perms.clear();
    }
    public void addPerm(Perms perm){
        if(!perms.contains(perm)) perms.add(perm);
    }
    public void addPerms(Perms[] perms){
        for (Perms perm:perms) {
            if(!this.perms.contains(perm)) this.perms.add(perm);
        }
    }
    public void removePerm(Perms perm){
        perms.remove(perm);
    }
    public boolean hasPerm(Perms perm){return perms.contains(perm);}

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title =title;
    }
    private String title = "";

    public void addMoney(int amount) {
        ItemStack item = ItemManager.money;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(Integer.toString(amount));
        lore.add("Narwhal Cash");
        meta.setLore(lore);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER, amount);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
        money += amount;
    }

    public void setMoney(int amount) {
        money=amount;
        for (ItemStack item:player.getInventory().getContents()) {
            if(item!=null&&item.getItemMeta().getLore().get(1).equalsIgnoreCase("narwhal cash"))
            {
                player.getInventory().remove(item);
            }
        }
        if(money>1000){
            int x = money;
            while(x != 0){
                if(x < 1000)
                {
                    addMoney(x);
                    return;
                }
                x -= 1000;
                addMoney(1000);
                return;
            }
        }
        addMoney(amount);
    }
    public boolean removeMoney(int amount) {
        if(money >= amount)
        {
            setMoney(money-amount);
            return true;
        }
        return false;
    }

}
