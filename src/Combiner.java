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
        for (int i = 0; i < original_motifs.size(); i++) {
            boolean combined = false;
            for (int j = i + 1; j < original_motifs.size(); j++) {
                if (isCombinable(original_motifs.get(j), original_motifs.get(i))) {
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
     * @pre motif1 and motif2 are of the same length
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
     * Helper function for the CombineMotifs function. Checks the precondition to the combine function
     * Function makes sure that the two motif strings do not conflict. So if the first motif is anything besides a wild card
     * in any given position, the function will check if the 2nd motif has a wild card at that position or if they are equal
     * @param m1 the first motif to test to see if the motifs can be combined
     * @param m2 the second motif to test
     * @return true is the two motif strings can be combined false if the two motifs can't
     * @pre motif1 and motif2 are of the same length
     */
    private boolean isCombinable(String m1, String m2) {
        for(int i = 0; i < m1.length(); i++) {
            if (m1.charAt(i) != '.' && (m2.charAt(i) != '.' && m1.charAt(i) != m2.charAt(i))) return false;
        }
        return true;
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
