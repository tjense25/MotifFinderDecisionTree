import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjense25 on 1/19/18.
 */
public class DecisionTree {

    private Node root;

    public DecisionTree() {
        this.root = new Node(-1, ' ');
    }

    public Node getRoot() {
        return this.root;
    }

    public class Node {
        private int position;
        private char residue;
        private List<Node> children = new ArrayList<>();

        public Node() {}

        public Node(int position, char residue) {
            this.position = position;
            this.residue = residue;
        }

        public void addNode(Node node) {
            children.add(node);
        }

        public int getPosition() {
            return position;
        }

        public char getResidue() {
            return residue;
        }

        public List<Node> getChildren() {
            return children;
        }
    }

    public class leafNode extends Node {
        private int count;
        private int misclassified;

        public leafNode(int position, char residue, int count, int misclassified) {
            super(position, residue);
            this.count = count;
            this.misclassified = misclassified;
        }
    }
}
