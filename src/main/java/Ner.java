import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.List;

public class Ner {
    public static void main(String[] args) {
        String input = "Good afternoon Rajat Raina, how are you today?";
        String word = "you";
        getNER(input,word);
    }

    public static void getNER(String s, String word) {
        CRFClassifier ner = CRFClassifier.getClassifierNoExceptions("classifiers/ner-eng-ie.crf-3-all2008.ser.gz");
        List a = ner.classify(s);
        System.out.println(a.get(0) instanceof CoreLabel);
        for (Object o : a) {
            System.out.println(o);
        }
    }
}
