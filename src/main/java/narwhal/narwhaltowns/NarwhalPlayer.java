package narwhal.narwhaltowns;

import narwhal.narwhaltowns.Files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NarwhalPlayer {
    public NarwhalPlayer(Player player, DataManager data){
        this.player =player;
        Players.add(this);
        Bukkit.getLogger().info("created narwhal player "+player.getName());
        this.data = data;
    }

    private final DataManager data;
    public void onDisconnect(PlayerQuitEvent event){
        save();
        NarwhalPlayer.Players.remove(this);
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
    public static List<NarwhalPlayer> Players = new ArrayList<NarwhalPlayer>();
    public static NarwhalPlayer convertPlayer(Player player){
        for (NarwhalPlayer narwhalPlayer:Players) {
            if(narwhalPlayer.player == player){
                return narwhalPlayer;
            }
        }
        Bukkit.getLogger().severe("Cannot convert to narwhal player as player is not in NarwhalPlayer.Players");
        return null;
    }
    public static NarwhalPlayer getPlayerFromUUID(String uuid){
        for(NarwhalPlayer player:Players){
            if(player.getPlayer().getUniqueId().toString().equalsIgnoreCase(uuid)){
                return player;
            }
        }
        return null;
    }
    public static NarwhalPlayer getPlayerFromUUID(UUID uuid){
        for(NarwhalPlayer player:Players){
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
}


