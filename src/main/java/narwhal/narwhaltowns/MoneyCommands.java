package narwhal.narwhaltowns;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommands implements CommandExecutor {

    NarwhalTowns plugin;

    public MoneyCommands(NarwhalTowns plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("getmoney") && sender instanceof Player)
        {
            NarwhalPlayer nplayer = NarwhalPlayer.convertPlayer((Player) sender);
            nplayer.addMoney(1000);
            return true;
        }
        if(label.equalsIgnoreCase("mymoney") && sender instanceof Player)
        {
            NarwhalPlayer nplayer = NarwhalPlayer.convertPlayer((Player) sender);
            nplayer.getPlayer().sendMessage(Integer.toString(nplayer.money));
            return true;
        }
        if(label.equalsIgnoreCase("takemoney") && sender instanceof Player)
        {
            NarwhalPlayer nplayer = NarwhalPlayer.convertPlayer((Player) sender);
            nplayer.removeMoney(100);
            return true;
        }
        return false;
    }
}