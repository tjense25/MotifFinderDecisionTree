package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjense25 on 1/19/18.
 */
public class DecisionTree {

    private Node root;

    public DecisionTree() {
        this.root = new Node();
    }

    public Node getRoot() {
        return this.root;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Node> nodeList = root.getChildren();
        for(Node node : nodeList) {
            sb.append(node.toString());
        }
        return sb.toString();
    }


}
