package tree;

import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.util.*;

/**
 * Created by tjense25 on 1/19/18.
 */
public class DecisionTree {

    private Node root;
    private Map<Double, List<String>> toxicMotifList;
    private Map<Double, List<String>> antitoxMotifList;
    private Map<Double, List<String>> neutralMotifList;

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

    public void findMotifs() {
        toxicMotifList = new TreeMap<>(Collections.reverseOrder());
        antitoxMotifList = new TreeMap<>(Collections.reverseOrder());
        neutralMotifList = new TreeMap<>(Collections.reverseOrder());
        StringBuilder motif = new StringBuilder("********");
        for(Node node : root.getChildren()) {
            findMotifs(node, motif);
        }
    }

    public void findMotifs(Node node, StringBuilder motif) {
        StringBuilder new_motif = new StringBuilder(motif);
        new_motif.setCharAt(node.getPosition() - 1, node.getResidue());
        if(node.isLeaf()) {
            LeafNode leaf = (LeafNode) node;
            double score = leaf.getCount() / ((double) leaf.getMisclassified() + 1);
            String motif_format = String.format("%s (%d/%d)", new_motif.toString(), leaf.getCount(), leaf.getMisclassified());
            switch(leaf.getTox()) {
                case TOXIC: addToMap(toxicMotifList, score, motif_format);
                case ANTITOX: addToMap(antitoxMotifList, score, motif_format);
                case NEUTRAL: addToMap(neutralMotifList, score, motif_format);
            }
        }
        else {
            for(Node child_node : node.getChildren()) {
                findMotifs(child_node, new_motif);
            }
        }
    }

    private void addToMap(Map<Double, List<String>> map, double score, String motif) {
        if(map.containsKey(score)) {
            map.get(score).add(motif);
        }
        else {
            List<String> list = new ArrayList<>();
            list.add(motif);
            map.put(score, list);
        }
    }

    public List<String> getToxicMotifs() {
        List<String> toxicMotifs = mapToList(toxicMotifList, 10);
        return toxicMotifs;
    }

    public List<String> getAntitoxicMotifs() {
        List<String> antitoxicMotifs = mapToList(antitoxMotifList, 10);
        return antitoxicMotifs;
    }

    public List<String> getNeutralMotifs() {
        List<String> neutralMotifs = mapToList(neutralMotifList, 10);
        return neutralMotifs;
    }

    private List<String> mapToList(Map<Double, List<String>> map, int count) {
        List<String> list = new ArrayList<>();
        for(Double doub : map.keySet()) {
            for(String motif : map.get(doub)) {
                list.add(String.format("%s %f", motif, doub));
                count--;
                if(count < 0) break;
            }
            if(count < 0) break;
        }
        return list;
    }



}
