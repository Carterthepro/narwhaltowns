package narwhal.narwhaltowns.shops;

import narwhal.narwhaltowns.Files.DataManager;
import narwhal.narwhaltowns.NarwhalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BuyShop extends ChestShop{
    public BuyShop(Block _chest, String _owner, Material _item_type, Integer _price, Integer _max, DataManager _data) {
        super(_chest, _owner, _item_type, _price, _max, _data);
    }

    public Boolean buy(Integer amount, String seller){
        if(!Bukkit.getPlayer(UUID.fromString(seller)).getInventory().contains(item_type, amount)){
            return false;
        }
        if(num+amount>max){
            return false;
        }
        //currently just taking money directly from player, would be best to implement some kind of shop bank account system
        if(NarwhalPlayer.convertPlayer(Bukkit.getPlayer(UUID.fromString(owner))).getMoney()<price*amount){
            return false;
        }

        for(int i = 1; i > amount+1; i++) {
            Bukkit.getPlayer(UUID.fromString(seller)).getInventory().remove(item_type);
        }
        ((Chest) chest.getState()).getInventory().addItem(new ItemStack(item_type, amount));
        NarwhalPlayer.convertPlayer(Bukkit.getPlayer(UUID.fromString(owner))).removeBills(price*amount);
        NarwhalPlayer.convertPlayer(Bukkit.getPlayer(UUID.fromString(seller))).addBills(price*amount);
        num += amount;
        return true;
    }

    @Override
    public void setLastLine(Sign sign) {
        sign.setLine(3, "Sell");
    }
}