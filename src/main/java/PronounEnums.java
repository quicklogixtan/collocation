import java.util.ArrayList;
import java.util.List;

public enum PronounEnums {
    PERSONAL {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("i");//LowerCase of I
            typeList.add("you");
            typeList.add("he");
            typeList.add("she");
            typeList.add("it");
            typeList.add("we");
            typeList.add("they");
            typeList.add("me");
            typeList.add("him");
            typeList.add("her");
            typeList.add("us");
            typeList.add("them");
            return typeList;
        }
    },
    SUBJECTIVE {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("i");//LowerCase of I
            typeList.add("you");
            typeList.add("he");
            typeList.add("she");
            typeList.add("it");
            typeList.add("we");
            typeList.add("they");
            typeList.add("what");
            typeList.add("who");

            return typeList;
        }
    },
    OBJECTIVE {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("me");
            typeList.add("him");
            typeList.add("her");
            typeList.add("it");
            typeList.add("us");
            typeList.add("you");
            typeList.add("them");
            typeList.add("whom");
            return typeList;
        }
    },
    POSSESSIVE {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("mine");
            typeList.add("yours");
            typeList.add("his");
            typeList.add("her");
            typeList.add("ours");
            typeList.add("theirs");
            return typeList;
        }
    },
    DEMONSTRATIVE {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("this");
            typeList.add("that");
            typeList.add("these");
            typeList.add("those");
            return typeList;
        }
    },
    INTERROGATIVE {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("who");
            typeList.add("whom");
            typeList.add("which");
            typeList.add("what");
            typeList.add("whose");
            typeList.add("whoever");
            typeList.add("whatever");
            typeList.add("whichever");
            typeList.add("whomever");
            return typeList;
        }
    },
    RELATIVE {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("who");
            typeList.add("whom");
            typeList.add("whose");
            typeList.add("which");
            typeList.add("that");
            typeList.add("what");
            typeList.add("whatever");
            typeList.add("whoever");
            typeList.add("whomever");
            typeList.add("whichever");
            return typeList;
        }
    },
    REFLEXIVE_INTENSIVE {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("myself");
            typeList.add("yourself");
            typeList.add("himself");
            typeList.add("herself");
            typeList.add("itself");
            typeList.add("ourselves");
            typeList.add("themselves");
            return typeList;
        }
    },
    RECIPROCAL {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("each other");
            typeList.add("one another");
            return typeList;
        }
    },
    INDEFINITE {
        public List<String> getPronounList() {
            List<String> typeList = new ArrayList<String>();
            typeList.add("anything");
            typeList.add("everybody");
            typeList.add("another");
            typeList.add("each");
            typeList.add("few");
            typeList.add("many");
            typeList.add("none");
            typeList.add("some");
            typeList.add("all");
            typeList.add("any");
            typeList.add("anybody");
            typeList.add("anyone");
            typeList.add("everyone");
            typeList.add("everything");
            typeList.add("no one");
            typeList.add("nobody");
            typeList.add("nothing");
            typeList.add("none");
            typeList.add("other");
            typeList.add("others");
            typeList.add("several");
            typeList.add("somebody");
            typeList.add("someone");
            typeList.add("something");
            typeList.add("most");
            typeList.add("enough");
            typeList.add("little");
            typeList.add("more");
            typeList.add("both");
            typeList.add("either");
            typeList.add("neither");
            typeList.add("one");
            typeList.add("much");
            typeList.add("such");
            return typeList;
        }
    }
    ;

    /**
     * @return pronoun list
     */
    public abstract List<String> getPronounList();
}
