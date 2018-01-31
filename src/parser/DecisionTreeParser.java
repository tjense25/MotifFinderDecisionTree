package parser;

import tree.DecisionForest;
import tree.LeafNode;
import tree.Node;

import java.io.*;
import java.util.Scanner;

/**
 * Created by tjense25 on 1/19/18.
 */
public class DecisionTreeParser {

    private Scanner scanner;

    public DecisionTreeParser(String input_file_name) throws IOException {
            this.scanner = new Scanner(new BufferedReader(new FileReader(input_file_name)));
    }

    public DecisionForest getDecisionForest() throws LineNode.InvalidFormatException {
        DecisionForest df = new DecisionForest();
        while(scanner.hasNext()) {
            addNode(df.addRoot(), getNextNode());
        }
        return df;
    }


    private Node getNextNode() throws LineNode.InvalidFormatException {
        LineNode line = null;
        if(scanner.hasNext()) {
           String line_string = scanner.nextLine();
           if(line_string.matches("(\\s*|^=+.*|^RandomTree.*)")) return getNextNode();
           else if (line_string.startsWith("Size of the tree")) return null;
           line = LineNode.parse(line_string);
            if(line.isLeaf()) {
                LeafLineNode leafLine = (LeafLineNode) line;
                if (leafLine.getCount() == 0) return getNextNode(); //don't add nodes with 0 count to tree
                else return new LeafNode(leafLine);
            }
            else return new Node(line);
        }
        return null;

    }

    private Node addNode(Node pnode, Node cnode) throws LineNode.InvalidFormatException {
        Node next_node = null;
        if(cnode == null) {
            return null;
        }
        if(cnode.getDepth() <= pnode.getDepth()) {
            return cnode;
        }
        else if(cnode.getDepth() > pnode.getDepth()) {
            pnode.addNode(cnode);
            next_node = addNode(cnode, getNextNode());
            if(next_node != null) next_node = addNode(pnode, next_node);
        }
        return next_node;
    }




}
