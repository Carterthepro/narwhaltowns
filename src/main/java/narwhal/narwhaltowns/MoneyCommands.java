package narwhal.narwhaltowns;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class MoneyCommands implements CommandExecutor {

    NarwhalTowns plugin;

    public MoneyCommands(NarwhalTowns plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("money")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Console has infinite money");
                return false;
            }
            NarwhalPlayer player = NarwhalPlayer.convertPlayer(((Player) sender));
            if (player == null) return false;
            if (args.length == 0) {
                //RUN HELP COMMAND
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "get":
                    player.addMoney(1000);
                    return true;
                case "bal":
                    player.getPlayer().sendMessage("You have " + player.getMoney() + " money");
                    return true;
                case "take":
                    player.removeMoney(100);
                    return true;
                default:
                    return false;
            }
        } else if (label.equalsIgnoreCase("bank")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Console cant use banks");
                return false;
            }

            NarwhalPlayer nPlayer = NarwhalPlayer.convertPlayer(((Player) sender).getPlayer());

            switch (args[0].toLowerCase()) {

                case "create":
                    if(args[1]==null)
                    {
                        sender.sendMessage("wrong usage of command please use /bank create [name]");
                        return false;
                    }
                    if(args[2]==null)
                    {
                        Bank bank = new Bank(args[1], nPlayer);
                        return true;
                    }
                    Territory territory = Territory.getTerritoryFromName(args[2]);
                    Bank bank = new Bank(args[1], nPlayer, territory);
                    return true;

                case "addchest":
                    if(args[1]==null)
                    {
                        sender.sendMessage("please specify the name of the bank in the second argument");
                        return false;
                    }
                    Bank ownedBank = nPlayer.getOwnedBanksFromString(args[1]);
                    Block block = ((Player) sender).getTargetBlock(null, 5);
                    if(block instanceof Chest)
                    {
                        ownedBank.addChest((Chest) block);
                        return true;
                    }
                    sender.sendMessage("player must be targeting chest to use this command");
                    return false;

                default:
                    return false;
            }
        }
        return false;
    }
}