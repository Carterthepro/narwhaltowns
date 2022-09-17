package narwhal.narwhaltowns;


import narwhal.narwhaltowns.Files.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

public abstract class Bank {
     private DataManager data;

     public Bank(String _name, String _owner, DataManager _data) {
          name = _name;
          owner = _owner;
          money_pool = 0;
          sponsored = false;
          Banks.add(this);
          data = _data;
     }
     public Bank(String _name, String _owner, DataManager _data, Territory _sponsor) {
          name = _name;
          owner = _owner;
          money_pool = 0;
          sponsored = true;
          sponsor = _sponsor;
          Banks.add(this);
          data = _data;
     }

     public void calculatePoolSize(){
          for(String member : members){
               money_pool += playersWealth.get(member);
          }
     }

     public void save(){
          //TODO save bank owner territory
          Bukkit.getLogger().info("Saving bank: "+name);
          List<String> members = new ArrayList<>();
          List<Integer> players_wealth = new ArrayList<>();
          for(String member : getMembers()){
               members.add(member);
               players_wealth.add(getPlayerWealth(member));
          }
          data.getConfig().set(getName() +".owner", owner);
          if(sponsor!=null) {
               data.getConfig().set(getName() + ".sponsor", sponsor.getName());
          }
          data.getConfig().set(getName() +".members", members);
          data.getConfig().set(getName() +".wealth", players_wealth);

          List<Location> chest_locations = new ArrayList<>();
          for(Location chest : getChests()){
               chest_locations.add(chest);
          }
          data.getConfig().set(getName() +".chests", chest_locations);
          data.saveConfig();
     }
     public void setName(String _name)
     {
          name = _name;
     }
     public String getName(){return name;}

     public void addMember(String  member) {
          playersWealth.put(member, 0);
          members.add(member);
     }
     public void removeMember(String member) {
          playersWealth.remove(member);
          members.add(member);
     }
     public void addMoney(String member, int amount){

          playersWealth.put(member, playersWealth.get(member)+amount);
          money_pool += amount;
     }
     public void removeMoney(String member, int amount) {
          if(playersWealth.get(member) < amount)
          {
               Bukkit.getLogger().info("Bank accounts cannot have negative numbers");
               return;
          }
          playersWealth.put(member, playersWealth.get(member)-amount);
          money_pool -= amount;
     }
     public void setMoney(String member, int amount) {

          playersWealth.put(member, amount);
     }
     public String getOwner(){
          return owner;
     }

     public void addChest(Location chest){
          chests.add(chest);
     }
     public void removeChest(Location chest){
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

     public List<String> getMembers(){
          return members;
     }

     public List<Location> getChests(){
          return chests;
     }

     public Integer getPlayerWealth(String member){
          return playersWealth.get(member);
     }

     private String name;
     private String owner;
     private HashMap<String, Integer> playersWealth = new HashMap<String, Integer>();

     private List<String> members = new ArrayList<>();
     private List<Location> chests = new ArrayList<>();

     private boolean sponsored;
     private Territory sponsor;

     private Integer money_pool;

     public Integer getMoney(){
          return money_pool;
     }

     private static List<Bank> Banks  = new ArrayList<>();

     public static Bank getBankFromName(String name){
          for (Bank bank : Banks) {
               if(bank.getName().equalsIgnoreCase(name))return bank;
          }
          return null;
     }

     public static Bank getBankFromChest(Location inventory){
          for(Bank bank : Banks){
               if(bank.getChests().contains(inventory)){
                    return bank;
               }
          }
          return null;
     }

     public static List<Bank> getBanks(){
          return Banks;
     }

     public double getInterest(){
          return 0.02;
     }

     public void addMoneyToPool(Integer amount){
          money_pool+=amount;
     }

     //TODO add fee
     public void wireTransfer(int amount, String member, String recipient, Bank recipient_bank){
          removeMoney(member, amount);
          recipient_bank.addMoney(recipient, amount);
     }

}
