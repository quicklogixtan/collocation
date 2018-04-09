

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInput;
import java.util.List;

public class Collocation {
    private LexicalizedParser lp = null;
    private JSONArray arrNoun;
    private JSONArray dictionary;
    public Collocation() {
        loadModel();
        loadWords();
        filterNoun();
       /* for (Object o : arrNoun) {
            JSONObject object = (JSONObject) o;
            if (object.get("term").equals("consumption")) {
                System.out.println(object);
            }
        }*/
    }

    public static void main(String[] args) {
        Collocation collocation= new Collocation();
        JSONArray ketqua1=collocation.findFromExamples("accelerate");
        System.out.println(ketqua1);
        JSONArray ketqua=collocation.findCollocation("accelerate");
        System.out.println(ketqua);

    }

    public void filterNoun() {
        arrNoun= new JSONArray();
        for (Object o :dictionary){
            JSONObject object = (JSONObject) o;
            String pos = (String) object.get("pos");
            if (pos.contains("noun")) {
                arrNoun.add(object);
            }
        }
    }

    public JSONArray findCollocation(String verb) {
        JSONArray result = new JSONArray();
        for (Object o : arrNoun) {
            JSONObject aWord = (JSONObject) o;
            JSONArray arrCollocation = (JSONArray) aWord.get("collocation");
            for (Object o1 : arrCollocation) {
                JSONObject aRela = (JSONObject) o1;
                String relation = (String) aRela.get("relation");
                if (relation.contains("VERB")) {
                    JSONArray words = (JSONArray) aRela.get("words");
                    for (Object word : words) {
                        String s = (String) word;
                        if (s.matches(".*\\b"+verb+"\\b.*")) {
                            JSONObject aResult= new JSONObject();
                            aResult.put("term", aWord.get("term"));
                            aResult.put("type", "noun");
//                            result.add(aWord.get("term"));
                            result.add(aResult);
                            break;
                        }
                    }
                }
            }

        }
        return result;
    }
    public void loadWords() {
        File file = null;
        WordExtractor extractor = null;
        try
        {

            FileInputStream excelFile = new FileInputStream(new File(String.valueOf(Collocation.class.getResource("/Oxford-Collocatio-Dictionary.doc").getFile())));
           // file = new File(String.valueOf(Collocation.class.getResource("/QuydinhTT.doc").getFile()));
            file = new File(String.valueOf(Collocation.class.getResource("/collocation.doc").getFile()));
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument document = new HWPFDocument(fis);
            Range range = document.getRange();

            int paraNum = range.numParagraphs();
            JSONArray _dictionary =new JSONArray();
            JSONObject word=null;
            JSONArray arrRelatedWord= new JSONArray();
            for (int i = 0; i < paraNum; i++) {
                Paragraph curPara = range.getParagraph(i);
                if (isTermPara(curPara) || i==paraNum-1) {
                    if (word != null) {
                        word.put("collocation", arrRelatedWord);
                        _dictionary.add(word);
                    }
                    word = new JSONObject();
                    arrRelatedWord=new JSONArray();
                    word.put("term", curPara.getCharacterRun(0).text().trim().toLowerCase());
                    word.put("pos", curPara.getCharacterRun(1).text().trim().toLowerCase());
                } else {
                    JSONArray aCollation = collocationConstruction(curPara);
                    for (Object o : aCollation) {
                        arrRelatedWord.add(o);
                    }
                }
            }
            this.dictionary = _dictionary;
//            JSONObject a = (JSONObject) _dictionary.get(_dictionary.size()-1);
//
//            System.out.println(a);
           /* JSONArray ketqua = collocationConstruction(range.getParagraph(7));
            System.out.println(ketqua);*/

            /*Paragraph parContainer = range.getParagraph(8);
            System.out.println(parContainer.text());
            int tan = parContainer.numCharacterRuns();
            System.out.println(tan);
            CharacterRun run = parContainer.getCharacterRun(4);
            System.out.println(run.text());
            System.out.println(run.isBold());
            System.out.println(run.getUnderlineCode());
            System.out.println(run.getColor());
            System.out.println(run.isItalic());*/
        }
        catch (Exception exep)
        {
            exep.printStackTrace();
        }

    }
    private boolean isTermPara(Paragraph paragraph) {
        if (paragraph.numCharacterRuns() == 2) {
            CharacterRun firstRun = paragraph.getCharacterRun(0);
            CharacterRun secondRun = paragraph.getCharacterRun(1);
            return firstRun.isBold() && firstRun.getColor() == 2 && firstRun.getUnderlineCode() == 0 && secondRun.getUnderlineCode() == 0 && !secondRun.isBold() && secondRun.getColor() == 1;
        } else if (paragraph.numCharacterRuns()>2){
            CharacterRun firstRun = paragraph.getCharacterRun(0);
            CharacterRun secondRun = paragraph.getCharacterRun(1);
            CharacterRun thirdRun = paragraph.getCharacterRun(2);
           /* if (firstRun.isBold() && firstRun.getColor() == 2 && firstRun.getUnderlineCode() == 0 && secondRun.getUnderlineCode() == 0 && !secondRun.isBold() && secondRun.getColor() == 1 && thirdRun.isBold()) {
                System.out.println(paragraph.text());
            }*/

            return firstRun.isBold() && firstRun.getColor() == 2 && firstRun.getUnderlineCode() == 0 && secondRun.getUnderlineCode() == 0 && !secondRun.isBold() && secondRun.getColor() == 1  ;

        }
        return false;
    }

