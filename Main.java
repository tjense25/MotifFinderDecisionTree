import parser.DecisionTreeParser;
import tree.DecisionTree;

import java.io.IOException;
import java.util.List;

/**
 * Created by tjense25 on 1/20/18.
 */
public class Main {

    public Main() {}

    public static void main(String[] args) {
        Main main = new Main();
        main.run(args[0]);
    }

    public void run(String text_file) {
        DecisionTreeParser treeParser = null;
        try {
            treeParser = new DecisionTreeParser(text_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DecisionTree dt = treeParser.getDecisionTree();
        dt.findMotifs();
        List<String> toxics = dt.getToxicMotifs();
        System.out.println("TOXIC: ");
        for (String motif : toxics) {
            System.out.println(motif);
        }
        List<String> antitoxics = dt.getAntitoxicMotifs();
        System.out.println("\nANTITOXIC: ");
        for (String motif: antitoxics) {
            System.out.println(motif);
        }
    }
}
