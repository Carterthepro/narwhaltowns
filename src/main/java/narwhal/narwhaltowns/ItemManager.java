package narwhal.narwhaltowns;

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
    public static ItemStack money;

    public static void init() {
        createMoney();
    }

    private static void createMoney() {
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta =  item.getItemMeta();
        meta.setDisplayName("Money");
        meta.addEnchant(Enchantment.LUCK, 1, false);
        List<String> lore = new ArrayList<>();
        lore.add("0");
        lore.add("Narwhal Cash");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(NarwhalTowns.getPlugin(), "value"), PersistentDataType.INTEGER, 0);
        item.setItemMeta(meta);
        money = item;
    }
}