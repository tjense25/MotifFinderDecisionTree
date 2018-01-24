package tree;

import com.sun.xml.internal.fastinfoset.util.CharArray;
import parser.Toxicity;

import java.util.*;

/**
 * Created by tjense25 on 1/19/18.
 */
public class DecisionForest {

    private List<Node> roots;
    private Map<String, Motif> motifList;
    private Map<Double, List<String>> toxicMotifList;
    private Map<Double, List<String>> antitoxMotifList;
    private Map<Double, List<String>> neutralMotifList;

    public DecisionForest() {
        this.roots = new ArrayList<>();
    }

    public List<Node> getRoots() {
        return this.roots;
    }


    public Node addRoot() {
        Node root = new Node();
        this.roots.add(root);
        return root;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node root : roots) {
            sb.append("RandomTree\n===========\n\n");
            List<Node> nodeList = root.getChildren();
            for(Node node : nodeList) {
                sb.append(node.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void findMotifs() {
        toxicMotifList = new TreeMap<>(Collections.reverseOrder());
        antitoxMotifList = new TreeMap<>(Collections.reverseOrder());
        neutralMotifList = new TreeMap<>(Collections.reverseOrder());
        StringBuilder motif = new StringBuilder("........");
        for(Node root: this.roots) {
            for (Node node : root.getChildren()) {
                findMotifs(node, motif);
            }
        }
    }

    public void findForestMotifs() {
        this.motifList = new HashMap<>();
        StringBuilder motif = new StringBuilder("........");
        for (Node root: this.roots) {
            for (Node node : root.getChildren()) {
                findForestMotifs(node, motif);
            }
        }

        toxicMotifList = new TreeMap<>(Collections.reverseOrder());
        antitoxMotifList = new TreeMap<>(Collections.reverseOrder());
        neutralMotifList = new TreeMap<>(Collections.reverseOrder());
        for (String motifKey : this.motifList.keySet()) {
            Motif m = motifList.get(motifKey);
            switch(m.getClassification()) {
                case TOXIC: addToMap(toxicMotifList, m.getScore(), m.getFormat());
                case ANTITOX: addToMap(antitoxMotifList, m.getScore(), m.getFormat());
                case NEUTRAL: addToMap(neutralMotifList, m.getScore(), m.getFormat());
            }
        }
    }

    public void findMotifs(Node node, StringBuilder motif) {
        StringBuilder new_motif = new StringBuilder(motif);
        new_motif.setCharAt(node.getPosition() - 1, node.getResidue());
        if (node.isLeaf()) {
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
            for (Node child_node : node.getChildren()) {
                findMotifs(child_node, new_motif);
            }
        }
    }

    public void findForestMotifs(Node node, StringBuilder motif) {
        StringBuilder new_motif = new StringBuilder(motif);
        new_motif.setCharAt(node.getPosition() - 1, node.getResidue());
        if(node.isLeaf()) {
            LeafNode leaf = (LeafNode) node;
            addToMotifs(motif.toString(), leaf.getTox(), leaf.getCount());
        }
        else {
            for (Node child_node : node.getChildren()) {
                findForestMotifs(child_node, new_motif);
            }
        }
    }

    private void addToMotifs(String motif, Toxicity tox, int count) {
        if (this.motifList.containsKey(motif)) {
            Motif m = motifList.get(motif);
            switch(tox) {
                case TOXIC: m.incrementTox(count);
                case NEUTRAL: m.incrementNeu(count);
                case ANTITOX: m.incrementAnti(count);
            }
        }
        else {
            Motif m = new Motif(motif, tox);
            this.motifList.put(motif, m);
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

    public List<String> getToxicMotifs(int k) {
        List<String> toxicMotifs = mapToList(toxicMotifList, k);
        return toxicMotifs;
    }

    public List<String> getAntitoxicMotifs(int k) {
        List<String> antitoxicMotifs = mapToList(antitoxMotifList, k);
        return antitoxicMotifs;
    }

    public List<String> getNeutralMotifs(int k) {
        List<String> neutralMotifs = mapToList(neutralMotifList, k);
        return neutralMotifs;
    }

    private List<String> mapToList(Map<Double, List<String>> map, int count) {
        boolean keep_count = true;
        if(count == 0) keep_count = false;
        List<String> list = new ArrayList<>();
        for(Double doub : map.keySet()) {
            for(String motif : map.get(doub)) {
                list.add(String.format("%s %f", motif, doub));
                if(keep_count) count--;
                if(keep_count && count <= 0) break;
            }
            if(keep_count && count <= 0) break;
        }
        return list;
    }

}
