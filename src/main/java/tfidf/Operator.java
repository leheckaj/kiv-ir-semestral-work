package tfidf;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Operator {
    static int best = -1;
    public static void setBest(Map<Integer, Double> cos){
        Map<Integer,Double> cos2 = cos.entrySet().stream().sorted((e1,e2)->
                        e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        cos2.forEach((key,val)-> {
            best = key;
            //System.out.println(key + " = " + val.toString());
        });
    }

    public static void process(String file) throws FileNotFoundException {
        // Načtení malého výřezu z dat pro semestrální práci
        Reader fReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(new BufferedReader(fReader));

        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        Analyzer analyzer = new StandardAnalyzer();
        analyzer = new CzechAnalyzer();

        // Creating an empty ArrayList of string type
        boolean firstLine = true;
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<CrawledPage> el = new ArrayList<CrawledPage>();
        while (sc.hasNextLine()) {
            if(firstLine) {
                firstLine = false;
                continue;
            }
            String[] val = sc.nextLine().split(";");
            //addDoc(w, val[1], val[2]);
            CrawledPage cp = new CrawledPage(val[0], val[1], val[2]);
            //al.add(cp.getTitle());
            al.add(cp.getBody());
            //addDoc(w, cp.getTitle(), cp.getBody());
            el.add(cp);
        }

        String[] docs = new String[al.size()];
        for (int i = 0; i < al.size(); i++) {
            docs[i] = al.get(i);
        }
        List<Map<String,Integer>> cetnosti = new ArrayList<Map<String,Integer>>();

        String query1 = "Nette Cache";

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



        // Druhé mocniny dokumentů a dotazu ====> jmenovatelé v cosinové vzdálenosti
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

        setBest(cos);
        System.out.println();

        System.out.println("Query: " + query1);
        System.out.println("Nejrelevantnější je dokument: " + el.get(best-1).getUrl());

    }

}
