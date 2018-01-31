import parser.DecisionTreeParser;
import parser.LineNode;
import tree.DecisionForest;
import tree.Motif;

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
            System.err.println("USAGE:\n java MotifFinder [decision_tree_file.txt] [# of motifs to find (default = 10)]\n" +
                                "OPTIONS:\n" +
                                "-a: get all Motifs from the tree\n");
            return;
        }
        else if (args.length >= 1) {
            if (args.length >=2 && args[1].equals("-a")) k = 0;
            else {
                if (args.length >= 2) k = Integer.parseInt(args[1]);
                if (k <= 0) {
                    System.err.print("Value for k must be a positive integer");
                    return;
                }
            }
            motifFinder.run(args[0], k);
        }
    }

    private void run(String text_file, int k) {
        DecisionForest dt = parse(text_file);
        dt.findMotifs();
        printMotifs(dt, k);
    }

    private void printMotifs(DecisionForest df, int k) {
        List<Motif> toxics = df.getToxicMotifs(k);

        for (Motif motif : toxics) {
            System.out.println(motif.toString());
        }
        List<Motif> antitoxics = df.getAntitoxicMotifs(k);
        for (Motif motif: antitoxics) {
            System.out.println(motif.toString());
        }
        List<Motif> neutrals = df.getNeutralMotifs(k);
        System.out.println("\nNEUTRAL: ");
        for (Motif motif : neutrals) {
            System.out.println(motif.toString());
        }

        System.out.println("\nCOMBINED MOTIFS:");
        System.out.println("===================");
        Combiner motifCombiner = new Combiner();
        List<String> motifs = motifCombiner.combineMotifs(toxics);
        for (String motif : motifs) {
            System.out.println(String.format("%s\t%s", motif, "toxic"));
        }
        motifs = motifCombiner.combineMotifs(antitoxics);
        for (String motif : motifs) {
            System.out.println(String.format("%s\t%s", motif, "antitoxic"));
        }
        motifs = motifCombiner.combineMotifs(neutrals);
        for (String motif : motifs) {
            System.out.println(String.format("%s\t%s", motif, "neutral"));
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
            System.err.println("ERROR: Text File has invalid Format. Cannot Parse.");
            e.printStackTrace();
        }
        return df;
    }
}
