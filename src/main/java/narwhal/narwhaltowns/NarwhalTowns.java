package narwhal.narwhaltowns;

import narwhal.narwhaltowns.Files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class NarwhalTowns extends JavaPlugin {
    public NarwhalTowns(){
        instance = this;
    }
    static NarwhalTowns instance;
    private DataManager townData;
    public DataManager getTownData(){
        return townData;
    }
    @Override
    public void onEnable() {

        townData = new DataManager(this,"towns");
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        this.getCommand("town").setExecutor(new TownCommands(this));


        for(String townName : townData.getConfig().getStringList("towns")){
            Town town = new Town(townName, townData);
            townData.getConfig().getStringList("towns."+townName+".members");
            //ADD MEMBERS
            if(!townData.getConfig().contains("towns."+townName+".chunkX") || !townData.getConfig().contains("towns."+townName+".chunkY")){
                Bukkit.getLogger().severe("Error loading town " + town.getName() + " as it has 0 x or y coordinates");
                continue;
            }
            List<Integer> xList = townData.getConfig().getIntegerList("towns."+townName+".chunkX");
            List<Integer> yList = townData.getConfig().getIntegerList("towns."+townName+".chunkY");
            if(xList.size()!=yList.size()) {
                Bukkit.getLogger().severe("Error loading town " + town.getName() + " as there's a different amount of x and y coordinates");
                continue;
            }
            for (int i = 0; i < xList.size(); i++) {
                town.addChunk(xList.get(i), yList.get(i));
            }


        }
    }
    @Override
    public void onDisable() {
        for (Town town:Town.getTowns()) {
            town.save();
        }
    }

}