    private JSONArray collocationConstruction(Paragraph paragraph) {
        if (paragraph.numCharacterRuns() < 2) {
            return new JSONArray();
        }
        JSONArray result = new JSONArray();
        JSONArray arrRelatedWord= new JSONArray();
        JSONArray arrExamples = new JSONArray();
        int numRun = paragraph.numCharacterRuns();
        JSONObject aCollocation = null;
        boolean inExampleRun = false;
        for (int i = 0; i < numRun; i++) {
            CharacterRun run = paragraph.getCharacterRun(i);
            if (run.isBold() && run.getUnderlineCode() == 1 && run.getColor() == 6) {
                if (aCollocation!=null) {
                    aCollocation.put("words", arrRelatedWord);
                    aCollocation.put("examples", arrExamples);
                    result.add(aCollocation);
                }
                aCollocation= new JSONObject();
                arrRelatedWord=new JSONArray();
                arrExamples=new JSONArray();
                aCollocation.put("relation", run.text().trim());
                inExampleRun = false;
            }
            if (run.isBold() && run.getUnderlineCode() == 0 && run.getColor() == 2) {
                arrRelatedWord.add(run.text().trim());
                inExampleRun = false;
            }
            if (!run.isBold() && run.getUnderlineCode() == 0 && run.getColor() == 1 && run.isItalic()) {
//                arrExamples.add(run.text().trim());
                if (inExampleRun) {
                    if (arrExamples.size() > 0) {
                        int indexOfLast = arrExamples.size() - 1;
                        String lastExample = (String) arrExamples.remove(indexOfLast);
                        arrExamples.add(lastExample + " " + run.text().trim());
                    }
                } else {
                    arrExamples.add(run.text().trim());
                }
                inExampleRun = true;
            }
        }
        if (aCollocation != null) {
            aCollocation.put("words", arrRelatedWord);
            aCollocation.put("examples", arrExamples);
            result.add(aCollocation);
        }

        return result;

    }

    public JSONArray findFromExamples(String verb) {
        JSONArray result = new JSONArray();
        JSONObject verbDef = getVebDef(verb);
        if (verbDef == null) {
            return result;
        }
        JSONArray collocations = (JSONArray) verbDef.get("collocation");
        for (Object collocation : collocations) {
            JSONObject aCollocation = (JSONObject) collocation;
            JSONArray examples = (JSONArray) aCollocation.get("examples");
            for (Object example : examples) {
                String ex = (String) example;
                System.out.println(ex);
//                lp.parse(ex).pennPrint();
//                List<String> aResult = Util.getNounsFromSentences(ex,verb);
                  JSONArray aResult = Util.getDependentNouns(ex,verb);
                for (Object oString : aResult) {
                        result.add(oString);
                }
            }
            /*List<String> haha=Util.getNouns(lp.parse("I have many children, His attacker had punched him"), "punch");
            System.out.println(lp.parse("She is a student who I love"));
            System.out.println(haha);*/

        }
        return result;
    }

    private JSONObject getVebDef(String verb) {
        for (Object o :dictionary){
            JSONObject object = (JSONObject) o;
            String pos = (String) object.get("pos");
            String term = (String) object.get("term");
            if  (term.equals(verb.toLowerCase()) && pos.contains("verb")) {
                return object;
            }
        }
        return null;
    }
    private void loadModel() {
        lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        lp.setOptionFlags(new String[] { "-maxLength", "80", "-retainTmpSubcategories" });
        System.out.println("done load model");
    }

}
