import tree.Motif;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes a list of motifs and combines motifs together if possible to generate a new list of combined motifs
 * Created by tjense25 on 1/29/18.
 */
public class Combiner {
    /**
     * List of the String representation of each of the original motifs that were passed into the combiner class
     */
    private List<String> original_motifs;
    /**
     * List of the combined motifs created by the combiner function
     */
    private List<String> combined_motifs;

    /**
     * Constructor: sets original_motifs and combined_motifs to be empty array lists
     * @pre None
     * @post original_motifs and combined_motifs are empty lists
     */
    Combiner() {
        this.original_motifs = new ArrayList<>();
        this.combined_motifs = new ArrayList<>();
    }

    /**
     * Combines the motifs that were passed in, to form new consesus combined motifs
     * @param motifs List of motif objects that should be combeind
     * @return A list of strings representations of the new combined motifs
     * @pre motifs is not empty
     * @post return value will not be empty and will contain some of the possible motif combinations
     */
    public List<String> combineMotifs(List<Motif> motifs) {
        this.original_motifs = extractMotifs(motifs);
        this.combined_motifs = new ArrayList<>();
        if ("...S....".matches("......D.")) {
            System.out.println("hi");
        }
        for (int i = 0; i < original_motifs.size(); i++) {
            boolean combined = false;
            for (int j = i + 1; j < original_motifs.size(); j++) {
                if (original_motifs.get(j).matches(original_motifs.get(i))) {
                    combined_motifs.add(combine(original_motifs.get(i), original_motifs.get(j)));
                    combined = true;
                }
            }
            if (!combined) combined_motifs.add(original_motifs.get(i));
        }
        return this.combined_motifs;
    }

    /**
     * Takes in two motifs that can be combined and returns the resulting combined motif
     * @param motif1 the first motif that will be comined
     * @param motif2 the second motif to be combined
     * @return a string regex representation of the two combined motifs
     * @pre motif1 and motif2 can be combined (i.e. the regex for motif1 is matched by regex of motif2 and vise versa)
     * @post return value is the combined motif
     */
    private String combine(String motif1, String motif2) {
        StringBuilder new_motif = new StringBuilder();
        for (int i = 0; i < motif1.length(); i++) {
            if (motif1.charAt(i) == '.' && motif2.charAt(i) == '.') {
                new_motif.append('.');
            }
            else if (motif1.charAt(i) != '.') {
                new_motif.append(motif1.charAt(i));
            }
            else if (motif2.charAt(i) != '.') {
                new_motif.append(motif2.charAt(i));
            }
        }
        return new_motif.toString();
    }

    /**
     * Takes a list of motifs and extracts the regex string representation of the motif for each motif
     * @param motifs a list of motif objects
     * @return a list of strings corresponding to the motifs in motif
     */
    private List<String> extractMotifs(List<Motif> motifs) {
        List<String> stringMotifs = new ArrayList<>();
        for(Motif motif : motifs) {
            stringMotifs.add(motif.getMotif());
        }
        return stringMotifs;
    }

    public List<String> getOriginalMotifs() {
        return original_motifs;
    }

    public List<String> getCombinedMotifs() {
        return combined_motifs;
    }
}
