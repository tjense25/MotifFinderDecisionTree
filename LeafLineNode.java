/**
 * Created by tjense25 on 1/19/18.
 */
public class LeafLineNode extends LineNode {

    private String tox_class;
    private int count;
    private int misclassified;

    public LeafLineNode(LineNode line, String tox_class, int count, int misclassified) {
        super(line);
        this.tox_class = tox_class;
        this.count = count;
        this.misclassified = misclassified;
    }

    public static LineNode parseLeaf(String line) throws InvalidFormatException {
        LineNode lineNode = parseLine(line);
        String[] tokens = line.split(" ");
        String tox_class = extractToxClass(tokens, lineNode.getDepth());
    }

    private static String parseLeaf(String[] tokens, int depth) {

    }

}
