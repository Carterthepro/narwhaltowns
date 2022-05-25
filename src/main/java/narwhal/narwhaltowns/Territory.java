package narwhal.narwhaltowns;

import narwhal.narwhaltowns.Files.DataManager;

import java.util.ArrayList;
import java.util.List;

public abstract class Territory {
    public Territory(String name,String type, DataManager data){
        this.name = name;
        this.type = type;
        territories.add(this);
        this.data=data;
    }

    private List<Chunk> chunks = new ArrayList<Chunk>();
    private List<NarwhalPlayer> onlineMembers = new ArrayList<NarwhalPlayer>();
    private List<String> members = new ArrayList<>();
    private String name;
    protected DataManager data;
    private String type;
    private boolean deleted = false;
    protected static List<Territory> territories = new ArrayList<>();

    public static Territory getTerritoryFromName(String name){
        for(Territory territory: territories){
            if(territory.getName().equalsIgnoreCase(name))return territory;
        }
        return null;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public boolean addChunk(int x, int y){
        for (Chunk chunk:Chunk.getClaimedChunks()) {
            if(chunk.getX() == x && chunk.getY() == y)return false;
        }
        chunks.add(new Chunk(x,y,this));
        return true;
    }
    public void removeChunk(int x, int y){
        chunks.removeIf(chunk -> chunk.getX() == x && chunk.getY() == y);
    }
    public void removeChunk(Chunk chunk){
        chunks.remove(chunk);
    }
    public Chunk[] getChunks(){
        return chunks.toArray(new Chunk[0]);
    }
    public boolean isInTerritory(Chunk chunk){
        return chunks.contains(chunk);
    }
    public boolean isInTerritory(int x, int y){
        for (Chunk chunk : chunks) {
            if(chunk.getX() == x&&chunk.getY() == y)return true;
        }
        return false;
    }
    public void destroy(){
        for (Chunk chunk:chunks) {
            chunk.unClaim();
        }
        for (NarwhalPlayer player: onlineMembers){
            player.removeTerritory(this);
        }
        chunks = null;
        onlineMembers = null;
        deleted = true;
    }
    public boolean hasBeenDeleted(){
        return deleted;
    }
    public void addMember(NarwhalPlayer player){
        onlineMembers.add(player);
        if(!members.contains(player.getPlayer().getUniqueId().toString()))
            members.add(player.getPlayer().getUniqueId().toString());
        player.addTerritory(this);
    }
    public void addMember(String uuid){
        if(!members.contains(uuid))
            members.add(uuid);
    }

    public void removeMember(NarwhalPlayer player){
        onlineMembers.remove(player);
        members.remove(player);
        player.removeTerritory(this);
    }
    public NarwhalPlayer[] getOnlineMembers(){
        return onlineMembers.toArray(new NarwhalPlayer[0]);
    }
    public String[] getMembers(){
        return members.toArray(new String[0]);
    }

    public abstract boolean save();
    public String getType(){
        return type;
    }
}
