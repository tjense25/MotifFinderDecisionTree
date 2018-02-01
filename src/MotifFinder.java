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

    /**
     * Constructor
     */
    public MotifFinder() {}

    /**
     * main method: finds motifs in the given random forest output file
     * @param args command-line arguments
     * @pre args contains at least 1 element, the name of the file with the decision tree, but can also specify the number
     *      of motifs to find and a "-c" to combine the motifs
     */
    public static void main(String[] args) {
        MotifFinder motifFinder = new MotifFinder();
        int k = 10;
        boolean combine = false;
        if (args.length < 1) {
            System.err.println("USAGE:\n java MotifFinder [decision_tree_file.txt] [# of motifs to find (default = 10)]\n" +
                                "OPTIONS:\n" +
                                "-a: get all Motifs from the tree\n" +
                                "-c: combine the resulting motifs together to get a list of potential consensus motifs");
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
            if (args.length >= 3 && args[2].equals("-c")) combine = true;
            motifFinder.run(args[0], k, combine);
        }
    }

    /**
     * Non-static function to find motifs
     * @param text_file output of the decision tree or random forest model which will be used to find motifs
     * @param k the number of motifs to find for each toxicity class
     * @param combine boolean value for whether or not to combine the motifs once they are found
     */
    private void run(String text_file, int k, boolean combine) {
        DecisionForest dt = parse(text_file);
        dt.findMotifs();
        printMotifs(dt, k, combine);
    }

    /**
     * Once motifs have been found prints the motifs to standard output.
     * @param df the decision forest storing the toxic, antitoxic, and neutral motifs
     * @param k the number of motifs to print
     * @param combine boolean value to determine whether to combine the motifs into a consensus motif
     */
    private void printMotifs(DecisionForest df, int k, boolean combine) {
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

        if(combine) {
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
    }


    /**
     * Calls DecisionTreeParser object to parse through the output and recreate the decision trees into a decision tree
     * Forest
     * @param text_file decision forest text file to parse and create a decision tree from
     * @return A DecisionForest object that contains all the decision tree
     */
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
