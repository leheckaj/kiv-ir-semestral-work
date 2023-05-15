package tfidf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Excercise {

    public void excercise1() {
        List<Map<String,Integer>> cetnosti = new ArrayList<Map<String,Integer>>();

        String doc1 = "Plzeň je krásné město a je to krásné místo";
        String doc2 = "Ostrava je ošklivé místo";
        String doc3 = "Praha je také krásné město Plzeň je hezčí";
        String query1 = "krásné město";

        String[] docs = {doc1, doc2, doc3};



        // Výpočet četností termů v dokumentech - dotazu
        for(String doc:docs){
            Map<String, Integer> freqMap = TextProcessor.getTermsCount(doc);
            cetnosti.add(freqMap);
        }
        Map<String, Integer> queryFrequency = TextProcessor.getTermsCount(query1);



        // Výpočet Tf-Idf pro DOC x TERM, QUERY x TERM
        List<Map<String,Double>> docTfIdf = Utils.computeAllTfIdfs(cetnosti, cetnosti);

        ArrayList<Map<String,Integer>> in = new ArrayList<Map<String,Integer>>();
        in.add(queryFrequency);
        List<Map<String,Double>> queryTfIdf = Utils.computeAllTfIdfs(in, cetnosti);


        // Druhé mocniny dokumentů a dotazu - jmenovatele v cosinové vzdálenosti
        Map<Integer, Double> docSquared = Utils.getNormalizedLengths(docTfIdf);
        Map<Integer, Double> querySquared = Utils.getNormalizedLengths(queryTfIdf);


        // Převedeme na list
        List<Map<String,Double>> docCosine = Utils.listValues(docTfIdf, docSquared);
        List<Map<String,Double>> queryCosine = Utils.listValues(queryTfIdf, querySquared);
        Map<String,Double> queryCos = queryCosine.get(0);

        // Cosinová vzdálenost Doc-Query
        System.out.println("Výpočet cosinové vzdálenosti");
        System.out.println("------------------------------");
        System.out.println("dokument = TfIdf");
        Map<Integer,Double> cos = Utils.computeCosDistance(docCosine, queryCos, docSquared, querySquared);
        cos.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEach(System.out::println);

        //System.out.println("Seřazené dle relevance");

    }
}
