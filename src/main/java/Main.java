import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        Collocation collocation=new Collocation();
        try {
            JSONArray conclusion=new JSONArray();
            File f = new File(Main.class.getResource("/verb-list.txt").getFile());
            BufferedReader b = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = b.readLine()) != null) {
                JSONObject aVerb=new JSONObject();
                line = line.trim();
                aVerb.put("verb", line);
                JSONArray related= mergeResults(collocation.findCollocation(line),collocation.findFromExamples(line));
                aVerb.put("related", related);
                conclusion.add(aVerb);
            }
            printToFile(conclusion,"/home/minhtan/Desktop/ketqua.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printToFile(JSONArray content,String path) {
        try {
            FileWriter file = new FileWriter(path);
            file.write(content.toJSONString());
            System.out.println("Successfully copied JSON to file");
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray mergeResults(JSONArray result1, JSONArray result2) {
        for (Object o : result2) {
            result1.add(o);
        }
        return result1;
    }
}
