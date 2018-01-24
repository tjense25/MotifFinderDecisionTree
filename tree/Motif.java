package tree;

import parser.Toxicity;

/**
 * Created by tjense25 on 1/24/18.
 */
public class Motif {
    private String motif;
    private int toxCount;
    private int neuCount;
    private int antiCount;
    private Toxicity classification;
    private int total_sum;
    private int missclassified;
    private Double score;

    public Motif(String motif, Toxicity tox) {
        this.motif = motif;
        this.toxCount = 0;
        this.neuCount = 0;
        this.antiCount = 0;
        switch(tox) {
            case TOXIC: toxCount++; break;
            case NEUTRAL: neuCount++; break;
            case ANTITOX: antiCount++; break;
        }
        this.classification = null;
        this.score = null;
    }

    public int getToxCount() {
        return toxCount;
    }

    public int getNeuCount() {
        return neuCount;
    }

    public int getAntiCount() {
        return antiCount;
    }

    public void incrementTox(int value) {
        this.toxCount += value;
    }

    public void incrementNeu(int value) {
        this.neuCount += value;
    }

    public void incrementAnti(int value) {
        this.antiCount += value;
    }

    public Toxicity getClassification() {
        if (toxCount > neuCount) {
            if (antiCount  > toxCount) this.classification = Toxicity.ANTITOX;
            else this.classification = Toxicity.TOXIC;
        }
        else {
            if (antiCount > neuCount) this.classification = Toxicity.ANTITOX;
            else this.classification = Toxicity.NEUTRAL;
        }
        return this.classification;
    }

    public double getScore() {
        if (score != null) return this.score;
        if (this.classification == null) getClassification();
        this.total_sum = toxCount + neuCount + antiCount;
        this.missclassified = total_sum;
        switch(this.classification) {
            case TOXIC: missclassified -= toxCount; break;
            case NEUTRAL: missclassified -= neuCount; break;
            case ANTITOX: missclassified -= antiCount; break;
        }
        this.score = total_sum / ((double) missclassified + 1);
        return this.score;
    }

    public String getFormat() {
        return String.format(" %s (%d/%d)", this.motif, this.total_sum, this.missclassified);
    }
}
