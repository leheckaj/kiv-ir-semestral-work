package tfidf;

import java.util.Map;

public class CosineDistance {
    public CosineDistance(){

    }

    /**
     * Spočte cosinovou vzdálenost pro zadané Normalizovnaé hodnoty,
     * předpokládá se, že hodnoty pro dotaz se v mapě nachází na poslední pozici
     * @param doc  HashMapa dokumentu
     * @param query HashMapa query
     */
    public double computeCosineDistance(Map<String,Double> doc, Map<String,Double> query){
        double sum = 0;
        for(String key : query.keySet()){
            if(query.containsKey(key) && doc.containsKey(key)) {
                Double queryVal = query.get(key);
                Double docVal = doc.get(key);
                sum += queryVal * docVal;
            }
        }
        return sum;
    }
}
