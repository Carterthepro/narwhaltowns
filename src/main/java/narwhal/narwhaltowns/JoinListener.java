package narwhal.narwhaltowns;

import narwhal.narwhaltowns.Files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinListener implements Listener {
    public JoinListener(NarwhalTowns plugin){
        this.plugin = plugin;
    }
    private DataManager data = NarwhalTowns.getPlayerData();
    private NarwhalTowns plugin;
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        NarwhalPlayer player = new NarwhalPlayer(e.getPlayer());
        UUID uuid = e.getPlayer().getUniqueId();
        if (data.getConfig().contains(uuid.toString())) {
            if (data.getConfig().contains(uuid + ".perms")) {
                List<String> permsList = data.getConfig().getStringList(uuid + ".perms");
                List<Perms> perms = new ArrayList<>();
                for (String perm : permsList) {
                    perms.add(Perms.valueOf(perm));
                }
                player.addPerms(perms.toArray(new Perms[0]));
            }
            if (data.getConfig().contains(uuid + ".title")) {
                player.setTitle(data.getConfig().getString(uuid + ".title"));
            }
            if (data.getConfig().contains(uuid + ".territories")) {
                for (String territoryName : data.getConfig().getStringList(uuid + ".territories")) {
                    Territory territory = Territory.getTerritoryFromName(territoryName);
                    if(territory==null){
                        Bukkit.getLogger().info("TERRITORY == NULL");
                        return;
                    }
                    if(territory.connectMember(player)) {
                        player.addTerritory(territory);
                    }

                }
            }
        }
        else{player.save();}
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){

        NarwhalPlayer player = NarwhalPlayer.convertPlayer(e.getPlayer());
        player.onDisconnect(e);
        e.setQuitMessage(player.getPlayer().getDisplayName()+" has left");
    }
}
