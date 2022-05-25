package narwhal.narwhaltowns;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Money extends ItemStack {
    public Money(int value)
    {
        itemValue = value;

        this.setType(Material.PAPER);
        this.setAmount(1);
        ItemMeta meta = getItemMeta();

        //editing meta
        meta.setDisplayName("Money");
        List<String> lore = new ArrayList<>();
        lore.add("$"+Integer.toString(value));
        lore.add("This is Genuine Server Currency");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        //setting meta
        setItemMeta(meta);
    }

    private static int itemValue;

    public int getValue()
    {
        return itemValue;
    }

}