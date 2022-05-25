package narwhal.narwhaltowns;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.TextComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TownCommands implements CommandExecutor {
    public TownCommands(NarwhalTowns plugin){
        this.plugin = plugin;
    }
    NarwhalTowns plugin;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("town")||label.equalsIgnoreCase("t")||label.equalsIgnoreCase("narwhaltown")){
            if(!(sender instanceof Player)){
                sender.sendMessage("Console cannot do /town");
                return false;
            }
            NarwhalPlayer player = NarwhalPlayer.convertPlayer(((Player)sender));
            if(player==null)return false;
            switch (args[0].toLowerCase()){
                case "create":
                    return create(player,args);
                case "info":
                    return info(player,args);
                case "claim":
                    return claim(player,args);
                case "leave":
                    return leave(player,args);
                case "invite":
                    return invite(player,args);
                case "disband":
                    return disband(player,args);
                case "accept":
                    return accept(player,args);
                default:
                    return false;
            }
        }
        return false;
    }
    boolean create(NarwhalPlayer player,String[] args){

        if(args.length<2) {
            player.getPlayer().sendMessage(ChatColor.RED+"Usage: /town create [town name]");
            return false;
        }
        if(player.getTerritory("town") != null) {
            player.getPlayer().sendMessage(ChatColor.RED+"Cannot create town as you already are a member of a town, try: /town leave [town name] or /town disband");
            return false;
        }
        if(args[1].length()>20 || args[1].length()<4) {
            player.getPlayer().sendMessage(ChatColor.RED+"Town names must be from 4-20 characters in length");
            return false;
        }
        if (Town.getTerritoryFromName(args[1]) != null) {
            player.getPlayer().sendMessage(ChatColor.RED+"Town or Nation of that name already exists");
            return false;
        }
        int x = player.getPlayer().getLocation().getChunk().getX();
        int y = player.getPlayer().getLocation().getChunk().getZ();
        if(Chunk.getChunkFromCoords(x,y)!=null){
            player.getPlayer().sendMessage(ChatColor.RED+"Cannot create town in claimed land");
            return false;
        }
        Town town = new Town(args[1],plugin.getTownData());
        town.addMember(player);
        town.addChunk(x,y);
        player.getPlayer().sendMessage(ChatColor.GREEN + "Created Town: " + town.getName());
        //TEMP
        player.addPerm(Perms.disband);
        player.addPerm(Perms.claim);
        return true;

    }

    boolean info(NarwhalPlayer player,String[] args){
        Town town;
        if(args.length==1) {
            town = (Town) player.getTerritory("town");
            if (town == null) {
                player.getPlayer().sendMessage(ChatColor.RED + "Usage: /town info [town]");
                return false;
            }

        }else{
            town = Town.getTownFromName(args[1]);
            if(town == null){
                player.getPlayer().sendMessage(ChatColor.RED + args[1]+" does not exist, make sure your spelling the town name correctly");
                return false;
            }
            StringBuilder str = new StringBuilder(ChatColor.GREEN+""+ChatColor.BOLD);
            str.append(town.getName());
            str.append(ChatColor.RESET + "" + ChatColor.GOLD);
            str.append("\n---------------\n");
            str.append("members(");
            str.append(town.getOnlineMembers().length);
            str.append(") online(");
            player.getPlayer().sendMessage();

        }

        return true;

    }

    boolean claim(NarwhalPlayer player, String[] args){
        if(!player.hasPerm(Perms.claim)) {
            player.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use that command");
            return false;
        }
        Town town = (Town) player.getTerritory("town");
        if(town == null){

            player.getPlayer().sendMessage(ChatColor.RED+"You are not apart of any town,try /town create [town name]");
            return false;
        }
        int loops = 1;
        if(args.length>1){
            try{ loops = Integer.parseUnsignedInt(args[1]);}
            catch (NumberFormatException e){
            }
        }
        int claimedChunks = 0;
        for (int i = -(loops/2);i<Math.ceil(loops/2D);i++) {
            int x = player.getPlayer().getLocation().getChunk().getX() + i;
            for (int j =-(loops/2);j<Math.ceil(loops/2D);j++) {
                int y = player.getPlayer().getLocation().getChunk().getZ() + j;
                if(town.addChunk(x, y)) {
                    player.getPlayer().sendMessage(ChatColor.GREEN + "Claimed Chunk" + " for " + town.getName());
                    claimedChunks++;
                }
                else player.getPlayer().sendMessage(ChatColor.RED + "Couldn't claimed chunk as someone already owns that chunk");
            }
        }
        Bukkit.getLogger().info(player.getPlayer().getDisplayName() + " claimed "+(claimedChunks)+" chunks for "+town.getName());
        return true;

        }

    List<NarwhalPlayer> canLeave = new ArrayList<NarwhalPlayer>();
    boolean leave(NarwhalPlayer player, String[] args){
        Town town = (Town) player.getTerritory("town");
        if (town==null){
            player.getPlayer().sendMessage(ChatColor.RED + "You cannot leave as you are not apart of any town");
            return false;
        }
        if(town.getOnlineMembers().length==1){
            player.getPlayer().sendMessage(ChatColor.RED + "Cannot leave town as you are the only member try /town disband");
            return false;
        }
        if(canLeave.contains(player)) {
            town.removeMember(player);
            player.getPlayer().sendMessage(ChatColor.RED + "You have left " + town.getName());
            canLeave.remove(player);
            return true;
        }
        canLeave.add(player);
        player.getPlayer().sendMessage(ChatColor.RED + "Run command again to confirm you want to leave " + town.getName());
        new BukkitRunnable() {
            public void run() {
                canLeave.remove(player);
            }
        }.runTaskLater(plugin, 100);
        return true;



    }

    List<NarwhalPlayer> canDisband = new ArrayList<NarwhalPlayer>();
    boolean disband(NarwhalPlayer player, String[] args){
        Town town = (Town) player.getTerritory("town");
        if (town == null) {
            player.getPlayer().sendMessage(ChatColor.RED + "You cannot disband as you are not apart of any town");
        }
        if(!player.hasPerm(Perms.disband)) {
            player.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use that command");
        }
        if (canDisband.contains(player)) {
            town.destroy();
            player.getPlayer().sendMessage(ChatColor.RED + "You have disbanded " + town.getName());
            canDisband.remove(player);
            return true;
        }
        canDisband.add(player);
        player.getPlayer().sendMessage(ChatColor.RED + "Run command again to confirm you want to disband " + town.getName());
        new BukkitRunnable() {
            public void run() {
                canDisband.remove(player);
            }
        }.runTaskLater(plugin, 100);
        return true;
    }

    HashMap<NarwhalPlayer,List<Town>> invites = new HashMap<>();
    boolean invite(NarwhalPlayer player, String[] args){
        Town town = (Town) player.getTerritory("town");
        if (town==null) {
            player.getPlayer().sendMessage(ChatColor.RED + "You cannot invite as you are not apart of any town");
            return false;
        }
        if(!player.hasPerm(Perms.invite)){
            player.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use that command");
            return false;
        }
        Player receivingPlayer = Bukkit.getPlayer(args[1]);
        if(receivingPlayer == null){
            player.getPlayer().sendMessage(ChatColor.RED + "Cannot invite "+args[1]+" as they aren't online");
            return false;
        }
        NarwhalPlayer receiver = NarwhalPlayer.convertPlayer(receivingPlayer);
        if(!invites.containsKey(receiver)) invites.put(receiver, new ArrayList<Town>());
        if(!invites.get(receiver).contains(town)){
            player.getPlayer().sendMessage(ChatColor.RED + "Your town already has a pending invite to that player");
            return false;
        }
        invites.get(receiver).add(town);
        TextComponent message = new TextComponent("You have been invited to "+town.getName()+" by "+player+"\nclick here to accept");
        message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        message.setBold(true);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/town accept "+town.getName()));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE+""+ net.md_5.bungee.api.ChatColor.ITALIC +"Click to join "+town.getName())));
        receivingPlayer.spigot().sendMessage(message);
        player.getPlayer().sendMessage(ChatColor.GREEN +"You have invited "+receivingPlayer.getName() + " to join "+town.getName());
        new BukkitRunnable() {
            public void run() {
                invites.get(receiver).remove(town);
                if(invites.get(receiver).size()==0)invites.remove(receiver);
                player.getPlayer().sendMessage(ChatColor.RED + "Invite to "+receivingPlayer.getName() + " has expired");
                if(receivingPlayer.isOnline())
                    receivingPlayer.sendMessage(ChatColor.RED + "Invite from "+player.getPlayer().getName() + " has expired");
            }
        }.runTaskLater(plugin, 20*25);
        return true;
    }

    boolean accept(NarwhalPlayer player, String[] args){
        if (player.getTerritory("town")!=null) {
            player.getPlayer().sendMessage(ChatColor.RED + "You cannot accept invite as you are already in a town, try /town leave or /town disband");
            return false;
        }
        Town town;
        if(args.length==1){
            if(invites.get(player).size()==0){
                player.getPlayer().sendMessage(ChatColor.RED + "You do not have any pending invites to accept");
                return false;
            }
            town = invites.get(player).get(invites.get(player).size()-1);

        }
        else {
            town = Town.getTownFromName(args[1]);
            if(town == null){
                player.getPlayer().sendMessage(ChatColor.RED + args[1]+" does not exist, make sure your spelling the town name correctly");
                return false;
            }
            if(!invites.get(player).contains(town)){
                player.getPlayer().sendMessage(ChatColor.RED + "You do not have any pending invites from "+town.getName());
                return false;
            }

        }
        town.addMember(player);
        invites.get(player).remove(town);
        player.getPlayer().sendMessage(ChatColor.GREEN + "You have joined "+town.getName());
        //ADD PERM SETUP
        return true;
    }


}
