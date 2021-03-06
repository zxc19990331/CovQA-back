package NlpSystem;

import FeatureExtractionUtils.Learn;
import NlpSystem.domain.Corpus;
import NlpSystem.domain.Questions;
import NlpSystem.domain.WordCode;
import PreprocessingUtils.Segment;
import PreprocessingUtils.StopWord;
import PreprocessingUtils.SynonymCode;

import java.io.File;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Vector;

public class NlpModel {

    //数据库
    private Corpus corpus = new Corpus();//语料库
    private Questions questions = new Questions();//问题库

    //NLP工具
    private Segment segment = new Segment();//分词器
    private StopWord stopWord = new StopWord();//去停词器
    private SynonymCode synonymCode = new SynonymCode();//同义词编码器

    //判断是否已经编码
    private boolean isVerb=false;

    private void WordPerprocessingPart()throws Exception{
        Vector<Vector<String>> InitWords = segment.BilateralSegment(questions.getQuestions());//问题库获得问题后分词器分词
        Vector<Vector<String>> UsefulWords = stopWord.RemoveStopWords(InitWords);//分词器结果去停词
        corpus.Init(UsefulWords);//去停词后的语料建立语料库
       // corpus.WriteToFile(this.getClass().getClassLoader().getResource("corpus.txt").getPath());
    }

    private void FeatureExtractionPart() throws Exception{
        Learn learn = new Learn();
        long start = System.currentTimeMillis();
        learn.learnFile(new File(this.getClass().getClassLoader().getResource("corpus.txt").getPath()));//TODO 资源：学习源文件
        System.out.println("use time " + (System.currentTimeMillis() - start));
        learn.saveModel(new File(this.getClass().getClassLoader().getResource("resource.bin").getPath()));//TODO 资源：结果bin文件
    }

    private void WordSynonymCodePart(){
        corpus = synonymCode.GenerateDircCode(corpus);
        isVerb=true;
    }

    public void Init()throws Exception{
        segment.Init();//读取字典
        stopWord.Init();//读取停用词典
        synonymCode.init();//读取同义词词典
      //questions.Init();;//初始化问题，Init中内置了7句问题
//        questions.InitByDataBase();
        //questions.InitByFile(this.getClass().getClassLoader().getResource("qa2.xlsx").getPath());

    }//初始化NLP模块：分词器+去停词器+问题库

    public void ShowQuestions(){
        questions.Show();
    }//展示问题内容
    public void ShowCorpus(){
        corpus.ShowDirc();
        corpus.ShowDircCode();
    }//展示语料库

    public ArrayList<String> getStringCode(String question){
        Vector<String> QVector = segment.BMM_FMMSegment(question);
        QVector = stopWord.RemoveOneLine(QVector);
        ArrayList<String> Question = new ArrayList<>();
        for(String s: QVector){
            Question.add(synonymCode.getWordCode(s).toString());
        }
        return Question;
    }

    public String getAnswers(String Q)throws Exception{
//        questions.addQuestion(Q);//问题融入问题库
//        WordPerprocessingPart();//初始化问题+分词+去停词+构件语料库
//
//        long time = System.currentTimeMillis();
//        if(!isVerb){
//        WordSynonymCodePart();//语料库编码
//        corpus.addCodeToDB(corpus.getDircCode());
//        }
//        else {
//
//        }
//        System.out.println("语料库编码化 耗时：" + (System.currentTimeMillis() - time) + "ms");

        //corpus.InitByDataBase();
        this.Init();
        corpus.InitByDB();
        questions.InitByDataBase();
        String Answer = FindClosestQuestion(Q);
        return Answer;

    }

    public void OnceCoding()throws  Exception{
        this.Init();//先初始化
        WordPerprocessingPart();
        long time = System.currentTimeMillis();
        WordSynonymCodePart();//语料库编码
        System.out.println("语料库编码化 耗时：" + (System.currentTimeMillis() - time) + "ms");
        corpus.addCodeToDB(corpus.getDircCode());
    }//TODO 这个方法进行对问题库编码并写入数据库


    private String FindClosestQuestion(String Q){
        Vector<String> QVector = segment.BMM_FMMSegment(Q);
        QVector = stopWord.RemoveOneLine(QVector);
        Vector<WordCode> Question = new Vector<>();
        for(String s: QVector){
            Question.add(synonymCode.getWordCode(s));
        }

        for(String s:QVector){
            System.out.print(s + " ");
        }
        for(WordCode s:Question){
            System.out.print(s.toString() + " ");
        }
        String Answer = null;

        double MaxScore = -1;
        int MaxIter = -1;
        for(int i = 0;i<corpus.getDircCode().size();i++){
            System.out.print("正在计算相似度" + i +"：" + corpus.getDirc().get(i));
            double score = synonymCode.CalcDistance(Question,corpus.getDircCode().get(i));
            System.out.println("***" + score);
            if(score > MaxScore){
                MaxIter = i;
                MaxScore = score;
            }
        }

        if(MaxScore > 1.0){
            String ClosestQues = questions.getQuestions().get(MaxIter);
            System.out.println("最相近问题：" + ClosestQues);
            Answer = questions.getAnswer(ClosestQues);
        }else{
            System.out.println("匹配不到相似问题");
        }
        return Answer;
    }
}