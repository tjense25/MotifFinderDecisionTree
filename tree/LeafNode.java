package tree;

import parser.LeafLineNode;
import parser.Toxicity;
import tree.Node;

/**
 * Created by tjense25 on 1/19/18.
 */
public class LeafNode extends Node {
    private Toxicity tox;
    private int count;
    private int misclassified;

    public Toxicity getTox() {
        return tox;
    }

    public int getCount() {
        return count;
    }

    public int getMisclassified() {
        return misclassified;
    }

    public LeafNode(LeafLineNode line) {
        super(line);
        this.children = null;
        this.tox = line.getTox();
        this.count = line.getCount();
        this.misclassified = line.getMisclassified();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getDepth(); i++) {
            sb.append("|\t");
        }
        sb.append(String.format("pos%d = %c: ", getPosition(), getResidue()));
        sb.append(String.format( " %s (%d/%d)\n", Toxicity.asString(tox), count, misclassified));
        return sb.toString();
    }


}
