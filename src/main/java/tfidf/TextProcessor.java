package tfidf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class TextProcessor {


    public static Map<String, Integer> getTermsCount(String text) {
        //String text = "Ann while Bob had had had had had had had had had had had a better effect on on the teacher";
        Map<String, Integer> freqMap = new HashMap<>();
        asList(text.split(" ")).forEach(s -> {
            if (freqMap.containsKey(s)) {
                Integer count = freqMap.get(s);
                freqMap.put(s, count + 1);
            } else {
                freqMap.put(s, 1);
            }
        });
        return freqMap;
    }

    /**
     * Spočte kolikrát se term nachází JEN v dokumentech
     * @param maps
     * @param key
     * @return
     */
    public static int getTermInDocumentsCount(List<Map<String,Integer>> maps, String key){
        int counter = 0;
        for(int idx=0; idx<maps.size(); idx++){
            Map<String,Integer> map = maps.get(idx);
            if(map.containsKey(key))
                counter++;
        }
        return counter;
    }

}
