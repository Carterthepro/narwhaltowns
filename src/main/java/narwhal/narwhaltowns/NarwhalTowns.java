package narwhal.narwhaltowns;

import narwhal.narwhaltowns.Files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public final class NarwhalTowns extends JavaPlugin {
    public NarwhalTowns(){
        instance = this;
    }
    private static NarwhalTowns instance;
    private static DataManager townData;
    private static DataManager playerData;
    private static DataManager bankData;
    private static DataManager shopData;
    public static DataManager getTownData(){
        return townData;
    }
    public static DataManager getPlayerData(){
        return playerData;
    }
    public static DataManager getBankData(){
        return bankData;
    }
    public static DataManager getShopData(){
        return shopData;
    }

    @Override
    public void onEnable() {

        townData = new DataManager(this,"towns");
        playerData = new DataManager(this,"players");
        bankData = new DataManager(this, "banks");
        shopData = new DataManager(this, "shops");
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerListener(),this);
        this.getCommand("town").setExecutor(new TownCommands(this));
        this.getCommand("money").setExecutor(new MoneyCommands(this));
        this.getCommand("bank").setExecutor(new MoneyCommands(this));
        this.getCommand("shop").setExecutor(new MoneyCommands(this));

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

        for(String bankName : bankData.getConfig().getKeys(false)){
            Bank bank = null;
            if(bankData.getConfig().contains(bankName+".sponsor")){
                bank = new SmallBank(bankName, bankData.getConfig().getString(bankName+".owner"), getBankData(), Territory.getTerritoryFromName(getConfig().getString(bankName+".sponsor")));
            }
            else {
                bank = new SmallBank(bankName, bankData.getConfig().getString(bankName + ".owner"), getBankData());
            }
            Bukkit.getLogger().info("Loading bank: "+bankName);

            if(bankData.getConfig().contains(bankName+".members")){
                for(String member : bankData.getConfig().getStringList(bankName+".members")){
                    bank.addMember(member);
                }
            }

            if(bankData.getConfig().contains(bankName+".wealth")){
                Integer i = 0;
                for(Integer num : bankData.getConfig().getIntegerList(bankName+".wealth")){
                    bank.setMoney(bank.getMembers().get(i), num);
                    i++;
                }
            }

            if(bankData.getConfig().contains(bankName+".chests")){
                List<Location> location = (List<Location>) bankData.getConfig().getList(bankName+".chests");
                for(Location l : location){
                    bank.addChest(l);
                }
            }
            bank.calculatePoolSize();
        }

        SmallBank.setInterest(0.01);

        BankInterestEnactor ie = new BankInterestEnactor();
        int id = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {@Override
            public void run() {
                ie.EnactInterest();
            }}, 0, 48000);
    }
    @Override
    public void onDisable() {
        for (Town town:Town.getTowns()) {
            town.save();
        }
        for (NarwhalPlayer player : NarwhalPlayer.onlinePlayers){
            player.save();
        }
        for(Bank bank : Bank.getBanks()){
            bank.save();
        }
    }
    public static NarwhalTowns getPlugin(){
        return instance;
    }

}

