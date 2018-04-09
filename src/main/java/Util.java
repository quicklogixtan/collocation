import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by YATAOME on 03/26/2018.
 */
public class Util {
    public static void main(String[] args) {
//        System.out.println(getDependentNouns("Her name is Ha. She is my teacher","is"));
        loadCoreNLP();
    }
    static StanfordCoreNLP pipeline = null;

    private static void loadCoreNLP() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, parse, lemma");
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,mention,coref");
        pipeline = new StanfordCoreNLP(props);
//TODO
 /*       //Annotation document=new Annotation("The dogs are eating shit. Tan shoo them away");
        Annotation document=new Annotation("Barack Obama was born in Hawaii.  He is the president. Obama was elected in 2008.");
        pipeline.annotate(document);
        *//*for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
            System.out.println("\t" + cc);
        }*//*
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("---");
            System.out.println("mentions");
            for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
                System.out.println("\t" + m);
            }
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                System.out.println(token.index());
                System.out.println(ne);
            }
        }*/


    }

    private static List<String> getDependenciesList(String sentence,String verb) {
        List<String> depenWords = new ArrayList<String>();
        Morphology morphology=new Morphology();
        Annotation document = new Annotation(sentence);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap s: sentences) {
            SemanticGraph dependencies = s.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//            System.out.println(dependencies);
            for (SemanticGraphEdge e : dependencies.edgeIterable()) {
                CoreLabel coreLabe = e.getGovernor().backingLabel();
                System.out.println(e.getGovernor());
                System.out.println(coreLabe.tag());
                /*System.out.printf("%s(%s-%d, %s-%d)%n", e.getRelation().toString(), e.getGovernor().word(),
                        e.getGovernor().index(), e.getDependent().word(), e.getDependent().index());*/
                if (e.getGovernor().word() != null && morphology.stem(e.getGovernor().word()).equals(verb)) {
                    depenWords.add(e.getDependent().word());
                }else if (e.getDependent().word() != null && morphology.stem(e.getDependent().word()).equals(verb)) {
                    depenWords.add(e.getGovernor().word());
                }
            }
        }
        return depenWords;
    }

    public static JSONArray getDependentNouns(String sentence,String verb) {

        loadCoreNLP();
        //These are two result
        JSONArray result = new JSONArray();

        Morphology morphology=new Morphology();//Used for stem word
        verb = morphology.stem(verb);
        Annotation document = new Annotation(sentence);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap setence : sentences) {
            SemanticGraph dependencies = setence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            SemanticGraphEdge special_one_another_reciprocal = null;
            SemanticGraphEdge special_each_other_reciprocal = null;

            for (SemanticGraphEdge edge : dependencies.edgeIterable()) {
                /*System.out.printf("%s(%s-%d, %s-%d)%n", edge.getRelation().toString(), edge.getGovernor().word(),
                        edge.getGovernor().index(), edge.getDependent().word(), edge.getDependent().index());*/
//                System.out.println(edge.getDependent().tag());
                if (edge.getGovernor().word() != null && morphology.stem(edge.getGovernor().word()).equals(verb) && edge.getGovernor().backingLabel().tag().startsWith("VB")) {
                    if (edge.getDependent().backingLabel().tag().startsWith("NN")) {
                        JSONObject aNoun=new JSONObject();
                        aNoun.put("type", "noun");
                        aNoun.put("term", edge.getDependent().word());
                        result.add(aNoun);
                    } else if (edge.getDependent().backingLabel().tag().startsWith("PR")) {
                        JSONObject aPronoun = new JSONObject();
                        aPronoun.put("type", "pronoun");
                        aPronoun.put("term", edge.getDependent().word());
                        String tag = Pronoun.getPronounName(Pronoun.checkPronounType(edge.getDependent().word()),edge.getRelation().toString());
                        aPronoun.put("tag", tag);
                        result.add(aPronoun);
                    } else if (edge.getDependent().word().equals("other")) {
                        special_each_other_reciprocal = edge;
                    } else if (edge.getGovernor().word().equals("one")) {
                        special_one_another_reciprocal = edge;
                    }
                }else if (edge.getDependent().word() != null && morphology.stem(edge.getDependent().word()).equals(verb) && edge.getDependent().backingLabel().tag().startsWith("VB") ) {
                    if (edge.getGovernor().backingLabel().tag().startsWith("NN")) {
                        JSONObject aNoun = new JSONObject();
                        aNoun.put("type", "noun");
                        aNoun.put("term", edge.getGovernor().word());

                        result.add(aNoun);
                    } else if (edge.getGovernor().backingLabel().tag().startsWith("PR")) {
                        JSONObject aPronoun = new JSONObject();
                        aPronoun.put("type", "pronoun");
                        aPronoun.put("term", edge.getGovernor().word());
                        String tag = Pronoun.getPronounName(Pronoun.checkPronounType(edge.getGovernor().word()),edge.getRelation().toString());
                        System.out.println(edge.getRelation().toString());
                        if (edge.getRelation().toString().equals("nsubj")) {
                            aPronoun.put("tag",tag);
                        }
                        result.add(aPronoun);
                    }
                }
            }

        }
        System.out.println(result);
        return result;
    }

    private static boolean isInAuxDepen(List<SemanticGraphEdge> auxList,String word,int indexOfWord) {
        for (SemanticGraphEdge graphEdge : auxList) {
            if (graphEdge.getGovernor().word() == word && graphEdge.getGovernor().index() == indexOfWord) {
                return true;
            }
        }
        return false;
    }

    private static List<SemanticGraphEdge> filterAux(SemanticGraph dependencies) {
        List<SemanticGraphEdge> result =new ArrayList<SemanticGraphEdge>();
        for (SemanticGraphEdge edge : dependencies.edgeIterable()) {
            if (edge.getRelation().toString().startsWith("aux")) {
                result.add(edge);
            }
        }
        return result;
    }
    public static JSONArray getNounsFromSentences(String paragraph,String verb) {
        /*LexicalizedParser lp = LexicalizedParser.loadModel();
        lp.setOptionFlags(new String[] { "-maxLength", "80", "-retainTmpSubcategories" });
//        String sent = "He was repeatedly kicked and punched as he lay on the ground.";//INPUT HERE
        Tree parse = lp.parse(paragraph);*/
        loadCoreNLP();

        JSONArray result = new JSONArray();
        MaxentTagger tagger = new MaxentTagger(MaxentTagger.DEFAULT_JAR_PATH);

        Morphology morphology = new Morphology();
        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(paragraph));
        if (sentences.size() > 1) {
            System.out.println("Sentence " + paragraph + " co > 1 cau");
        }
        for (List<HasWord> aSent : sentences) {
            List<TaggedWord> taggedSent = tagger.tagSentence(aSent);
            String sentenceString = SentenceUtils.listToString(aSent);
            List<String> depenWords = getDependenciesList(sentenceString, verb);

            for (TaggedWord taggedWord : taggedSent) {
                if (taggedWord.tag().startsWith("NN")) {
                    if (depenWords.contains(taggedWord.word())) {
                        result.add(taggedWord.word());
                    }

                }
            }
        }
        System.out.println(result);
        return result;
    }
    public static JSONArray getNounsFromSentencesV2(String paragraph,String verb) {
        LexicalizedParser lp = LexicalizedParser.loadModel();
        lp.setOptionFlags(new String[] { "-maxLength", "80", "-retainTmpSubcategories" });
//        String sent = "He was repeatedly kicked and punched as he lay on the ground.";//INPUT HERE
        Tree parse = lp.parse(paragraph);
        List<String> depenWords = getDependentWord(parse, verb);

        JSONArray result = new JSONArray();

        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(paragraph));
        if (sentences.size() > 1) {
            System.out.println("Sentence " + paragraph + " co > 1 cau");
        }
        for (List<HasWord> aSent : sentences) {
            String sentenceString = SentenceUtils.listToString(aSent);
            Tree root = lp.parse(sentenceString);
            List<Tree> nouns = getNounsInSentence(root);
            for (Tree noun : nouns) {
                if (depenWords.contains(noun.label().value()) || isWordInside(getClauseFromTreeOfWord(root,noun),verb)) {
                    result.add(noun.label().value());
                }
            }
        }
        System.out.println(result);
        return result;
    }

    public static List<String> getNouns(Tree root, String word) {
        /*Tree treeOfWord = getTreeOfWord(root, "punched");
        if (treeOfWord != null) {
            Tree clauseTree = getClauseFromTreeOfWord(root, treeOfWord);
            System.out.println(clauseTree);
            List<String> result = getNounsInSentence(clauseTree);
            System.out.println(result);
        }*/
        List<String> result = new ArrayList<String>();
        List<Tree> nouns = getNounsInSentence(root);
        for (Tree noun : nouns) {
            Tree clause = getClauseFromTreeOfWord(root, noun);
            if (clause != null && isWordInside(clause, word)) {
                result.add(noun.toString());
            }
        }
        return result;
    }

    private static boolean isWordInside(Tree tree, String word) {
        Morphology morphology = new Morphology();
        for (Tree aTree : tree.getLeaves()) {
            if (morphology.stem(aTree.label().toString()).equals(word)) {
                return true;
            }
        }
        return false;
    }

    private static Tree getClauseFromTreeOfWord(Tree root, Tree wordTree) {
        Tree parent = wordTree.parent(root);
        if (parent == null) {
            return null;
        }
        String labelString = parent.label().toString();
        if (labelString.equals("ROOT") || labelString.startsWith("SBAR") || labelString.equals("SINV")) {
            return parent;
        } else {
            return getClauseFromTreeOfWord(root, parent);
        }
    }

    private static List<Tree> getNounsInSentence(Tree root) {
        List<Tree> result = new ArrayList<Tree>();
        for (Tree o : root.getLeaves()) {
            Tree parent = o.parent(root);
            if (parent.label().toString().equals("NN") || parent.label().toString().equals("NNS")) {
                result.add(o);
            }
        }
        return result;
    }

    public static List<String> getDependentWord(Tree parse, String word) {

        List<String> depenWords = new ArrayList<String>();
        Morphology morphology = new Morphology();
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
        for (TypedDependency typedDependency : tdl) {
            System.out.println(typedDependency);
            if (typedDependency.gov().word() != null && morphology.stem(typedDependency.gov().word()).equals(word)) {
                depenWords.add(typedDependency.dep().word());
            }else if (typedDependency.dep().word() != null && morphology.stem(typedDependency.dep().word()).equals(word)) {
                depenWords.add(typedDependency.gov().word());
            }
        }
        return depenWords;
    }

}
