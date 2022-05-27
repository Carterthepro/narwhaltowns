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
        if(!(sender instanceof Player)){
            sender.sendMessage("Console has infinite money");
            return false;
        }
        NarwhalPlayer player = NarwhalPlayer.convertPlayer(((Player)sender));
        if(player==null)return false;
        if(args.length == 0){
            //RUN HELP COMMAND
            return false;
        }
        switch (args[0].toLowerCase()){
            case "get":
                player.addMoney(1000);
                return true;
            case "bal":
                player.getPlayer().sendMessage("You have "+player.getMoney()+" money");
                return true;
            case "take":
                player.removeMoney(100);
                return true;
            default:
                return false;
        }
    }
}