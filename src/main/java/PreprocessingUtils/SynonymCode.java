package PreprocessingUtils;

import NlpSystem.domain.Corpus;
import NlpSystem.domain.WordCode;
import com.sun.jmx.remote.internal.ArrayQueue;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class SynonymCode {
    private Map<WordCode, ArrayList<String>> synonym = new HashMap<>();

    public void init() throws  Exception{
        String dicPath = this.getClass().getClassLoader().getResource("HIT-IRLab.txt").getPath();//TODO 资源： 300k行的词典
        String line = null;
        long time = System.currentTimeMillis();
        BufferedReader br;

        br = new BufferedReader(new InputStreamReader( new FileInputStream(dicPath),"UTF-8"));
        while((line = br.readLine()) != null){
            line = line.trim();
            String[] words = line.split(" ");
            WordCode wordCode = new WordCode(words[0]);
            ArrayList<String> arrayList = new ArrayList<>();
            for(int i = 1;i<words.length;i++){
                arrayList.add(words[i]);
            }
            synonym.put(wordCode,arrayList);
        }
        br.close();

        System.out.println("读取同义词字典件 耗时：" + (System.currentTimeMillis() - time) + "ms");
        System.out.println("字典大小：" + synonym.size());
    }

    public WordCode getWordCode(String words){
        WordCode wordCode = new WordCode("Zz00Z00@");

        for(Map.Entry<WordCode,ArrayList<String>> entry:synonym.entrySet()){
            for(String  s:entry.getValue()){
                if(words.equals(s)) {
                    wordCode = entry.getKey();
                    break;
                }
            }
        }
        wordCode.setInitWord(words);
        return wordCode;
    }

    public double CalcDistance(Vector<WordCode> question,Vector<WordCode> DataLine){
        double FinalScore = 0.0;

        Vector<WordCode> flag = new Vector<WordCode>();

        for(int i = 0;i<DataLine.size();i++){
            flag.add(new WordCode("Zz00Z00@"));
        }

        Queue<WordCode> quesQueue = new LinkedList<WordCode>();

        for(WordCode wordCode:question){
           quesQueue.add(wordCode);
        }


        while(!quesQueue.isEmpty()){
            Map<Integer,Double> record = new HashMap<Integer, Double>();
            WordCode quesCode = quesQueue.poll();

            for(int i = 0;i<DataLine.size();i++){
                //TODO 考虑Zz的情况
                if(DataLine.get(i).getTopLevel() == 'Z'|| quesCode.getTopLevel() == 'Z'){
                    if(DataLine.get(i).getInitWord().equals(quesCode.getInitWord())){
                        record.put(i,5.5);
                    }else{
                        record.put(i,0.0);
                    }
                    continue;
                }
                double score = quesCode.Minus(DataLine.get(i));
                record.put(i,score);
            }

            List<Map.Entry<Integer,Double>> list = new ArrayList<Map.Entry<Integer,Double>>(record.entrySet()); //转换为list
            Collections.sort(list, new Comparator<Map.Entry<Integer,Double>>() {
                @Override
                public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            for(int i = 0;i<list.size();i++){
                if(list.get(i).getValue()>0){
                    if(flag.get(list.get(i).getKey()).getTopLevel() == 'Z'){//还没有定好近似词
                        flag.set(list.get(i).getKey(),quesCode);
                        break;
                    }else{
                        WordCode flagWord = flag.get(list.get(i).getKey());//flag之前确定好的近义词
                        WordCode DataWord = DataLine.get(list.get(i).getKey());//匹配串里和flag对应的当前词
                        WordCode currentWord = quesCode;//现在确定quesCode和Dataword相近,判断是flagword和current谁更加和Data相似
                        if(flagWord.Minus(DataWord)>=currentWord.Minus(DataWord)){//flag里的词更近，current只能退而求其次
                            continue;
                        }else{
                            flag.set(list.get(i).getKey(),currentWord);//替换掉原本的词
                            quesQueue.add(flagWord);//原本的flagword弹出，重新寻找相近的词匹配
                            break;
                        }
                    }
                }
            }

            //遍历完一遍了，计算匹配度

        }

        for(int i = 0;i<DataLine.size();i++){
            if(flag.get(i).getTopLevel() == 'Z'){
                FinalScore -=1;
                continue;
            }
            FinalScore+= DataLine.get(i).Minus(flag.get(i));
        }

        if(FinalScore == DataLine.size() *16.5 && FinalScore == question.size() * 16.5)
            return 9999;
        return FinalScore;
    }

    public Corpus GenerateDircCode(Corpus corpus){
        Corpus corpus1 = corpus;
        Vector<Vector<WordCode>> dirc = new Vector<Vector<WordCode>>();
        for(Vector<String> Line:corpus1.getDirc()){
            Vector<WordCode> code = new Vector<WordCode>();
            for(String s:Line){
                code.add(getWordCode(s));
            }
            dirc.add(code);
        }
        corpus1.setDircCode(dirc);
        return corpus1;
    }
}
