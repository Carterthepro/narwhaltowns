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
        getServer().getPluginManager().registerEvents(new ChatListener(),this);
        this.getCommand("town").setExecutor(new TownCommands(this));

        for(String townName : townData.getConfig().getKeys(false)){
            Town town = new Town(townName, townData);
            Bukkit.getLogger().info("Loading town: "+townName);

            if(townData.getConfig().contains(townName+".members")) {
                for (String member : townData.getConfig().getStringList(townName + ".members")) {
                    town.addMember(member);
                }
            }else{
                Bukkit.getLogger().severe("Error loading town " + town.getName() + " members are empty");
                continue;
            }
            if(!townData.getConfig().contains(townName+".chunksX") || !townData.getConfig().contains(townName+".chunksY")){
                Bukkit.getLogger().severe("Error loading town " + town.getName() + " as it has 0 x or y coordinates");
                //continue;
            }
            List<Integer> xList = townData.getConfig().getIntegerList(townName+".chunksX");
            List<Integer> yList = townData.getConfig().getIntegerList(townName+".chunksY");
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
        for (NarwhalPlayer player : NarwhalPlayer.onlinePlayers){
            player.save();
        }
    }

}
