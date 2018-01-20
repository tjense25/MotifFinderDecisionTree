import javafx.util.Pair;

/**
 * Created by tjense25 on 1/19/18.
 */
public class LeafLineNode extends LineNode {

    private Toxicity tox;
    private int count;
    private int misclassified;

    public LeafLineNode(LineNode line, Toxicity tox, int count, int misclassified) {
        super(line);
        this.tox = tox;
        this.count = count;
        this.misclassified = misclassified;
    }

    public Toxicity getTox() {
        return tox;
    }

    public int getCount() {
        return count;
    }

    public int getMisclassified() {
        return misclassified;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    public static LineNode parseLeaf(String line) throws InvalidFormatException {
        LineNode lineNode = parseLine(line);
        String[] tokens = line.split(" ");
        Toxicity tox = extractToxClass(tokens, lineNode.getDepth());
        Pair<Integer, Integer> score = extractScore(tokens);
        return new LeafLineNode(lineNode, tox, score.getKey(), score.getValue());
    }

    private static Toxicity extractToxClass(String[] tokens, int depth) throws InvalidFormatException{
        Toxicity tox = null;
        try {
            String toxicity = tokens[depth + 4];
            if (toxicity.equals("anti-toxic")) tox = Toxicity.ANTITOX;
            else if (toxicity.equals("toxic")) tox = Toxicity.TOXIC;
            else if (toxicity.equals("neutral")) tox = Toxicity.NEUTRAL;
            else throw new Exception();
        } catch (Exception e) {
            throw new InvalidFormatException();
        }
        return tox;
    }

    private static Pair<Integer, Integer> extractScore(String[] tokens) throws InvalidFormatException {
        try {
            String scoreString = tokens[tokens.length - 1];
            scoreString = scoreString.substring(1, scoreString.length() - 1);
            double count;
            double missclasified;
            if (scoreString.contains("/")) {
                String[] scores = scoreString.split("/");
                count = Double.parseDouble(scores[0]);
                missclasified = Double.parseDouble(scores[1]);
            } else {
                count = Double.parseDouble(scoreString);
                missclasified = 0;
            }
            return new Pair<>((int) count, (int) missclasified);
        } catch(Exception e) {
            throw new InvalidFormatException();
        }
    }

}
