package narwhal.narwhaltowns;


import narwhal.narwhaltowns.Files.DataManager;

import java.util.ArrayList;
import java.util.List;

public class Town extends Territory{
    private static List<Town> Towns = new ArrayList<Town>();
    public Town(String name, DataManager data){
        super(name,"town",data);
        Towns.add(this);
    }
    public static List<Town> getTowns(){return Towns;}

    @Override
    public void destroy() {
        super.destroy();
        Towns.remove(this);
        data.getConfig().set("towns."+getName(),null);
    }
    @Override
    public boolean save() {
        for(String player : getMembers()) {
            data.getConfig().set("towns." + getName() + ".members",player);
        }
        List<Integer> xList = new ArrayList<>();
        List<Integer> yList = new ArrayList<>();
        for(Chunk chunk: getChunks()){
            xList.add(chunk.getX());
            yList.add(chunk.getY());
        }
        data.getConfig().set("towns." + getName() + ".chunksX",xList.toArray(new Integer[0]));
        data.getConfig().set("towns." + getName() + ".chunksY",yList.toArray(new Integer[0]));
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
