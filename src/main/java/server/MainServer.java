package server;

import NlpSystem.NlpModel;
import dao.JDBCDAO;
import entity.DataResult;


public class MainServer {
    public static DataResult getAnswer(String question){
        NlpModel nlpModel =new NlpModel();
        String answer="";
        try{
            nlpModel.Init();
            nlpModel.ShowQuestions();
            answer=nlpModel.getAnswers(question);
        }catch (Exception e){System.out.println("异常提醒:"+e);}
        if(answer.isEmpty())
            return DataResult.success("success","我什么也不知道");
        else
            return DataResult.success("success",answer);
    }
}
