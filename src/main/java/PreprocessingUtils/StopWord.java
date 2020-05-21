package PreprocessingUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class StopWord {
    private Set<String> StopDict = new HashSet<String>();

    public void Init() throws Exception{
        String dicPath = this.getClass().getClassLoader().getResource("stopwords3732.txt").getPath();//TODO 3732个停用词词典
        String line = null;
        long time = System.currentTimeMillis();
        BufferedReader br;

        br = new BufferedReader(new InputStreamReader( new FileInputStream(dicPath),"UTF-8"));
        while((line = br.readLine()) != null){
            line = line.trim();
            if(line.isEmpty())
                continue;
            StopDict.add(line);
        }
        br.close();


        System.out.println("读取停词字典 耗时：" + (System.currentTimeMillis() - time) + "ms");
        System.out.println("停词词典大小：" + StopDict.size());
    }

    public Vector<String> RemoveOneLine(Vector<String> strings){
        Vector<String> afterRemove_list = new Vector<String>();

        for(String s:strings){
            if(StopDict.contains(s)){
                continue;
            }
            afterRemove_list.add(s);
        }
        return afterRemove_list;
    }

    public Vector<Vector<String>> RemoveStopWords(Vector<Vector<String>> strings){
        Vector<Vector<String>> result = new Vector<Vector<String>>();
        for(Vector<String> line:strings){
            Vector<String> afterR = RemoveOneLine(line);
            result.add(afterR);
        }
        return  result;
    }
}
