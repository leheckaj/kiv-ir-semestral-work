/**
 * Copyright (c) 2014, Michal Konkol
 * All rights reserved.
 */
package preprocessing;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Michal Konkol
 */
public class AdvancedTokenizer implements Tokenizer,java.io.Serializable {
    //cislo |  | html | tecky a ostatni
    //public static final String defaultRegex = "(\\d+[.,](\\d+)?)|([\\p{L}\\d]+)|(<.*?>)|([\\p{Punct}])";

    // Cenzorované u URL - regex
    public static final String URL_REGEX = "([\\w+À-ž]+\\*[\\w+À-ž]*|[\\w+À-ž]*\\*[\\w+À-ž]+)";

    // Webové adresy - URL - regex
    public static final String WEB_REGEX ="(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";

    // Datum - regex
    public static final String DAY_MONTH_REGEX = "\\d{1,2}\\.\\d{1,2}\\.";
    public static final String DAY_MONTH_YEAR_REGEX = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}";
    public static final String DATUM_REGEX = "("+DAY_MONTH_YEAR_REGEX+"|"+DAY_MONTH_REGEX+")";

    // Interpunkce - regex
    public static final String PUNCTUATION_REGEX = "([\\p{L}\\d]+)|(<.*?>)|([\\p{Punct}])";

    // datum | URL | web | interpunkce
    public static final String defaultRegex = DATUM_REGEX +  "|" + URL_REGEX +"|" + WEB_REGEX + "|" + PUNCTUATION_REGEX;


    public static String[] tokenize(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);

        ArrayList<String> words = new ArrayList<String>();

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            words.add(text.substring(start, end));
        }

        String[] ws = new String[words.size()];
        ws = words.toArray(ws);

        return ws;
    }

    public static String removeAccents(String text) {
        return text == null ? null : Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    @Override
    public String[] tokenize(String text) {
        return tokenize(text, defaultRegex);
    }
}
