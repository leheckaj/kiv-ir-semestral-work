package tfidf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TfIdfClass {

    public static Map<String, Double> getTfIdf(Map<String, Integer> freqMap, List<Map<String,Integer>> cetnosti){
        Map<String, Double> mapa = new HashMap<>();
        for (String key : freqMap.keySet()) {
            //System.out.println(key + ":" + freqMap.get(key));
            double cetnostVDokumentu = freqMap.get(key);
            double tf = (1+Math.log10(cetnostVDokumentu));

            double pocetDokumentuVKolekci = (double) Constants.DOCUMENT_COUNT;
            // Počet dokument v kterých se term nachází
            double pocetDokumentuSTermem = TextProcessor.getTermInDocumentsCount(cetnosti, key);
            double idf = Math.log10(pocetDokumentuVKolekci/pocetDokumentuSTermem);

            double tfidf = tf*idf;

            mapa.put(key, tfidf);
        }
        return mapa;
    }
}
