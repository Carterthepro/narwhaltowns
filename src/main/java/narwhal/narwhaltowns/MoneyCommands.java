package narwhal.narwhaltowns;

import narwhal.narwhaltowns.shops.BuyShop;
import narwhal.narwhaltowns.shops.ChestShop;
import narwhal.narwhaltowns.shops.SellShop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

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
                //TODO HELP COMMAND
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "get":
                    getMoney(player, args);
                    return true;

                case "bal":
                    bal(player);
                    return true;

                case "take":
                    takeMoney(player, args);
                    return true;
                default:
                    player.getPlayer().sendMessage("unrecognized argument!");
                    return true;
            }

        }
        else if (label.equalsIgnoreCase("bank")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Console cant use banks");
                return false;
            }

            NarwhalPlayer nPlayer = NarwhalPlayer.convertPlayer(((Player) sender).getPlayer());

            switch (args[0].toLowerCase()) {

                case "create":
                    createBank(nPlayer, args);
                    return true;

                case "getbanks":
                    getBanks(nPlayer, args);
                    return true;

                case "addchest":
                    addChest(nPlayer, args);
                    return true;

                case "addmember":
                    addMember(nPlayer, args);
                    return true;

                case "info":
                    bankInfo(nPlayer, args);
                    return true;

                case "deposit":
                    deposit(nPlayer, args);
                    return true;

                case "withdraw" :
                    withdraw(nPlayer, args);
                    return true;

                case "bal":
                    getBalance(nPlayer, args);
                    return true;

                default:
                    return true;
            }
        }
        else if(label.equalsIgnoreCase("shop")){
            NarwhalPlayer nPlayer = NarwhalPlayer.convertPlayer((Player) sender);
            switch(args[0]){
                case "create":
                    createShop(nPlayer,args);
                    return true;

                default:
                    nPlayer.getPlayer().sendMessage("invalid argument or lack of argument, try again!");
            }
        }
        return false;
    }

    boolean getMoney(NarwhalPlayer player, String[] args){
        if(args.length < 2)
        {
            player.getPlayer().sendMessage("insufficient args try /money get [amount]");
            return false;
        }
        try {
            player.addBills(Integer.parseInt(args[1]));
        }catch (Exception e){
            player.getPlayer().sendMessage("amount argument must be an integer");
            return false;
        }
        return true;
    }
    boolean takeMoney(NarwhalPlayer player, String[] args){
        if(args.length < 2)
        {
            player.getPlayer().sendMessage("insufficient args try /money take [amount]");
            return false;
        }

        try {
            if(player.getMoney() < Integer.parseInt(args[1]))
            {
                player.getPlayer().sendMessage("you cannot have a negative balance!(try a lower number)");
            }
            player.removeBills(Integer.getInteger(args[1]));
        }catch (Exception e){
            player.getPlayer().sendMessage("amount argument must be an integer");
            return false;
        }
        return true;
    }
    boolean bal(NarwhalPlayer player){
        player.getPlayer().sendMessage("You have " + player.getMoney() + " money");
        return true;
    }

    boolean createBank(NarwhalPlayer nPlayer, String[] args){
        if(args.length < 2)
        {
            nPlayer.getPlayer().sendMessage("wrong usage of command please use /bank create [name]");
            return false;
        }
        if(args.length < 3)
        {
            Bank bank = new SmallBank(args[1], nPlayer.getPlayer().getUniqueId().toString(), NarwhalTowns.getBankData());
            nPlayer.addBank(bank);
            bank.addMember(nPlayer.getPlayer().getUniqueId().toString());
            nPlayer.getPlayer().sendMessage("successfully create bank!");
            return true;
        }
        Territory territory = Territory.getTerritoryFromName(args[2]);
        Bank bank = new SmallBank(args[1], nPlayer.getPlayer().getUniqueId().toString(), NarwhalTowns.getBankData(), territory);
        return true;
    }
    boolean getBanks(NarwhalPlayer nPlayer, String[] args){
        if(nPlayer.getBanks().size() == 0){
            nPlayer.getPlayer().sendMessage("You do not belong to any banks!");
            return true;
        }
        for(Bank _bank : nPlayer.getBanks()){
            nPlayer.getPlayer().sendMessage(_bank.getName());
        }
        return true;
    }
    boolean addChest(NarwhalPlayer nPlayer, String[] args){
        if(args[1]==null)
        {
            nPlayer.getPlayer().sendMessage("please specify the name of the bank in the second argument");
            return false;
        }
        Bank bank = nPlayer.getOwnedBanksFromString(args[1]);
        if(bank==null){
            nPlayer.getPlayer().sendMessage("unrecognized bank");
            return false;
        }
        Block block = nPlayer.getPlayer().getTargetBlock(null, 5);
        Chest chest = (Chest) block.getState();
        if(chest == null){
            nPlayer.getPlayer().sendMessage("player must be targeting chest to use this command");
            return true;
        }
        bank.addChest(block.getLocation());
        nPlayer.getPlayer().sendMessage("successfully added chest");
        return true;
    }
    boolean addMember(NarwhalPlayer nPlayer, String[] args){
        if(args.length < 3){
            nPlayer.getPlayer().sendMessage("insufficient arguments proper usage is /bank addmember [member name] [bank name]");
        }
        //TODO add invite support
        NarwhalPlayer target = NarwhalPlayer.getPlayerFromUUID(Bukkit.getPlayer(args[1]).getUniqueId());
        Bank bank = Bank.getBankFromName(args[2]);
        if(target == null){
            nPlayer.getPlayer().sendMessage("member name not recognized");
            return false;
        }if(bank == null){
            nPlayer.getPlayer().sendMessage("bank name not recognized");
            return false;
        }
        if(bank.getMembers().contains(target)){
            nPlayer.getPlayer().sendMessage("player already member of bank");
            return false;
        }
        target.addBank(bank);
        bank.addMember(target.getPlayer().getUniqueId().toString());
        nPlayer.getPlayer().sendMessage("successfully added player!");
        return true;
    }
    boolean bankInfo(NarwhalPlayer nPlayer, String[] args){
        if(args.length < 2){
            nPlayer.getPlayer().sendMessage("Insufficient arguments proper usage is /bank info [bank name]");
            return false;
        }
        Bank bank = Bank.getBankFromName(args[1]);
        if(bank == null){
            nPlayer.getPlayer().sendMessage("Specified bank not found!");
            return false;
        }
        nPlayer.getPlayer().sendMessage(Bukkit.getOfflinePlayer(UUID.fromString(bank.getOwner())).getName()+" owns "+bank.getName());
        nPlayer.getPlayer().sendMessage(bank.getName() + " has "+bank.getMoney() + " in total");
        nPlayer.getPlayer().sendMessage(bank.getName()+" has "+bank.getMembers().size()+" members");
        nPlayer.getPlayer().sendMessage(bank.getName()+" has "+bank.getChests().size()+" chests");
        return true;
    }
    boolean deposit(NarwhalPlayer nPlayer, String[] args){
        if(args.length < 2){
            nPlayer.getPlayer().sendMessage("insufficient arguments proper usage is /bank deposit [amount]");
            return false;
        }

        Block block = nPlayer.getPlayer().getTargetBlock(null, 5);
        Chest chest = (Chest) block.getState();
        if(chest == null){
            nPlayer.getPlayer().sendMessage("player must be targeting chest to use this command");
            return false;
        }

        Bank bank = Bank.getBankFromChest(chest.getLocation());
        if(bank == null){
            nPlayer.getPlayer().sendMessage("chest does not belong to any bank");
            return false;
        }

        if(!bank.getMembers().contains(nPlayer.getPlayer().getUniqueId().toString())){
            nPlayer.getPlayer().sendMessage("you are not a member of the bank that owns this chest");
            return false;
        }

        try{
            Integer amount = Integer.parseInt(args[1]);
            if(amount>nPlayer.getMoney()){
                nPlayer.getPlayer().sendMessage("player does not have enough money to deposit that amount");
                return false;
            }
            bank.addMoney(nPlayer.getPlayer().getUniqueId().toString(), amount);
            nPlayer.removeBills(amount);
        }catch (NumberFormatException e){
            nPlayer.getPlayer().sendMessage("amount must be whole number");
            return false;
        }
        nPlayer.getPlayer().sendMessage("successfully deposited money");
        return true;
    }
    boolean withdraw(NarwhalPlayer nPlayer, String[] args){
        if(args.length < 2){
            nPlayer.getPlayer().sendMessage("insufficient arguments proper usage is /bank deposit [amount]");
            return false;
        }

        Block block = nPlayer.getPlayer().getTargetBlock(null, 5);
        Chest chest = (Chest) block.getState();
        if(chest == null){
            nPlayer.getPlayer().sendMessage("player must be targeting chest to use this command");
            return false;
        }

        Bank bank = Bank.getBankFromChest(chest.getLocation());
        if(bank == null){
            nPlayer.getPlayer().sendMessage("chest does not belong to any bank");
            return false;
        }

        if(!bank.getMembers().contains(nPlayer.getPlayer().getUniqueId().toString())){
            nPlayer.getPlayer().sendMessage("you are not a member of the bank that owns this chest");
            return false;
        }

        try{
            Integer amount = Integer.parseInt(args[1]);
            if(amount>bank.getPlayerWealth(nPlayer.getPlayer().getUniqueId().toString())){
                nPlayer.getPlayer().sendMessage("player does not have enough money to deposit that amount");
                return false;
            }
            bank.removeMoney(nPlayer.getPlayer().getUniqueId().toString(), amount);
            nPlayer.addBills(amount);
        }catch (NumberFormatException e){
            nPlayer.getPlayer().sendMessage("amount must be whole number");
            return false;
        }
        nPlayer.getPlayer().sendMessage("successfully deposited money");
        return true;
    }
    boolean getBalance(NarwhalPlayer nPlayer, String[] args){
        if(args.length<2){
            nPlayer.getPlayer().sendMessage("insufficient arguments proper usage is /bank balance [bank name]");
            return false;
        }
        Bank bank = Bank.getBankFromName(args[1]);
        if(bank==null){
            nPlayer.getPlayer().sendMessage("bank name not recognized");
            return false;
        }
        NarwhalPlayer player = null;
        if(args.length>2) {
            player = NarwhalPlayer.convertPlayer(Bukkit.getPlayer(args[2]));
        }
        else{
            player = nPlayer;
        }
        nPlayer.getPlayer().sendMessage(player.getPlayer().getDisplayName()+"'s bank balance is "+bank.getPlayerWealth(nPlayer.getPlayer().getUniqueId().toString()).toString());
        return true;
    }

    boolean createShop(NarwhalPlayer nPlayer, String[] args) {
        if(args.length < 4){
            nPlayer.getPlayer().sendMessage("insufficient arguments! usage is /shop create [shop type(buy or sell)] [price] [max items](you must be holding the object you intend to sell)");
            return false;
        }

        int price;
        try{
           price = Integer.parseInt(args[2]);
        }catch(NumberFormatException e){
            nPlayer.getPlayer().sendMessage("amount must be a whole number! usage is /shop create [shop type(buy or sell)] [price] [max items](you must be holding the object you intend to sell)");
            return false;
        }

        Material item_type = nPlayer.getPlayer().getInventory().getItemInMainHand().getType();
        if(item_type==null){
            nPlayer.getPlayer().sendMessage("no item in hand! usage is /shop create [shop type(buy or sell)] [price] [max items](you must be holding the object you intend to sell)");
            return false;
        }

        int max;
        try{
            max = Integer.parseInt(args[3]);
        }catch(NumberFormatException e){
            nPlayer.getPlayer().sendMessage("max must be whole number! usage is /shop create [shop type(buy or sell)] [price] [max items](you must be holding the object you intend to sell)");
            return false;
        }

        Block chest = nPlayer.getPlayer().getTargetBlock(null, 5);
        if(chest.getType()!=Material.CHEST){
            nPlayer.getPlayer().sendMessage("no chest found(please look at a chest)! usage is /shop create [shop type(buy or sell)] [price] [max items](you must be holding the object you intend to sell)");
            return false;
        }

        ChestShop shop = null;

        switch(args[1]) {
            case "buy":
                shop = new BuyShop(chest, nPlayer.getPlayer().getUniqueId().toString(), item_type, price, max, NarwhalTowns.getShopData());
                break;
            case "sell":
                shop = new SellShop(chest, nPlayer.getPlayer().getUniqueId().toString(), item_type, price, max, NarwhalTowns.getShopData());
                break;
            default:
                nPlayer.getPlayer().sendMessage("unrecognized shop type");
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
        } if (label.equalsIgnoreCase("bank")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Console cant use banks");
                return false;
            }

            NarwhalPlayer nPlayer = NarwhalPlayer.convertPlayer(((Player) sender).getPlayer());
            if (nPlayer == null) return false;
            if (args.length == 0) {
                //RUN HELP COMMAND
                return false;
            }
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
                        sender.sendMessage("usage /bank addchest [Bank Name]");
                        return false;
                    }
                    Bank ownedBank = nPlayer.getOwnedBanksFromString(args[1]);
                    if(ownedBank==null)return false;
                    Block block = ((Player) sender).getTargetBlock(null, 5);
                    if(!nPlayer.CanInteractWith(block,TownPerms.createBankChest))return false;
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

        nPlayer.getPlayer().sendMessage("created shop!");
        return true;
        return false;
    }
    //boolean buy(NarwhalPlayer nPlayer, String[] args) {}
    //boolean sell(NarwhalPlayer nPlayer, String[] args) {}


}