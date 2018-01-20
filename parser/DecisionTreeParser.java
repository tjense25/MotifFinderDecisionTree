package parser;

import tree.DecisionTree;
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

    public DecisionTree getDecisionTree() {
        DecisionTree dt = new DecisionTree();
        try {
            addNode(dt.getRoot(), getNextNode());
        } catch (LineNode.InvalidFormatException e) {
            System.out.println("ERROR: Text File has invalid Format. Cannot Parse.");
            e.printStackTrace();
        }
        return dt;
    }


    private Node getNextNode() throws LineNode.InvalidFormatException {
        LineNode line = null;
        if(scanner.hasNext()) {
           line = LineNode.parse(scanner.nextLine());
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
