/**
 * Created by tjense25 on 1/19/18.
 */
public class LineNode {
    int depth;
    //Motif position
    int pos;
    //Amino Acid residue
    char r;

    static class InvalidFormatException extends Exception {}

    public LineNode() {}

    public LineNode(int depth, int pos, char r) {
        this.depth = depth;
        this.pos = pos;
        this.r = r;
    }

    public LineNode(LineNode line) {
        this.depth = line.depth;
        this.pos = line.pos;
        this.r = r;
    }

    public int getDepth() {
        return depth;
    }

    public int getPos() {
        return pos;
    }

    public char getR() {
        return r;
    }

    public boolean isLeaf() {
        return false;
    }

    public static LineNode parse(String line) throws InvalidFormatException {
        if(line.endsWith(")")) {
            return LeafLineNode.parseLeaf(line);
        }
        else {
            return parseLine(line);
        }
    }

    protected static LineNode parseLine(String line) throws InvalidFormatException {
        String[] tokens = line.split(" ");
        int depth = extractDepth(tokens);
        int pos = extractPos(tokens, depth);
        char r = extractRes(tokens, depth);
        return new LineNode(depth, pos, r);

    }

    private static int extractDepth(String[] tokens) throws InvalidFormatException {
        int depth = 0;
        try {
            while (tokens[depth].equals("|")) {
                depth++;
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidFormatException();
        }
        return depth;
    }

    private static int extractPos(String[] tokens, int depth) throws InvalidFormatException {
        int pos = 0;
        try {
            String posString = tokens[depth + 1];
            pos = Integer.parseInt(posString.substring(posString.length() - 1));
        } catch (Exception e) {
            throw new InvalidFormatException();
        }
        return pos;
    }

    private static char extractRes(String[] tokens, int depth) throws InvalidFormatException {
        char r = 0;
        try {
            String resString = tokens[depth + 3];
            r = resString.charAt(0);
        } catch(Exception e) {
            throw new InvalidFormatException();
        }
        return r;
    }

}
