package easycommand.utils;

import java.util.ArrayList;
import java.util.List;

public class CliUtils {

    public static String[] parse(String cli) {

        // a1 a2 "b c" d "e f"

        // a1 a2
        // b c
        // d
        // e f

        // a1
        // a2
        // "b
        // c"

        List<String> resultList = new ArrayList<>();

        String[] words = cli.split(" ");

        boolean inBlock = false;
        String prevWord = "";

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            if (inBlock == false && word.startsWith("\"")) {
                inBlock = true;
                prevWord = word.replace("\"", "");
            } else

            if (inBlock == true && word.endsWith("\"")) {
                inBlock = false;
                prevWord += " " + word.replace("\"", "");
                resultList.add(prevWord);
                prevWord = "";
            } else

            if (inBlock == true) {
                prevWord += " " + word;
            } else {
                resultList.add(word);
            }

        }

        String[] resultArr = new String[resultList.size()];
        resultList.toArray(resultArr);

        return resultArr;
    }

}
