package narwhal.narwhaltowns;


public class TownPermPresets {
    public static final TownPerms[] Mayor = TownPerms.values();
    public static final TownPerms[] Citizen = {TownPerms.breakAndPlace};
    public static final TownPerms[] Manager = {TownPerms.breakAndPlace,TownPerms.invite,TownPerms.kick,TownPerms.claim,TownPerms.createShop,TownPerms.createPlot,TownPerms.createBank,TownPerms.createBankChest};
}

