package narwhal.narwhaltowns;

import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.*;

public class Bank {

    public Bank(String _name, NarwhalPlayer _owner) {
        name = _name;
        owner = _owner;
        sponsored = false;
        Banks.add(this);
    }
    public Bank(String _name, NarwhalPlayer _owner, Territory _sponsor) {
        name = _name;
        owner = _owner;
        sponsored = true;
        sponsor = _sponsor;
        Banks.add(this);
    }

    public void setName(String _name)
    {
        name = _name;
    }
    public String getName(){return name;}

    public void addMember(String memberName)
    {
        playersWealth.put(memberName, 0);
    }
    public void removeMember(String memberName)
    {
        playersWealth.remove(memberName);
    }
    public void addMoney(String memberName, int amount){

        playersWealth.put(memberName, playersWealth.get(memberName)+amount);
    }
    public void removeMoney(String memberName, int amount) {
        if(playersWealth.get(memberName) < amount)
        {
            Bukkit.getLogger().info("Bank accounts cannot have negative numbers");
            return;
        }
        playersWealth.put(memberName, playersWealth.get(memberName)-amount);
    }
    public void setMoney(String memberName, int amount) {

        playersWealth.put(memberName, amount);
    }
    public NarwhalPlayer getOwner(){
        return owner;
    }

    public void addChest(Chest chest){
        chests.add(chest);
    }
    public void removeChest(Chest chest){
        chests.remove(chest);
    }

    public void destroy() {
        Banks.remove(this);
        name = null;
        owner = null;
        playersWealth = null;
        chests = null;
        sponsored = false;
        sponsor = null;
        //data.getConfig().set(getName(),null);
    }


    private String name;
    private NarwhalPlayer owner;
    private HashMap<String, Integer> playersWealth = new HashMap<String, Integer>();
    private List<Chest> chests = new ArrayList<>();

    private boolean sponsored;
    private Territory sponsor;

    private List<Bank> Banks  = new ArrayList<>();

}