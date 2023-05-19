package tfidf;

import java.io.FileNotFoundException;

/**
 * Main
 * Hlavní třída programu, slouží pro orchestraci celého úkolu
 *
 * Program vypočítává Tf-Idf pro vzorový příklad + pro nacrawlovaná data
 * Vyhledává v rámcí nadpisů článků (title)
 *
 * Jak zbuildit
 * Plugins -> Assembly -> assembly-assembly
 */
public class Main {
    public  static void printHeader(String text){
        System.out.println("##################################################");
        for (int i=0; i< (34-text.length())/2; i++){
            System.out.print(" ");
        }
        System.out.println("#####  " + text + "  #######");

        System.out.println("##################################################");
        System.out.println();
    }

    public static void help(){
        System.out.println("Prosím vložte soubor output.csv se zmenšenou nacrawlovanou sadou vedle tohoto JARU");
        System.out.println("Spuštění: java -jar leheckaj-rank.jar");
    }

    public static void main(String[] args){
        printHeader("Cvičení:");
        new Excercise().excercise1();

        printHeader("Aplikace na nacrawlovaná data:");

        try {
            Operator.process("output.csv");
        } catch (FileNotFoundException e) {
            help();
        }
    }

}
