package tree;

import java.util.*;

/**
 * Decision Forest: Stores the roots to a large number of decision trees and contains the algorithms to parse
 * through these trees and find the most toxic, antitoxic, and neutral motifs
 *
 * Created by tjense25 on 1/19/18.
 */
public class DecisionForest {

    private List<Node> roots;

    /**
     *  Collection of all the motifs in the decision tree stored based on their string sequence
     */
    private Map<String, Motif> motifList;

    /**
    *Collection of all the toxic motifs in the decision forest sorted by their motif score
     */
     private Set<Motif> toxicMotifList;

    /**
     *Collection of all the antitoxic motifs in the decision forest sorted by their motif score
     */
    private Set<Motif> antitoxMotifList;

    /**
     *Collection of all the neutral motifs in the decision forest sorted by their motif score
     */
    private Set<Motif> neutralMotifList;

    /**
     * Constructor: initializes list of root nodes to be an empty array list
     */
    public DecisionForest() {
        this.roots = new ArrayList<>();
    }

    /**
     * @return list of root nodes in the decision forest
     */
    public List<Node> getRoots() {
        return this.roots;
    }


    /**
     * Adds an empty node to the decision tree and returns a reference to this new node. Using this returned node,
     * you can build up a decision tree and it will be stored in this forest.
     * @return the empty decision tree node that was added to the forest
     */
    public Node addRoot() {
        Node root = new Node();
        this.roots.add(root);
        return root;
    }

    /**
     * Creates a string representation of the decision tree in the same format as the weka Random Forest output
     * @return string representation of random forest
     */
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


    /**
     * Traverses through all of the decision trees and stores the motifs into a new object that is stored in the
     * motifList Map. After all the motifs have been stored in the map. The program iterates over the map and depending
     * on the toxicity and score of the motif adds it to either the sorted toxic, antitox, or neutral motifList
     * @pre all decision trees have already been added to the forest
     * @post the toxic, antitox, and neutral motif list contain all the motifs sorted by their motif toxicity score
     */
    public void findMotifs() {
        this.motifList = new HashMap<>();
        StringBuilder motif_string = new StringBuilder("........"); //Starts with the trivial motif (matches with all peptides)
        for (Node root: this.roots) {
            for (Node node : root.getChildren()) {
                findMotifs(node, motif_string);
            }
        }

        //Initialize all the class motif lists to be empty TreeMaps
        //that are sorted in reverse order (from highest score to lower)
        toxicMotifList = new TreeSet<>(Collections.reverseOrder(new ScoreComparator()));
        antitoxMotifList = new TreeSet<>(Collections.reverseOrder(new ScoreComparator()));
        neutralMotifList = new TreeSet<>(Collections.reverseOrder(new ScoreComparator()));

        //Iterate through motifList map and depending on the classification of the motif, adds it to corresponding map
        for (String motifKey : this.motifList.keySet()) {
            Motif m = motifList.get(motifKey);
            switch(m.getClassification()) {
                case TOXIC: toxicMotifList.add(m); break;
                case ANTITOX: antitoxMotifList.add(m); break;
                case NEUTRAL: neutralMotifList.add(m); break;
            }
        }
    }

    /**
     * Helper function for the findMotifs(). Takes a given node of the decision tree and a partial motifs, and updates
     * the partial motif corresponding to the value in that node, and then either passes the motif along to all of its
     * children to be updated, or if it's a leaf node adds the completed motif to the motifList map
     * @param node the current node in the traversal of the decision forest
     * @param motif the string representation of the partial motif that has been created from the root node to this point
     */
    public void findMotifs(Node node, StringBuilder motif) {
        StringBuilder new_motif = new StringBuilder(motif);
        new_motif.setCharAt(node.getPosition() - 1, node.getResidue()); //update the motif based on residue & position in node
        if(node.isLeaf()) { //If is leafNode add the motif to the motifList map
            LeafNode leaf = (LeafNode) node;
            addToMotifs(motif.toString(), leaf);
        }
        else { //if not a leaf node continue updating motif for all of the nodes children
            for (Node child_node : node.getChildren()) {
                findMotifs(child_node, new_motif);
            }
        }
    }

