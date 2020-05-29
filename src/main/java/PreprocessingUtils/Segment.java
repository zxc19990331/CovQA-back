package PreprocessingUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Segment {
      private Set<String> segDict = new HashSet<String>();

    public void Init()throws  Exception{
        String dicPath = this.getClass().getClassLoader().getResource("300.txt").getPath();//TODO 资源： 300k行的词典
        String line = null;
        long time = System.currentTimeMillis();
        BufferedReader br;

        br = new BufferedReader(new InputStreamReader( new FileInputStream(dicPath),"UTF-8"));
        while((line = br.readLine()) != null){
            line = line.trim();
            if(line.isEmpty())
                continue;
            segDict.add(line);
        }
        br.close();

        System.out.println("读取字典件 耗时：" + (System.currentTimeMillis() - time) + "ms");
        System.out.println("字典大小：" + segDict.size());
    }

    /**
     * 前向算法分词
     * segDict 分词词典
     * phrase 待分词句子
     * */
    private Vector<String> FMM2(String  phrase){
        int maxlen = 6;
        Vector<String> fmm_list = new Vector<String>();
        int len_phrase = phrase.length();
        int i=0,j=0;

        while(i < len_phrase){
            int end = i+maxlen;
            if(end >= len_phrase)
                end = len_phrase;
            String phrase_sub = phrase.substring(i, end);
            for(j = phrase_sub.length(); j >=0; j--){
                if(j == 1)
                    break;
                String key =  phrase_sub.substring(0, j);
                if(segDict.contains(key)){
                    fmm_list.add(key);
                    i +=key.length() -1;
                    break;
                }
            }
            if(j == 1)
                fmm_list.add(""+phrase_sub.charAt(0));
            i+=1;
        }
        return fmm_list;
    }

    /**
     * 后向算法分词
     * */
    private Vector<String> BMM2( String  phrase){
        int maxlen = 6;
        Vector<String> bmm_list = new Vector<String>();
        int len_phrase = phrase.length();
        int i=len_phrase,j=0;

        while(i > 0){
            int start = i - maxlen;
            if(start < 0)
                start = 0;
            String phrase_sub = phrase.substring(start, i);
            for(j = 0; j < phrase_sub.length(); j++){
                if(j == phrase_sub.length()-1)
                    break;
                String key =  phrase_sub.substring(j);
                if(segDict.contains(key)){
                    bmm_list.insertElementAt(key, 0);
                    i -=key.length() -1;
                    break;
                }
            }
            if(j == phrase_sub.length() -1)
                bmm_list.insertElementAt(""+phrase_sub.charAt(j), 0);
            i -= 1;
        }
        return bmm_list;
    }


    public Vector<String> BMM_FMMSegment( String phrase){
        Vector<String> fmm_list = FMM2(phrase);
        Vector<String> bmm_list = BMM2(phrase);
        //如果正反向分词结果词数不同，则取分词数量较少的那个
        if(fmm_list.size() != bmm_list.size()){
            if(fmm_list.size() > bmm_list.size())
                return bmm_list;
            else return fmm_list;
        }
        //如果分词结果词数相同
        else{
            //如果正反向的分词结果相同，就说明没有歧义，可返回任意一个
            int i ,FSingle = 0, BSingle = 0;
            boolean isSame = true;
            for(i = 0; i < fmm_list.size();  i++){
                if(!fmm_list.get(i).equals(bmm_list.get(i)))
                    isSame = false;
                if(fmm_list.get(i).length() ==1)
                    FSingle +=1;
                if(bmm_list.get(i).length() ==1)
                    BSingle +=1;
            }
            if(isSame)
                return fmm_list;
            else{
                //分词结果不同，返回其中单字较少的那个
                if(BSingle < FSingle)
                    return fmm_list;
                else return bmm_list;
            }
        }
    }

    public Vector<Vector<String>> BilateralSegment(Vector<String> phrases){
        Vector<Vector<String>> Results = new Vector<Vector<String>>();

        for(String s:phrases){
            Vector<String> line = BMM_FMMSegment(s);
            Results.add(line);
        }
        return Results;
    }

}
