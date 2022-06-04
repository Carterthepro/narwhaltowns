package narwhal.narwhaltowns;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private ItemStack money;
    public ItemStack getMoneyItem(){
        return money;
    }
    private int billSize;
    public ItemManager(int billSize){
        this.billSize = billSize;
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta =  item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN+""+ChatColor.BOLD+"Money");
        meta.addEnchant(Enchantment.LUCK, 1, false);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN+""+ChatColor.BOLD+ billSize);
        lore.add(ChatColor.GREEN+""+ChatColor.BOLD+"Narwhal Cash");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER, billSize);
        item.setItemMeta(meta);
        money = item;
    }

    public int getBillSize(){
        return billSize;
    }
    public void setBillSize(int billSize){
        this.billSize = billSize;
        ItemMeta meta =  money.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(0,ChatColor.GREEN+""+ChatColor.BOLD+billSize);
        meta.setLore(lore);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER, billSize);
        money.setItemMeta(meta);
    }
    public ItemStack getMoneyOfBillSize(int billSize){
        ItemStack bill = money.clone();
        ItemMeta meta =  bill.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(0,ChatColor.GREEN+""+ChatColor.BOLD+billSize);
        meta.setLore(lore);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER, billSize);
        bill.setItemMeta(meta);
        return bill;
    }
    public static boolean isMoney(ItemStack item){
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().hasLore()) return false;
        if (item.getItemMeta().getLore().size() < 2) return false;
        return item.getItemMeta().getLore().get(1).contains("Narwhal Cash");
    }
}