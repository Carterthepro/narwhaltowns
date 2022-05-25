package narwhal.narwhaltowns;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankCommands implements CommandExecutor {
    public BankCommands(NarwhalTowns plugin){
        this.plugin = plugin;
    }
    NarwhalTowns plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //temp money command
        if(label.equalsIgnoreCase("givemoney")){
            if(sender instanceof Player)
            {
                Player player = (Player) sender;
                Money money = new Money(Integer.parseInt(args[0]));
                player.getInventory().addItem(money);
                return true;
            }
            sender.sendMessage("Console cannot execute this command");
            return false;
        }
        return false;
    }
}
