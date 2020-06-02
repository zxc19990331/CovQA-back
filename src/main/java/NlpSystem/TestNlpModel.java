package NlpSystem;

import NlpSystem.domain.WordCode;
import PreprocessingUtils.SynonymCode;

public class TestNlpModel {
    public static void main(String[] args) {
        NlpModel nlpModel = new NlpModel();
        try {
            System.out.println(nlpModel.getAnswers("新冠肺炎的英文"));
//            nlpModel.OnceCoding();
//            //nlpModel.ShowQuestions();
//            String question = "口罩能预防新冠吗";//TODO 这里输入问题
////            String question = "什么是新型冠状病毒？";
//            System.out.println("原问题：" + question);
//            System.out.println(nlpModel.getAnswers(question));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
