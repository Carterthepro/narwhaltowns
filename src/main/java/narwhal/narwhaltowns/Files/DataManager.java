package narwhal.narwhaltowns.Files;

import narwhal.narwhaltowns.NarwhalTowns;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataManager {
    private NarwhalTowns plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(NarwhalTowns plugin, String fileName){
        this.plugin = plugin;
        this.fileName = fileName+".yml";
        saveDefaultConfig();
    }
    private String fileName;
    public void reloadConfig(){
        if(configFile == null)
            configFile = new File(this.plugin.getDataFolder(),fileName);
        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource(fileName);
        if(defaultStream!=null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            defaultConfig.setDefaults(defaultConfig);
        }
    }
    public FileConfiguration getConfig(){
        if(dataConfig == null)reloadConfig();
        return dataConfig;
    }

    public void saveConfig(){
        if(dataConfig== null || configFile == null)
            return;
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("COULD NOT SAVE CONFIG TO "+configFile+". "+e.getMessage());
        }
    }
    public void saveDefaultConfig(){
        if(configFile==null)
            configFile = new File(plugin.getDataFolder(), fileName);

        if(!configFile.exists()){
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName,false);
        }
    }
}
