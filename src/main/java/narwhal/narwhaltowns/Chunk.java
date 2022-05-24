package narwhal.narwhaltowns;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    public Chunk(int x,int y,Territory owner){
        this.x = x;
        this.y = y;
        addOwner(owner);
        for (Chunk chunk:claimedChunks) {
            if(chunk.getX() == x &&chunk.getY()==y){
                claimedChunks.remove(chunk);
                Bukkit.getLogger().warning("overid chunk x: "+x+" y: " +y);
            }
        }
        claimedChunks.add(this);
    }
    public Chunk(int x,int y,Territory[] owners){
        this.x = x;
        this.y = y;
        for (Territory owner:owners) {
            addOwner(owner);
        }
    }
    private static List<Chunk> claimedChunks = new ArrayList<Chunk>();
    public static List<Chunk> getClaimedChunks(){
        return claimedChunks;
    }
    public static Chunk getChunkFromCoords(int x,int y){
        for (Chunk chunk:claimedChunks) {
            if(chunk.getY() == y && chunk.getX() == x){
                return chunk;
            }
        }
        return null;
    }
    private int x;
    private int y;
    public int getX(){return x;}
    public int getY(){return y;}
    private List<Territory> owners = new ArrayList<Territory>();
    public void addOwner(Territory owner){
        for (Territory territory:owners) {
            if(territory.getType() == owner.getType()){
                Bukkit.getLogger().warning("Cannot add territory as it is already an owner of the chunk");
                return;
            }
        }
        owners.add(owner);
    }
    public void setOwner(Territory owner){
        int i = 0;
        for (Territory territory:owners) {
            if(territory.getType() == owner.getType()){
                owners.set(i,owner);
                return;
            }
            i++;
        }
    }
    public Territory getOwner(String type){
        for (Territory territory:owners) {
            if(territory.getType() == type){
                return territory;
            }
        }
        return null;
    }
    public void unClaim(){
        claimedChunks.remove(this);
        owners = null;
    }

}
