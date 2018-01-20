import org.omg.CORBA.DynAnyPackage.Invalid;

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
        addNode()


        return dt;
    }


    private Node getNextNode() throws LineNode.InvalidFormatException {
        LineNode line = null;
        if(scanner.hasNext()) {
           line = LineNode.parse(scanner.nextLine());
            if(line.isLeaf()) return new LeafNode((LeafLineNode) line);
            else return new Node(line);
        }
        return null;

    }

    private Node addNode(Node pnode, Node cnode) throws LineNode.InvalidFormatException {
        if(cnode.getDepth() - 1 != pnode.getDepth()) {
            return cnode;
        }
        pnode.addNode(cnode);
        Node next_node = getNextNode();
        if(next_node.getDepth() > cnode.getDepth()) {
            next_node = addNode(cnode, next_node);
        }
        else if(next_node.getDepth() < cnode.getDepth()) {
            return cnode;
        }
        else {
            while(next_node.getDepth() == cnode.getDepth()) {
                pnode.addNode(next_node);
                next_node = getNextNode();
            }
            if(next_node.getDepth() > cnode.getDepth()) {
                next_node = addNode(cnode, pnode);
            }
            else if(next_node.getDepth() < cnode.getDepth()) {
                return next_node;
            }
        }
        return null;
    }




}
