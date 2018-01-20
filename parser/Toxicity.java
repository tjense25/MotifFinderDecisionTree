package parser;

/**
 * Created by tjense25 on 1/19/18.
 */
public enum Toxicity {
    ANTITOX, NEUTRAL, TOXIC;

    public static String asString(Toxicity tox) {
        switch(tox) {
            case ANTITOX: return "anti-toxic";
            case NEUTRAL: return "neutral";
            case TOXIC: return "toxic";
            default: return "";
        }
    }
}
