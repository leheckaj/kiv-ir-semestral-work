package tfidf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static List<Map<String,Double>> listValues(List<Map<String,Double>> docTermTfIdf, Map<Integer, Double> normalizacniDelka){
        //Přenormování hodnot
        int docId = 0;
        for(Map<String,Double> document:docTermTfIdf){
            for(String key : document.keySet()){
                document.put(key,document.get(key));
                //document.put(key, document.get(key)/normalizacniDelka.get(docId));
            }
            docId++;
        }
        return docTermTfIdf;
    }


    public static Map<Integer, Double> getNormalizedLengths(List<Map<String,Double>> docTermTfIdf){
        // Výpočet normalizační délky pro jednotlivé dokumenty
        int docId = 0;
        Map<Integer, Double> normalizacniDelka = new HashMap<>();

        for(Map<String,Double> document:docTermTfIdf){
            double sum = 0;
            for(String key : document.keySet()){
                sum += document.get(key)*document.get(key);
            }
            normalizacniDelka.put(docId, Math.pow(sum,0.5));
            docId++;
        }
        return normalizacniDelka;
    }

    public static void printDocumentsTfIdf(List<Map<String,Double>> docTfIdf){
        int docId = 0;
        for(Map<String,Double> mapaa:docTfIdf){
            for(String key : mapaa.keySet()){
                System.out.println(docId + "-> " + key + ":" + mapaa.get(key));
            }
            docId++;
        }
    }

    public static void printCetnosti(List<Map<String,Integer>> docTfIdf){
        int docId = 0;
        for(Map<String,Integer> mapaa:docTfIdf){
            for(String key : mapaa.keySet()){
                System.out.println(docId + "-> " + key + ":" + mapaa.get(key));
            }
            docId++;
        }
    }

    /**
     * Spočte všechny vzdálenosti mezi dokumenty a query
     * @param query Mapa query
     * @param cosine Pole normalizovaných cosinových hodnot
     */
    public static Map<Integer,Double> computeCosDistance(List<Map<String,Double>> cosine, Map<String,Double> query,
                                                         Map<Integer, Double> docSquared, Map<Integer, Double> querySquared){
        // Vzdálenosti mezi zadaným Dokumentem a Query
        Map<Integer,Double> map = new HashMap<>();

        CosineDistance cos = new CosineDistance();
        for(int idx=0; idx<(cosine.size()); idx++){
            double sum = cos.computeCosineDistance(cosine.get(idx), query);
            double cosval = sum/(docSquared.get(idx)*querySquared.get(0));
            map.put(idx+1,cosval);
        }
        return map;
    }

    public static List<Map<String,Double>> computeAllTfIdfs(List<Map<String,Integer>> cetnosti, List<Map<String,Integer>> cet){
        List<Map<String,Double>> docTfIdf = new ArrayList();
        for(Map<String, Integer> freqMap:cetnosti){
            docTfIdf.add(TfIdfClass.getTfIdf(freqMap, cet));
        }
        return docTfIdf;
    }

    /**
     * Vytiskne cosinové vzdálensti
     * @param cosineDistance pole s cosinovými vzdálenostmi
     */
    public static void printCosineDistance(double[] cosineDistance){
        for(int i=0; i<cosineDistance.length; i++){
            System.out.println("cosineDistance(Query, Doc" + (i+1) + ") = " + cosineDistance[i]);
        }
    }
}
