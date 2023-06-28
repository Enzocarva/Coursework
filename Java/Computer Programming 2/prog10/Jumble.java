package prog10;
import java.io.File;
import java.util.*;

import prog02.UserInterface;
import prog02.GUI;
import prog06.SkipMap;
import prog09.BTree;

public class Jumble {
    /**
     * Sort the letters in a word.
     * @param word Input word to be sorted, like "computer".
     * @return Sorted version of word, like "cemoptru".
     */
    public static String sort (String word) {
        char[] sorted = new char[word.length()];
        for (int n = 0; n < word.length(); n++) {
            char c = word.charAt(n);
            int i = n;

            while (i > 0 && c < sorted[i-1]) {
                sorted[i] = sorted[i-1];
                i--;
            }

            sorted[i] = c;
        }

        return new String(sorted, 0, word.length());
    }

    public static void main (String[] args) {
        UserInterface ui = new GUI("Jumble");
        // UserInterface ui = new ConsoleUI();

        // Map<String,String> map = new TreeMap<String,String>();
        // Map<String,String> map = new PDMap();
        // Map<String,String> map = new LinkedMap<String,String>();
        // Map<String,String> map = new SkipMap<String,String>();
        // Map<String, String> map = new BTree<String, String>();
        Map<String, List<String>> map = new BTree<String, List<String>>();

        Scanner in = null;
        do {
            try {
                in = new Scanner(new File(ui.getInfo("Enter word file.")));
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Try again.");
            }
        } while (in == null);

        int n = 0;
        while (in.hasNextLine()) {
            String word = in.nextLine();
            if (n++ % 1000 == 0)
                System.out.println(word + " sorted is " + sort(word));

            // EXERCISE: Insert an entry for word into map.
            // What is the key?  What is the value?
            ///
            String sorted = sort(word);
            if (!map.containsKey(sorted)) {
                List<String> wordList = new ArrayList<>();
                wordList.add(word);
                map.put(sorted, wordList);
            } else {
                List<String> wordList = map.get(sorted);
                wordList.add(word);
            }
            ///
        }

        String jumble = ui.getInfo("Enter jumble.");
        while (jumble != null) {
            List<String> words = map.get(sort(jumble));

            // EXERCISE:  Look up the jumble in the map.
            // What key do you use?
            ///
            ///
            if (words == null)
                ui.sendMessage("No match for " + jumble);
            else
                ui.sendMessage(jumble + " unjumbled is " + words);

            jumble = ui.getInfo("Enter Jumble.");
        }

        //Second loop
        while (true) {
            String letters = ui.getInfo("Enter letters from clues: ");
            if (letters == null) {
                return;
            }
            String sortedLetters = sort(letters);

            int intLetters = 0;
            do {
                String numberLetters = ui.getInfo("How many letters in the first word?");
                try {
                    intLetters = Integer.parseInt(numberLetters);
                    if (intLetters <= 0) {
                        ui.sendMessage(numberLetters + " is not a positive number.");
                    }
                } catch (Exception e) {
                    ui.sendMessage(numberLetters + " is not a number");
                }
            } while (intLetters <= 0);

            for (String key1 : map.keySet()) {
                if (key1.length() == intLetters) {
                    String key2 = "";
                    int key1Index = 0;

                    for (int i = 0; i < sortedLetters.length(); i++) {
                        if (key1Index < key1.length() && sortedLetters.charAt(i) == key1.charAt(key1Index)) {
                            key1Index++;
                        } else {
                            key2 = key2 + sortedLetters.charAt(i);
                        }
                    }

                    if (key1Index == key1.length()) {
                        if (map.containsKey(key2)) {
                            ui.sendMessage(map.get(key1) + " " + map.get(key2));
                        }
                    }
                }
            }
        }
    }
}