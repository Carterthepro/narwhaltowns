package narwhal.narwhaltowns;


import narwhal.narwhaltowns.Files.DataManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Town extends Territory{
    private static List<Town> Towns = new ArrayList<>();
    public Town(String name, DataManager data){
        super(name,"town",data);
        Towns.add(this);
    }
    public static List<Town> getTowns(){return Towns;}

    @Override
    public void destroy() {
        Towns.remove(this);
        super.destroy();
        data.getConfig().set(getName(),null);
    }
    @Override
    public boolean save() {
            data.getConfig().set(getName() + ".members",getMembers());
        List<Integer> xList = new ArrayList<>();
        List<Integer> yList = new ArrayList<>();
        for(Chunk chunk: getChunks()){
            xList.add(chunk.getX());
            yList.add(chunk.getY());
        }
        data.getConfig().set(getName() + ".chunksX",xList.toArray(new Integer[0]));
        data.getConfig().set(getName() + ".chunksY",yList.toArray(new Integer[0]));
        data.saveConfig();
        return true;
    }

    public static Town getTownFromName(String name){
        for (Town town : Towns) {
            if(town.getName().equalsIgnoreCase(name))return town;
        }
        return null;
    }
}
