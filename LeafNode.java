/**
 * Created by tjense25 on 1/19/18.
 */
public class LeafNode extends Node {
    private Toxicity tox;
    private int count;
    private int misclassified;

    public LeafNode(LeafLineNode line) {
        super(line);
        this.children = null;
        this.tox = line.getTox();
        this.count = line.getCount();
        this.misclassified = line.getMisclassified();
    }
}
