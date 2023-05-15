package preprocessing;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utils for I/O operations
 * Created by Tigi on 22.9.2014.
 */
public class Utils {
    public static final java.text.DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd_HH_mm_SS");

    public static Set<String> readStopWords(String file) {
        BufferedReader reader;
        HashSet<String> stopwords = new HashSet<String>();

        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                stopwords.add(line);
                //System.out.println(line);

                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stopwords;
    }


    /**
     * Saves text to given file.
     *
     * @param file file to save
     * @param text text to save
     */
    public static void saveFile(File file, String text) {
        try {
            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            printStream.print(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves lines from the list into given file; each entry is saved as a new line.
     *
     * @param file file to save
     * @param list lines of text to save
     */
    public static void saveFile(File file, Collection<String> list) {
        try {

            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            for (String text : list) {
                printStream.println(text);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }


    /**
     * Read lines from the stream; lines are trimmed and empty lines are ignored.
     *
     * @param inputStream stream
     * @return list of lines
     */
    public static List<String> readTXTFile(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Cannot locate stream");
        }
        try {
            List<String> result = new ArrayList<String>();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    result.add(line.trim());
                }
            }

            inputStream.close();

            return result;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
