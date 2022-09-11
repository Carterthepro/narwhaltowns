package narwhal.narwhaltowns;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class BankInterestEnactor{
    public void EnactInterest(){
        for(Bank bank : Bank.getBanks()){
            double value = bank.getMoney().doubleValue()*bank.getInterest();
            bank.addMoneyToPool((int) value);
            Bukkit.getLogger().info("enacted interest");
        }
    }
}