    /**
     * Helper function to add new motifs to the motifList Map. Checks to see if motif is already present in the map, if
     * it is, it updates that motif corresponding to the toxicity class and count found in the leafNode.
     * @param motif the motif found from traversing the decision tree to a leaf node
     * @param leaf the leafNode in the decision tree corresponding to that motif. Contains the information for the count
     *             of the motif, the toxicity class, and the number of missclassified instances
     */
    private void addToMotifs(String motif, LeafNode leaf) {
        if (this.motifList.containsKey(motif)) {
            Motif m = motifList.get(motif);
            m.incrementMissclassified(leaf.getMisclassified());
            switch(leaf.getTox()) {
                case TOXIC: m.incrementTox(leaf.getCount()); break;
                case NEUTRAL: m.incrementNeu(leaf.getCount()); break;
                case ANTITOX: m.incrementAnti(leaf.getCount()); break;
            }
        }
        else {
            Motif m = new Motif(motif, leaf);
            this.motifList.put(motif, m);
        }
    }

    /**
     * Returns a list that contains the first k elements of the toxicMotif set
     * @param k the number of motifs that should be returned
     * @return A list of the k most toxic motifs
     * @pre The findMotif() function must have already been called so toxicMotifList is not null
     */
    public List<Motif> getToxicMotifs(int k) {
        return setToList(toxicMotifList, k);
    }

    /**
     * Returns a list that contains the first k elements of the antitoxicMotif set
     * @param k the number of motifs that should be returned
     * @return A list of the k most antitoxic motifs
     * @pre The findMotif() function must have already been called so antitoxMotifList is not null
     */
    public List<Motif> getAntitoxicMotifs(int k) {
         return setToList(antitoxMotifList, k);
    }

    /**
     * Returns a list that contains the first k elements of the neutralMotif set
     * @param k the number of motifs that should be returned
     * @return A list of the k most neutral motifs
     * @pre The findMotif() function must have already been called so neutralMotif set is not null
     */
    public List<Motif> getNeutralMotifs(int k) {
       return  setToList(neutralMotifList, k);
    }

    /**
     * converts a set of motifs into a list of the "count"-most probably motifs
     * @param set the Classification motif set which will be used to create the list
     * @param count the number of motifs to include in the list
     * @return A list of count number motifs that which had the highest score in the set
     * @pre set is either neutralMotifList, antitoxMotifList, or toxicMotifList
     * @post the return value contains the most toxic/antitoxic/neutral motifs
     */
    private List<Motif> setToList(Set<Motif> set, int count) {
        boolean keep_count = true;
        if(count == 0) keep_count = false;
        List<Motif> list = new ArrayList<>();
        for(Motif motif : set) {
                list.add(motif);
                if(keep_count) count--;
                if(keep_count && count <= 0) break;
        }
        return list;
    }


    /**
     * Score Comparator class. Comparator to be used the Classification sets (toxicMotifList, antitoxMotifList, etc. . .)
     * Compares motifs first by their calculated score based on counts and misclassified numbers, if they are even, compares
     * the motif strings lexicographically.
     */
    public class ScoreComparator implements Comparator<Motif> {

        /**
         * Comparator function to compare to motifs
         * @param m1 the first motif to compare
         * @param m2 the second motif to compare
         * @return -1 if m1 is less than m2, 0 if they are equal, and 1 if m1 is bigger
         */
        @Override
        public int compare(Motif m1, Motif m2) {
            if (m1.getScore() < m2.getScore()) return -1;
            else if (m1.getScore() == m2.getScore()) return m1.getMotif().compareTo(m2.getMotif());
            else return 1;
        }
    }
}
