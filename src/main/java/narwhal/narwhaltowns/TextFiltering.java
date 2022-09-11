package narwhal.narwhaltowns;

import narwhal.narwhaltowns.Files.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TextFiltering {
    private static DataManager wordLists = null;
    //add better filtering methods such as Damerau-Levenshtein
    public static String Filter(String text,FilterLevel filterLevel){
        if(wordLists == null){
            wordLists = new DataManager(NarwhalTowns.getPlugin(),"swears");
        }
        List<String> badWords = new ArrayList<>();
        switch (filterLevel){
            case Easy:
                badWords.addAll(wordLists.getConfig().getStringList("Easy"));
            case Medium:
                badWords.addAll(wordLists.getConfig().getStringList("Medium"));
            case Hard:
                badWords.addAll(wordLists.getConfig().getStringList("Hard"));
                break;
        }
        StringBuilder str = new StringBuilder(text.toLowerCase(Locale.ROOT));
        for (String word : badWords){
            int index = str.indexOf(word);
            if(index!=-1){
                str.replace(index,word.length(),"****");
            }
        }
        return str.toString();
    }
    public static boolean ValidFilter(String text,FilterLevel filterLevel){
        if(wordLists == null){
            wordLists = new DataManager(NarwhalTowns.getPlugin(),"swears");
        }
        List<String> badWords = new ArrayList<>();
        switch (filterLevel){
            case Easy:
                badWords.addAll(wordLists.getConfig().getStringList("Easy"));
            case Medium:
                badWords.addAll(wordLists.getConfig().getStringList("Medium"));
            case Hard:
                badWords.addAll(wordLists.getConfig().getStringList("Hard"));
                break;
        }
        StringBuilder str = new StringBuilder(text.toLowerCase(Locale.ROOT));
        for (String word : badWords){
            int index = str.indexOf(word);
            if(index!=-1){
               return false;
            }
        }
        return true;
    }
    public static enum FilterLevel{
        Easy,
        Medium,
        Hard
    }
}
