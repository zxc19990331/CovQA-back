package NlpSystem.domain;



//import com.monitorjbl.xlsx.StreamingReader;
import dao.JDBCDAO;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

public class Questions {
    private Vector<String> questions = new Vector<String>();

    private Map<String,String> questionAndAnswer = new LinkedHashMap<>();
    public void Init(){
        questions.add("我经常熬夜，睡得很晚，没事吧");
        questions.add("新冠肺炎是什么东东？");
        questions.add("多喝水有没有帮助呢？");
        questions.add("新冠肺炎确诊多少人了？");
        questions.add("美国新冠肺炎死了多少？");
        questions.add("群体免疫有用吗？");
        questions.add("疫苗开发的怎么样了？");
        questions.add("什么是新冠肺炎？");
    }

    public String getAnswer(String key){
        return questionAndAnswer.get(key);
    }

    /*public void InitByFile(String path)throws  Exception{
        long time = System.currentTimeMillis();

        System.out.println("读取excel");
        FileInputStream in = new FileInputStream(new File(path));
        Workbook open = StreamingReader.builder()
                .rowCacheSize(10)//一次读取多少行(默认是10行)
                .bufferSize(1024)//使用的缓冲大小(默认1024)
                .open(in);

        for (Sheet sheet : open) {
            for (Row row : sheet) {

                String ques = row.getCell(0).getStringCellValue();
                String answers = row.getCell(1).getStringCellValue();
                ques = ques.replace(" ","");
                questions.add(ques);
                questionAndAnswer.put(ques,answers);
            }
        }
        System.out.println("读取完成");
        System.out.println("读取问题数据库 耗时：" + (System.currentTimeMillis() - time) + "ms");
    }*/

    public void InitByDataBase(){
        /*TODO 这里写数据库接口
        *  问题数据结构是Vector<String>
        *  问题-答案数据结构是Map<"question","answer">
        */
        questions=JDBCDAO.getQuestionVectorAll();
        questionAndAnswer=JDBCDAO.getQuestionAndAnswer();
    }

    public Vector<String> getQuestions() {
        return questions;
    }

    public void setQuestions(Vector<String> questions) {
        this.questions = questions;
    }

    public void Show(){
        System.out.println("-------------问题库------------");
        for(String s:questions){
            System.out.println(s + "  ");
        }
    }

    public void addQuestion(String ques){
        questions.add(ques);//最后一行加入问题
    }
}
