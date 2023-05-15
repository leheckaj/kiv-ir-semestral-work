package utils;

import preprocessing.*;
import tfidf.CrawledPage;
import tfidf.TextProcessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Processor {
    private Preprocessing preprocessing;

    ArrayList<CrawledPage> el = new ArrayList<CrawledPage>();

    List<Map<String,Integer>> cetnosti;

    List<Map<String,Double>> docCosine;

    Map<Integer,Double> docSquared;

    private void createNewInstance() {
        Set<String> stopwords = Utils.readStopWords("stopwords.txt");
        preprocessing = new BasicPreprocessing(
                new CzechStemmerAgressive(), new AdvancedTokenizer(), stopwords, false, true, true
        );
    }

    public void init() throws FileNotFoundException {
        Reader fReader = new InputStreamReader(new FileInputStream(Constants.INPUT_DATA_CSV_FILE), StandardCharsets.UTF_8);
        //Reader fReader = new InputStreamReader(new FileInputStream("output.csv"), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(new BufferedReader(fReader));

        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        createNewInstance();

        // 1. create the index
        // Creating an empty ArrayList of string type
        boolean firstLine = true;

        // Data pro Tf-Idf
        ArrayList<String> al = new ArrayList<String>();

        // Jednotlivé načtené dokumenty
        while (sc.hasNextLine()) {
            if (firstLine) {
                firstLine = false;
                continue;
            }
            String[] val = sc.nextLine().split(";");
            preprocessing.index(val[2]);

            CrawledPage cp = new CrawledPage(val[0], val[1], val[2]);
            al.add(cp.getBody());
            el.add(cp);
        }

        String[] docs = new String[al.size()];
        for (int i = 0; i < al.size(); i++) {
            String indexedValue = al.get(i);
            String[] arrOfStr = indexedValue.split(" ", 0);
            for(String s:arrOfStr){
                //System.out.println(s + " -> "+ preprocessing.getProcessedForm(s));
                //System.out.print(indexedValue + " -> ");
                indexedValue = indexedValue.replace(s, preprocessing.getProcessedForm(s));
                //System.out.println(indexedValue);
            }
            docs[i] = indexedValue;
        }

        cetnosti = new ArrayList<Map<String, Integer>>();

        // Výpočet četností termů v dokumentech - dotazu
        for (String doc : docs) {
            Map<String, Integer> freqMap = TextProcessor.getTermsCount(doc);
            cetnosti.add(freqMap);
        }

        // Výpočet Tf-Idf pro DOC x TERM
        List<Map<String, Double>> docTfIdf = tfidf.Utils.computeAllTfIdfs(cetnosti, cetnosti);


        // Druhé mocniny dokumentů a dotazu ====> jmenovatelé v cosinové vzdálenosti
        docSquared = tfidf.Utils.getNormalizedLengths(docTfIdf);

        // Převedeme na list
        docCosine = tfidf.Utils.listValues(docTfIdf, docSquared);
    }

    public ArrayList<Prepravka> processQuery(String query1){
        String query2 = new String(query1);
        String[] arrOfStr = query1.split(" ", 0);
        for(String s:arrOfStr){
            query1 = query1.replace(s, preprocessing.getProcessedForm(s));
        }

        System.out.println("Query: " + query2);
        Map<String, Integer> queryFrequency = TextProcessor.getTermsCount(query1);

        ArrayList<Map<String, Integer>> in = new ArrayList<Map<String, Integer>>();
        in.add(queryFrequency);

        List<Map<String, Double>> queryTfIdf = tfidf.Utils.computeAllTfIdfs(in, cetnosti);

        Map<Integer, Double> querySquared = tfidf.Utils.getNormalizedLengths(queryTfIdf);

        List<Map<String, Double>> queryCosine = tfidf.Utils.listValues(queryTfIdf, querySquared);
        Map<String, Double> queryCos = queryCosine.get(0);

        System.out.println("Výpočet cosinové vzdálenosti");
        Map<Integer, Double> cos = tfidf.Utils.computeCosDistance(docCosine, queryCos, docSquared, querySquared);

        ArrayList<Prepravka> best = setBest(cos, el);
        System.out.println();

        System.out.println("Query: " + query1);
        //System.out.println("Nejrelevantnější je dokument: " + el.get(best - 1).getTitle());
        return best;
    }

    private ArrayList<Prepravka> setBest(Map<Integer, Double> cos, ArrayList<CrawledPage> el){
        Map<Integer,Double> cos2 = cos.entrySet().stream().sorted((e1,e2)->
                        e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        ArrayList<Prepravka> result = new ArrayList<>();

        cos2.forEach((key,val)-> {
            if(val != 0) {
                Prepravka prepravka = new Prepravka(key, val.toString(), el.get(key - 1));
                result.add(prepravka);
            }
        });

        return result;
    }

    public Preprocessing getPreprocessing() {
        return preprocessing;
    }

    public ArrayList<CrawledPage> getEl() {
        return el;
    }
}
