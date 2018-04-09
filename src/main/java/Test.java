import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
//import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.trees.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


//Just for test. Pls ignore this file.
public class Test {
    static LexicalizedParser lp = null;
    public static void loadModel() {
        lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        lp.setOptionFlags(new String[] { "-maxLength", "80", "-retainTmpSubcategories" });
        System.out.println("done load model");
        //TODO
        /*String text = "His attacker had punched him hard in the face.";
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(text);*/
    }
    public static void main(String[] args) {
        //getDependencies();

        /*MaxentTagger tagger=new MaxentTagger(MaxentTagger.DEFAULT_JAR_PATH);
        List<HasWord> sent =MaxentTagger.tokenizeText(new StringReader("I have many children")).get(0);
        System.out.println(MaxentTagger.tokenizeText(new StringReader("I have a table; I love you")).size());

        Morphology morphology = new Morphology();
        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader("He was repeatedly kicked and punched as he lay on the ground."));
        System.out.println(sentences.size());
        for (List<HasWord> aSent:sentences) {
            List<TaggedWord> taggedSent = tagger.tagSentence(aSent);
            for (TaggedWord taggedWord : taggedSent) {
                if (taggedWord.tag().startsWith("NN")) {
                    System.out.println(taggedWord.word());
                }
            }
        }*/
        Morphology morphology=new Morphology();
        System.out.println(morphology.stem("punching"));
        String input = "The two politicians hate each other.";
        //String input = "Barack Obama was born in Hawaii.  He is the president. Obama was elected in 2008.";
        String input1 = "Don't take any notice? She is just putting on an act!";
        String input2 = "He was repeatedly kicked and punched. Then he lay on the ground";

//        String input = "I have a pen. I have many children";
        loadModel();
//        Tree aTree = lp.parse(input4);
        //lp.parse(input1).pennPrint();
        Util.getDependentNouns(input,"hate");
        //Util.getNounsFromSentencesV2(input,"clean");

        //System.out.println(aTree);

/*        Test tan = new Test();
        tan.getNouns(aTree,"punch");*/
    }

    public void getNouns(Tree root,String word) {
        /*Tree treeOfWord = getTreeOfWord(root, "punched");
        if (treeOfWord != null) {
            Tree clauseTree = getClauseFromTreeOfWord(root, treeOfWord);
            System.out.println(clauseTree);
            List<String> result = getNounsInSentence(clauseTree);
            System.out.println(result);
        }*/
        List<String> result=new ArrayList<String>();
        List<Tree> nouns = getNounsInSentence(root);
        for (Tree noun : nouns) {
            Tree clause = getClauseFromTreeOfWord(root,noun);
            if (isWordInside(clause,word)) {
                result.add(noun.toString());
            }
        }
        System.out.println(result);
    }

    private boolean isWordInside(Tree tree,String word) {
        Morphology morphology=new Morphology();
        for (Tree aTree : tree.getLeaves()) {
            if (morphology.stem(aTree.label().toString()).equals(word)) {
                return true;
            }
        }
        return false;
    }

    private Tree getClauseFromTreeOfWord(Tree root, Tree wordTree) {
        Tree parent= wordTree.parent(root);
        if (parent == null) {
            return null;
        }
        String labelString= parent.label().toString();
        if (labelString.equals("S") || labelString.equals("SBAR")) {
            return parent;
        } else {
            return getClauseFromTreeOfWord(root, parent);
        }
    }

    private List<Tree> getNounsInSentence(Tree root) {
        List<Tree> result=new ArrayList<Tree>();
        for (Tree o : root.getLeaves()){
            Tree parent = o.parent(root);
            if (parent.label().toString().equals("NN")) {
                result.add(o);
            }
        }
        return result;
    }

    public static void getDependencies() {
        LexicalizedParser lp = LexicalizedParser.loadModel();
        lp.setOptionFlags(new String[] { "-maxLength", "80", "-retainTmpSubcategories" });
//        String sent = "He was repeatedly kicked and punched as he lay on the ground.";//INPUT HERE
        String sent = "Kannan has two children";
        Tree parse = lp.parse(sent);
        parse.pennPrint();
        System.out.println();

        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
//        System.out.println(tdl);
        for (TypedDependency typedDependency : tdl) {
            System.out.println(typedDependency);
            System.out.println(typedDependency.gov().word());
            System.out.println(typedDependency.dep().word());
        }
        TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
        //tp.printTree(parse);

        Collection<TypedDependency> td = gs.typedDependenciesCollapsed();
        //System.out.println(td);

        Object[] list = td.toArray();
        System.out.println(list.length);
        TypedDependency typedDependency;
        for (Object object : list) {
            typedDependency = (TypedDependency) object;
            System.out.println(
                    "Depdency Name " + typedDependency.dep().toString() + " :: " + typedDependency.reln());
            if (typedDependency.reln().getShortName().equals("something")) {
                // your code
            }
        }
    }


}
