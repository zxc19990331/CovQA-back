package server;

import NlpSystem.NlpModel;
import NlpSystem.domain.WordCode;
import PreprocessingUtils.Segment;
import SpiderUtils.Spider;
import dao.JDBCDAO;
import entity.DataResult;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class MainServer {
    public static DataResult getAnswer(String question){
        NlpModel nlpModel =new NlpModel();
        String answer="";
        try{
            //TODO 这里不用Init了
           // nlpModel.Init();
            answer=nlpModel.getAnswers(question);
        }catch (Exception e){e.printStackTrace();}
        if(answer.isEmpty())
            return DataResult.success("success","我什么也不知道");
        else
            return DataResult.success("success",answer);
    }

    public static DataResult updateDB(){
        NlpModel nlpModel = new NlpModel();
        long startTime=System.currentTimeMillis();
        int maxPage=3;
        try {
            nlpModel.Init();
            ArrayList<ArrayList<String>>questionAndAnswer=Spider.getQuestionAndAnswer();
            Spider.spiderQuestionAndAnswer(maxPage);
            ArrayList<ArrayList<String>> codeQuestionAnswer = new ArrayList<>();
            for(ArrayList<String> temp:questionAndAnswer){
               ArrayList<String> questionCode = nlpModel.getStringCode(temp.get(0));
               StringBuilder stringBuilder = new StringBuilder();
               for(String s:questionCode){
                   stringBuilder.append(s + " ");
               }
               ArrayList<String> tempCode = new ArrayList<>();
                tempCode.add(stringBuilder.toString());
                tempCode.add(temp.get(0));
                tempCode.add(temp.get(1));
                codeQuestionAnswer.add(tempCode);
            }
            JDBCDAO.autoUpdateDB(codeQuestionAnswer);
        }catch (Exception e){
            e.printStackTrace();
            return DataResult.fail("fail");
        }
        long endTime=System.currentTimeMillis();
        HashMap<String,Integer>data=new HashMap<>();
        data.put("cost_time",(int)(endTime-startTime));
        data.put("update_count",maxPage*10);
        return DataResult.success("success",data);
    }
    public static DataResult updateDBManual(String question,String answer){
        if(JDBCDAO.addQuestionAndAnswer(question,answer))
            return DataResult.success("success","手动更新成功");
        else
            return DataResult.fail("fail");
    }
}
