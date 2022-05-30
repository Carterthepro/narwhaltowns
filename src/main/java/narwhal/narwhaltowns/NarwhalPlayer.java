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
    public NarwhalPlayer(Player player, NarwhalTowns plugin) {
        this.player = player;
        onlinePlayers.add(this);
        Bukkit.getLogger().info("created narwhal player " + player.getName());
        data = NarwhalTowns.getPlayerData();
        this.plugin = plugin;
    }

    private final NarwhalTowns plugin;
    private DataManager data;

    public void onDisconnect(PlayerQuitEvent event) {
        Town town = (Town) getTerritory("town");
        if (town != null)
            town.disconnectPlayer(this);
        save();
        NarwhalPlayer.onlinePlayers.remove(this);
    }

    public void save() {
        data.getConfig().set(player.getUniqueId() + ".title", title);
        List<String> permList = new ArrayList<String>();
        for (Perms perm : perms) {
            permList.add(perm.toString());
        }
        data.getConfig().set(player.getUniqueId() + ".perms", permList.toArray(new String[0]));
        List<String> territoryNames = new ArrayList<>();
        for (Territory territory : territories) {
            territoryNames.add(territory.getName());
        }
        data.getConfig().set(player.getUniqueId() + ".territories", territoryNames);

        data.saveConfig();
    }

    public static List<NarwhalPlayer> onlinePlayers = new ArrayList<NarwhalPlayer>();

    public static NarwhalPlayer convertPlayer(Player player) {
        for (NarwhalPlayer narwhalPlayer : onlinePlayers) {
            if (narwhalPlayer.player == player) {
                return narwhalPlayer;
            }
        }
        Bukkit.getLogger().severe("Cannot convert to narwhal player as player is not in NarwhalPlayer.Players");
        return null;
    }

    public static NarwhalPlayer getPlayerFromUUID(String uuid) {
        for (NarwhalPlayer player : onlinePlayers) {
            if (player.getPlayer().getUniqueId().toString().equalsIgnoreCase(uuid)) {
                return player;
            }
        }
        return null;
    }

    public static NarwhalPlayer getPlayerFromUUID(UUID uuid) {
        for (NarwhalPlayer player : onlinePlayers) {
            if (player.getPlayer().getUniqueId().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    private List<Territory> territories = new ArrayList<Territory>();

    public void addTerritory(Territory territory) {
        for (Territory _territory : territories) {
            if (_territory.getType() == territory.getType()) {
                Bukkit.getLogger().warning("Cannot add territory as it the player already has a territory of that type");
                return;
            }
        }
        territories.add(territory);
    }

    public void setTerritory(Territory territory) {
        int i = 0;
        for (Territory _territory : territories) {
            if (_territory.getType() == territory.getType()) {
                territories.set(i, territory);
                return;
            }
            i++;
        }
    }

    public void removeTerritory(Territory territory) {
        territories.remove(territory);
    }

    public Territory getTerritory(String type) {
        for (Territory territory : territories) {
            if (territory.getType() == type) {
                return territory;
            }
        }
        return null;
    }

    private List<Perms> perms = new ArrayList<>();
    private final Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPerms(Perms[] perms) {
        this.perms.clear();
        this.perms.addAll(Arrays.asList(perms));
    }

    public void clearPerms() {
        this.perms.clear();
    }

    public void addPerm(Perms perm) {
        if (!perms.contains(perm)) perms.add(perm);
    }

    public void addPerms(Perms[] perms) {
        for (Perms perm : perms) {
            if (!this.perms.contains(perm)) this.perms.add(perm);
        }
    }

    public void removePerm(Perms perm) {
        perms.remove(perm);
    }

    public boolean hasPerm(Perms perm) {
        return perms.contains(perm);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title = "";


    private int money = 0;

    public int getMoney() {
        return money;
    }

    public static ItemManager itemManager = new ItemManager(1000);

    //ADD CHECK IF INV FULL
    public void addMoney(int amount) {
        int maxBillSize = itemManager.getBillSize();
        ItemStack item = itemManager.getMoneyItem();
        for (int i = 0; i < amount / maxBillSize; i++) {
            player.getInventory().addItem(item);
        }
        if (amount % maxBillSize != 0) {
            player.getInventory().addItem(itemManager.getMoneyOfBillSize(amount % maxBillSize));
        }
        money += amount;
    }

    public void clearMoney() {
        for (ItemStack item : player.getInventory().getContents()) {
            if(itemManager.isMoney(item))
                player.getInventory().remove(item);
        }
        money = 0;
    }
        public void setMoney (int amount){
            clearMoney();
            addMoney(amount);
        }
        public boolean removeMoney (int amount){
            if (money >= amount) {
                setMoney(money - amount);
                return true;
            }
            return false;
        }


}

