package narwhal.narwhaltowns;


import narwhal.narwhaltowns.Files.DataManager;

public class Nation extends Territory{

    public Nation(String name, DataManager data) {

        super(name,"nation",data);
    }

    @Override
    public boolean save() {
        return false;
    }

}
