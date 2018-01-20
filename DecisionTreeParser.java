import java.io.*;
import java.util.Scanner;

/**
 * Created by tjense25 on 1/19/18.
 */
public class DecisionTreeParser {

    private Scanner input;

    public DecisionTreeParser(String input_file_name) throws IOException {
            this.input = new Scanner(new BufferedReader(new FileReader(input_file_name)));
    }

    public DecisionTree getDecisionTree() {
        DecisionTree dt = new DecisionTree();
        int depth = 0;
        int prev_depth = -1;
        DecisionTree.Node node = dt.getRoot();
        DecisionTree.Node parent_node = dt.getRoot();
        while(input.hasNext()) {
            String line = input.nextLine();
            String words[] = line.split(" ");
            depth = 0;
            while(words[depth].equals("|")) {
                depth++;
            }
            String pos = words[depth + 1];
            if(depth > prev_depth) {

            }

        }

        return dt;
    }

}
