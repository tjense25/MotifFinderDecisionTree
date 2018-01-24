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
        MotifFinder motifFinder = new MotifFinder();
        int k = 10;
        if (args.length < 1) {
            System.out.println("USAGE:\n java MotifFinder [decision_tree_file.txt] [# of motifs to find (default = 10)]\n" +
                                "OPTIONS:\n" +
                                "-a: get all Motifs from the tree\n" +
                                "-RF: apply to a RandomForest output (by defualt applies to one decision tree)");
            return;
        }
        else if (args.length >= 1) {
            if (args.length >=2 && args[1].equals("-a")) k = 0;
            else {
                if (args.length >= 2) k = Integer.parseInt(args[1]);
                if (k <= 0) {
                    System.out.print("Value for k must be a positive integer");
                    return;
                }
            }
            if (args.length >= 3 && args[2].equals("-RF")) motifFinder.runForest(args[0], k);
            else motifFinder.run(args[0], k);
        }
    }

    private void run(String text_file, int k) {
        DecisionForest dt = parse(text_file);
        dt.findMotifs();
        printMotifs(dt, k);
    }

    private void runForest(String text_file, int k) {
        DecisionForest df = parse(text_file);
        df.findForestMotifs();
        printMotifs(df, k);
    }

    private void printMotifs(DecisionForest df, int k) {
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
        List<String> neutrals = df.getNeutralMotifs(k);
        System.out.println("\nNEUTRAL: ");
        for (String motif : neutrals) {
            System.out.println(motif);
        }
    }

    private DecisionForest parse(String text_file) {
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
        return df;
    }
}
