package server;

import NlpSystem.NlpModel;
import dao.JDBCDAO;
import entity.DataResult;


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
}
