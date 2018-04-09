import java.util.List;

public class Pronoun {
    public static void main(String[] args) {
        Pronoun pronoun=new Pronoun();
        String input = "whoever";
        PronounEnums pronounType = pronoun.checkPronounType(input);
//        System.out.println(pronoun.getPronounName(pronounType));
    }
    //Get pronoun Enum
    public static PronounEnums checkPronounType(String pronoun) {
        pronoun = pronoun.trim().toLowerCase();
        if (pronoun.trim() == "") {
            return null;
        }
        List<String> indefinite = PronounEnums.INDEFINITE.getPronounList();
        if (indefinite.contains(pronoun)) {
            return PronounEnums.INDEFINITE;
        }
        List<String> personal = PronounEnums.PERSONAL.getPronounList();
        if (personal.contains(pronoun)) {
            return PronounEnums.PERSONAL;
        }
        List<String> subjective = PronounEnums.SUBJECTIVE.getPronounList();
        if (subjective.contains(pronoun)) {
            return PronounEnums.SUBJECTIVE;
        }
        List<String> objective = PronounEnums.OBJECTIVE.getPronounList();
        if (objective.contains(pronoun)) {
            return PronounEnums.OBJECTIVE;
        }
        List<String> possessive = PronounEnums.POSSESSIVE.getPronounList();
        if (possessive.contains(pronoun)) {
            return PronounEnums.POSSESSIVE;
        }
        List<String> demonstrative = PronounEnums.DEMONSTRATIVE.getPronounList();
        if (demonstrative.contains(pronoun)) {
            return PronounEnums.DEMONSTRATIVE;
        }
        List<String> interrogative = PronounEnums.INTERROGATIVE.getPronounList();
        if (interrogative.contains(pronoun)) {
            return PronounEnums.INTERROGATIVE;
        }
        List<String> relative = PronounEnums.RELATIVE.getPronounList();
        if (relative.contains(pronoun)) {
            return PronounEnums.RELATIVE;
        }
        List<String> reflexive = PronounEnums.REFLEXIVE_INTENSIVE.getPronounList();
        if (reflexive.contains(pronoun)) {
            return PronounEnums.REFLEXIVE_INTENSIVE;
        }
        List<String> reciprocal = PronounEnums.RECIPROCAL.getPronounList();
        if (reciprocal.contains(pronoun)) {
            return PronounEnums.RECIPROCAL;
        }
        return null;
    }

    public static String getPronounName(PronounEnums pronounEnums,String dependencyName) {
        if (pronounEnums == null) {
            return null;
        }
        switch (pronounEnums) {
            case PERSONAL:
                if (dependencyName.equals("nsubj") || dependencyName.equals("nmod:agent")) {
                    return "subject";
                } else if (dependencyName.endsWith("obj") || dependencyName.equals("nsubjpass")) {
                    return "object";
                }
            case INDEFINITE:
                return "indefinite";
            case RECIPROCAL:
                return "reciprocal";
            case RELATIVE:
                return "relative";
            case OBJECTIVE:
                return "personal";
            case REFLEXIVE_INTENSIVE:
                if (dependencyName.equals("dobj")) {
                    return "reflexive";
                } else if (dependencyName.equals("nsubj")) {
                    return "intensive";
                }
            case POSSESSIVE:
                return "possessive";
            case SUBJECTIVE:
                return "personal";
            case DEMONSTRATIVE:
                return "demonstrative";
            case INTERROGATIVE:
                return "interrogative";
                default:
                    return null;
        }
    }
}
