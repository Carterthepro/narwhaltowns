package narwhal.narwhaltowns;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    //ADD CHAT FILTERING
    @EventHandler
    public void OnPlayerChat(AsyncPlayerChatEvent e){
        NarwhalPlayer player = NarwhalPlayer.convertPlayer(e.getPlayer());
        Town town = (Town) player.getTerritory("town");
        StringBuilder str = new StringBuilder();
        if(town!=null) {
            str.append(town.getName());
            str.append(' ');
        }
        if(!player.getTitle().isEmpty()) {
            str.append(player.getTitle());
            str.append(' ');
        }
        str.append('<');
        str.append(player.getPlayer().getDisplayName());
        str.append("> ");
        str.append(e.getMessage());
        e.setFormat(str.toString());
    }
}
