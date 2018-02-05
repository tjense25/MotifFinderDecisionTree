import parser.DecisionTreeParser;
import parser.LineNode;
import tree.DecisionForest;
import tree.Motif;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjense25 on 1/20/18.
 */
public class MotifFinder {

    private String input_file;
    private int k = 10;
    private boolean combine = false;
    private boolean noneu = false;
    private boolean noanti = false;
    private boolean notox = false;
    private static final String USAGE = "USAGE:\n java MotifFinder [decision_tree_file.txt] [options]\n" +
            "OPTIONS:\n" +
            "-k [int]: specify the number of motifs of each class that you want to print\n" +
            "-a: get all Motifs from the tree\n" +
            "-c: combine the resulting motifs together to get a list of potential consensus motifs\n" +
            "-noneu: No-neutral. Do not print out motifs for neutral data\n" +
            "-notox: No-Toxic. Do not print out motifs for toxic data\n" +
            "-noanti: No-Antitoxic. Do not print out motifs for antitoxic data\n";

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
        try {
            motifFinder.input_file = args[0];
            for (int i = 1; i < args.length; i++) {
                switch (args[i]) {
                    case "-k":
                        motifFinder.k = Integer.parseInt(args[i + 1]);
                        i++;
                        break;
                    case "-a":
                        motifFinder.k = 0;
                        break;
                    case "-c":
                        motifFinder.combine = true;
                        break;
                    case "-noneu":
                        motifFinder.noneu = true;
                        break;
                    case "-notox":
                        motifFinder.notox = true;
                        break;
                    case "-noanti":
                        motifFinder.noanti = true;
                        break;
                }
            }
        } catch(Exception e) {
            System.err.println(USAGE);
            return;
        }

        motifFinder.run();
    }

    /**
     * Non-static function to actually run the motifFinder algorithms
     * @Pre input_file, and all other command line parameters have already been initialized
     * @Post possible motifs are printed to the screen
     */
    private void run() {
        DecisionForest df = parse(input_file);
        df.findMotifs();
        printMotifs(df);
    }

    /**
     * Once motifs have been found prints the motifs to standard output.
     * @param df the decision forest storing the toxic, antitoxic, and neutral motifs
     */
    private void printMotifs(DecisionForest df) {
        //Initialize classification motifs to empty array lists
        List<Motif> toxics = new ArrayList<>();
        List<Motif> antitoxics = new ArrayList<>();
        List<Motif> neutrals = new ArrayList<>();

        //If user wants to display toxic get toxic motifs and print them to screen
        if (!notox) {
            toxics = df.getToxicMotifs(k);
            for (Motif motif : toxics) {
                System.out.println(motif.toString());
            }
        }

        //If user wants to display anti get antitoxic motifs from decision forest and print them to screen
        if (!noanti) {
            antitoxics = df.getAntitoxicMotifs(k);
            for (Motif motif : antitoxics) {
                System.out.println(motif.toString());
            }
        }

        if (!noneu) {
            neutrals = df.getNeutralMotifs(k);
            for (Motif motif : neutrals) {
                System.out.println(motif.toString());
            }
        }

        //If combine is true print combined as well depending on if user wants to display tox, anti, or neutral
        if (combine) {
            System.out.println("\nCOMBINED MOTIFS:");
            System.out.println("===================");
            Combiner motifCombiner = new Combiner();
            List<String> motifs = null;

            if (!notox) {
                motifs = motifCombiner.combineMotifs(toxics);
                for (String motif : motifs) {
                    System.out.println(String.format("%s\t%s", motif, "toxic"));
                }
            }

            if (!noanti) {
                motifs = motifCombiner.combineMotifs(antitoxics);
                for (String motif : motifs) {
                    System.out.println(String.format("%s\t%s", motif, "antitoxic"));
                }
            }

            if(!noneu) {
                motifs = motifCombiner.combineMotifs(neutrals);
                for (String motif : motifs) {
                    System.out.println(String.format("%s\t%s", motif, "neutral"));
                }
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
