import parser.DecisionTreeParser;
import parser.LineNode;
import tree.DecisionForest;

import java.io.IOException;
import java.util.List;

/**
 * Created by tjense25 on 1/20/18.
 */
public class MotifFinder {

    public MotifFinder() {}

    public static void main(String[] args) {
        int k = 10;
        if(args.length < 1) {
            System.out.println("USAGE:\n java MotifFinder [decision_tree_file.txt] [# of motifs to find (default = 10)]\n" +
                                "-a after file name will find all motifs");
            return;
        }
        else if(args.length > 1) {
            if(args[1].equals("-a")) k = 0;
            else {
                k = Integer.parseInt(args[1]);
                if (k <= 0) {
                    System.out.print("Value for k must be a positive integer");
                    return;
                }
            }
        }
        MotifFinder motifFinder = new MotifFinder();
        motifFinder.run(args[0], k);
    }

    public void run(String text_file, int k) {
        DecisionTreeParser treeParser = null;
        DecisionForest df = null;
        try {
            treeParser = new DecisionTreeParser(text_file);
            df = treeParser.getDecisionForest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineNode.InvalidFormatException e) {
            System.out.println("ERROR: Text File has invalid Format. Cannot Parse.");
            e.printStackTrace();
        }


        df.findMotifs();

        List<String> toxics = df.getToxicMotifs(k);
        System.out.println("TOXIC: ");
        for (String motif : toxics) {
            System.out.println(motif);
        }
        List<String> antitoxics = df.getAntitoxicMotifs(k);
        System.out.println("\nANTITOXIC: ");
        for (String motif: antitoxics) {
            System.out.println(motif);
        }
    }
}
