package narwhal.narwhaltowns;

import narwhal.narwhaltowns.Files.DataManager;

public class SmallBank extends Bank{
    SmallBank(String _name, String _owner, DataManager _data){
        super(_name, _owner, _data);
    }
    public SmallBank(String _name, String _owner, DataManager _data, Territory _territory) {
        super(_name, _owner, _data, _territory);
    }

    public static void setInterest(double amount){
        interest = amount;
    }

    @Override
    public double getInterest(){
        return interest;
    }

    public static double interest = 0.03;

}