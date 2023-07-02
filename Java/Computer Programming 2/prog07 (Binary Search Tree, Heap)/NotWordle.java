package prog07;

import prog02.GUI;
import prog02.UserInterface;
import prog05.LinkedQueue;
import prog07.Heap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//----------------------------------------------------------------------------------------------------------------------
public class NotWordle {
    //----------------------------------------------------------------------------------------------------------------------
    protected class Node {
        String word;
        public prog05.NotWordle.Node next;

        public Node(String word) { //Node constructor
            this.word = word;
            this.next = null;
        }
        public String getWord() {
            return word;
        }
    } //end of Node class
    //----------------------------------------------------------------------------------------------------------------------
    List<prog05.NotWordle.Node> wordEntries = new ArrayList<prog05.NotWordle.Node>();
    UserInterface ui; //class variable
    //----------------------------------------------------------------------------------------------------------------------
    public NotWordle (UserInterface ui) { // constructor
        this.ui = ui;
    }
    //----------------------------------------------------------------------------------------------------------------------
    public boolean loadWords (String file) {
        try {
            // Create buffer reader to read file
            Scanner reader = new Scanner(new File(file));
            String word;

            // Read every word in the file, call constructor to make a Node, add it to ArrayList
            while (reader.hasNextLine()) {
                word = reader.nextLine();
                prog05.NotWordle.Node wordNode = new prog05.NotWordle.Node(word);
                wordEntries.add(wordNode);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            ui.sendMessage(file + ": file not found");
            System.out.println(e);
            return false;
        }
        return true;
    }
    //----------------------------------------------------------------------------------------------------------------------
    public prog05.NotWordle.Node find (String word) {
       /* int low = 0;
        int high = wordEntries.size();
        int mid;

        while (low < high) {
            mid = (low + high) / 2;
            int compare = wordEntries.get(mid).getWord().compareTo(word);
            if (compare >= 0) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return wordEntries.get(high);
        */

        for (int i = 0; i < wordEntries.size(); i++)
            if (wordEntries.get(i).getWord().equals(word))
                return wordEntries.get(i);

        return null;
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static void main (String[] args) {
        GUI ui = new GUI("NotWordle Game");
        prog05.NotWordle game = new prog05.NotWordle(ui);

        while (true) {
            String wordFile = ui.getInfo("Enter the name of a word file: ");
            if (!game.loadWords(wordFile)) {
                continue;
            } else
                break;
        }

        //game.loadWords(wordFile);

        //Ask for start
        //ui.getInfo
        String startingWord = ui.getInfo("Enter a starting word:");

        //Ask for end
        String targetWord = ui.getInfo("Enter target word:");

        //Ask for human or computer
        String[] commands = { "Human plays", "Computer plays", "Computer plays V2", "Computer plays V3" };
        //ui.getCommand(commands)

        int c = ui.getCommand(commands);
        switch (c) {
            case 0:
                game.play(startingWord, targetWord);
                break;
            case 1:
                game.solve(startingWord, targetWord);
                break;
            case 2:
                game.solve2(startingWord, targetWord);
                break;
            case 3:
                game.solve3(startingWord, targetWord);
                break;
            default:
                ui.sendMessage("ERROR");
                break;
        }
        //If Human
        //Call Play
        //If Computer
        //Call solve
    }
    //----------------------------------------------------------------------------------------------------------------------
    public void play (String start, String end) {
        while (true) {
            ui.sendMessage("Current word: " + start + "\nTarget word: " + end);
            String current = ui.getInfo("What is your next word?");
            if (current == null) {
                break;
            }
            if (find(current) == null) {
                ui.sendMessage(current + " is not an existing word.\nTry again.");
                continue;
            }
            if (oneLetterDifferent(start, current)) {
                start = current;
            } else {
                ui.sendMessage("The input word does not differ by exactly one letter.");
            }
            if (start.equals(end)) {
                ui.sendMessage("You win!");
                return;
            }
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    public void solve (String start, String end) {
        LinkedQueue<prog05.NotWordle.Node> queue = new LinkedQueue<prog05.NotWordle.Node>();
        prog05.NotWordle.Node firstNode = find(start);
        queue.offer(firstNode);
        int count = 0;

        while (!queue.isEmpty()) {
            prog05.NotWordle.Node theNode = queue.poll();
            count++;
            for (prog05.NotWordle.Node nextNode : wordEntries) {
                if (oneLetterDifferent(theNode.word, nextNode.word) && (nextNode != firstNode) && (nextNode.next == null)) {
                    nextNode.next = theNode;
                    queue.offer(nextNode);
                    if (nextNode.word.equals(end)) {
                        ui.sendMessage("You win! You got from " + start + " to " + end);
                        String s = "";
                        while (nextNode != null) {
                            s = "\n" + nextNode.word + s;
                            nextNode = nextNode.next;
                        }
                        ui.sendMessage(s);
                        ui.sendMessage("Poll ran " + count + " times.");
                        return;
                    }
                }
            }
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static boolean oneLetterDifferent (String current, String next) {
        int difference = 0;
        if (current.length() == next.length()) {
            for (int i = 0; i < current.length(); i++) {
                if (current.charAt(i) != next.charAt(i)) {
                    difference++;
                }
            }
        }
        if (difference == 1) {
            return true;
        }
        return false;
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static int lettersDifferent (String word, String target) {

        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                count++;
            }
        }
        return count;
    }
    //----------------------------------------------------------------------------------------------------------------------
    public int distanceFromStart (prog05.NotWordle.Node node) {

        int count = 0;
        for (prog05.NotWordle.Node n = node; n != null; n = n.next) {
            count++;
        }
        return count;
    }
    //======================================================================================================================
    protected class NodeComparator implements Comparator<prog05.NotWordle.Node> {
        String target;
        //----------------------------------------------------------------------------------------------------------------------
        public NodeComparator(String target) {
            this.target = target;
        }
        //----------------------------------------------------------------------------------------------------------------------
        public int priority (prog05.NotWordle.Node node) {
            return distanceFromStart(node) + lettersDifferent(node.word, target);
        }
        //----------------------------------------------------------------------------------------------------------------------
        public int compare (prog05.NotWordle.Node entryA, prog05.NotWordle.Node entryB) {
            return priority(entryA) - priority(entryB);
        }
//----------------------------------------------------------------------------------------------------------------------
    }
//----------------------------------------------------------------------------------------------------------------------
}
//======================================================================================================================
