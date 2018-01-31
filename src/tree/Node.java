package tree;

import parser.LineNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjense25 on 1/19/18.
 */
public class Node {
    private int position;
    private int depth;
    private char residue;
    protected List<Node> children;

    public Node() {
        position = -1;
        depth = -1;
        residue = 0;
        this.children = new ArrayList<>();
    }

    public Node(LineNode line) {
        this.children = new ArrayList<>();
        this.position = line.getPos();
        this.depth = line.getDepth();
        this.residue = line.getR();
    }

    public void addNode(Node node) {
        children.add(node);
    }

    public int getPosition() {
        return position;
    }

    public int getDepth() { return depth; }

    public char getResidue() {
        return residue;
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.depth; i++) {
            sb.append("|\t");
        }
        sb.append(String.format("pos%d = %c\n", this.position, this.residue));
        for(Node node : children) {
            sb.append(node.toString());
        }
        return sb.toString();
    }
}