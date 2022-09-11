package narwhal.narwhaltowns.shops;

import narwhal.narwhaltowns.Files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ChestShop {

    ChestShop(Block _chest, String _owner, Material _item_type, Integer _price, Integer _max, DataManager _data){
        chest = _chest;
        owner = _owner;
        item_type = _item_type;
        price = _price;
        max = _max;
        data = _data;
        shops.add(this);

        sign = chest.getRelative(((Directional) chest.getBlockData()).getFacing());


        if(checkSignLocation()) {
            sign.setType(Material.OAK_WALL_SIGN);
            BlockState signState = sign.getState();
            Sign signStateAsSign = (Sign) signState;
            signStateAsSign.setLine(0, item_type.name());
            signStateAsSign.setLine(1, price.toString());
            signStateAsSign.setLine(2, num.toString() + "/" + max.toString());
            ((Directional) signStateAsSign.getBlockData()).setFacing(((Directional) chest.getBlockData()).getFacing());
            setLastLine(signStateAsSign);
            WallSign wallSign = (WallSign) signState.getBlockData();
            signState.setBlockData(wallSign);
        }

    }

    public void setLastLine(Sign sign){
        sign.setLine(3, "abstract");
    }

    public boolean checkSignLocation(){
        if(sign.getType()!=Material.AIR){
            Bukkit.getPlayer(UUID.fromString(owner)).sendMessage("block in front of chest must be cleared to make room for sign");
            return false;
        }
        return true;
    }
    Block chest;
    String owner;
    Material item_type;
    Integer price;
    Integer max;
    Integer num = 0;
    DataManager data;

    Block sign;

    static List<ChestShop> shops = new ArrayList<>();
}
