package NlpSystem;

import FeatureExtractionUtils.Learn;
import FeatureExtractionUtils.Word2VEC;
import NlpSystem.domain.Corpus;
import NlpSystem.domain.QueryVector;
import NlpSystem.domain.Questions;
import PreprocessingUtils.Segment;
import PreprocessingUtils.StopWord;

import java.io.File;
import java.util.Vector;

public class NlpModel {

    //数据库
    private Corpus corpus = new Corpus();//语料库
    private Questions questions = new Questions();//问题库

    //NLP工具
    private Segment segment = new Segment();//分词器
    private StopWord stopWord = new StopWord();//去停词器

    private void WordPerprocessingPart()throws Exception{
        Vector<Vector<String>> InitWords = segment.BilateralSegment(questions.getQuestions());//问题库获得问题后分词器分词
        Vector<Vector<String>> UsefulWords = stopWord.RemoveStopWords(InitWords);//分词器结果去停词
        corpus.Init(UsefulWords);//去停词后的语料建立语料库
        ShowCorpus();
        corpus.WriteToFile(this.getClass().getClassLoader().getResource("corpus.txt").getPath());
    }

    private void FeatureExtractionPart() throws Exception{
        Learn learn = new Learn();
        long start = System.currentTimeMillis();
        learn.learnFile(new File(this.getClass().getClassLoader().getResource("corpus.txt").getPath()));//TODO 资源：学习源文件
        System.out.println("use time " + (System.currentTimeMillis() - start));
        learn.saveModel(new File(this.getClass().getClassLoader().getResource("resource.bin").getPath()));//TODO 资源：结果bin文件
    }

    public void Init()throws Exception{
        segment.Init();//读取字典
        stopWord.Init();//读取停用词典

//        questions.Init();;//初始化问题，Init中内置了7句问题

        //TODO question需要从数据库中读取问题（接口未实现）
        // questions.InitByDataBase();
        //questions.InitByFile("src\\main\\resources\\quesAnsw.xlsx");
        //********************
        questions.InitByDataBase();

    }//初始化NLP模块：分词器+去停词器+问题库

    public void ShowQuestions(){
        questions.Show();
    }//展示问题内容
    public void ShowCorpus(){
        corpus.Show();
    }//展示语料库



    public String getAnswers(String Q)throws Exception{
        questions.addQuestion(Q);//问题融入问题库
        WordPerprocessingPart();//初始化问题+分词+去停词+构件语料库
        FeatureExtractionPart();//语料库向量化

        Word2VEC word2VEC = new Word2VEC();
        word2VEC.loadJavaModel(this.getClass().getClassLoader().getResource("resource.bin").getPath());//TODO 资源：调用resources中resource.bin

        Vector<String> ques = corpus.getDirc().get(corpus.getDirc().size()-1);
        QueryVector AimVector = new QueryVector();
        AimVector.Calc(word2VEC,ques);
        System.out.print(ques);
        System.out.println(AimVector.getVector());

        double max = -1;
        int iter = -1;
        for(int i = 0;i<corpus.getDirc().size()-1;i++){
            QueryVector vector = new QueryVector();
            vector.Calc(word2VEC,corpus.getDirc().get(i));
            double res = AimVector.COS(vector);
            if(res>max){
                max = res;
                iter = i;
            }
        }
        System.out.println("提问："+questions.getQuestions().get(questions.getQuestions().size()-1));
        if(max > 0.3){
            System.out.println("相似："+questions.getQuestions().get(iter));
            return questions.getAnswer(questions.getQuestions().get(iter));
        }else{
            System.out.println("无相似度高于0.3的问题");
            return "抱歉，我不知道。";
        }

    }
}