package NlpSystem.domain;

import dao.JDBCDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;

public class Corpus {
    private Vector<Vector<String>> Dirc = new Vector<Vector<String>>();

    private Vector<Vector<WordCode>> DircCode = new Vector<Vector<WordCode>>();

    public void Init(Vector<Vector<String>> Words){
        for(Vector<String> OneLine:Words){
            for(int i = 0;i<OneLine.size();i++){
                if(OneLine.get(i) == " "){
                    OneLine.remove(i);
                }
            }
            Dirc.add(OneLine);
        }
    }

    public void InitByDB(){
        //从数据库中一行行读出：【Bj3A1=,新型冠状病毒】【 Hi8C3=,肆虐】【 Di1A1=,全球】
        Vector<String>code=JDBCDAO.getCode();
        for(int i=0;i<code.size();i++){
            if(i == 147){
                System.out.print("!!!");
            }
            String temp=code.get(i);
            String []arr=temp.split("\\s+");
            Vector<WordCode> wordCodes=new Vector<>();
            Vector<String>question=new Vector<>();
            for(String ss:arr){
                String []arr2=ss.split(",");
                WordCode wordCode=new WordCode(arr2[0]);
                wordCode.setInitWord(arr2[1]);
                wordCodes.add(wordCode);
                question.add(arr2[1]);
            }
            Dirc.add(question);
            DircCode.add(wordCodes);
        }
    }

    public Vector<Vector<WordCode>> getDircCode() {
        return DircCode;
    }

    public void setDircCode(Vector<Vector<WordCode>> dircCode) {
        DircCode = dircCode;
    }

    public void ShowDirc(){
        System.out.println("-------------语料库------------");
        int ctrl = 1;
        for(Vector<String> Line:Dirc){
            for (String s:Line){
                System.out.print(s + " ");
            }
            System.out.print("\n");
        }
        System.out.println(" ");
    }

    public void ShowDircCode(){
        System.out.println("-------------语料库编码------------");
        int ctrl = 0;
        for(int i =0;i<DircCode.size();i++){
            for(int j = 0;j<DircCode.get(i).size();j++){
                if(DircCode.get(i).get(j).toString().equals("Zz0Z0@")){
                    System.out.print(Dirc.get(i).get(j) + " ");
                    ctrl++;
                }else{
                    System.out.print(DircCode.get(i).get(j).toString() + " ");
                }
            }
            System.out.print("\n");
        }
        System.out.println("Zz总共:"+ctrl+"个");
    }//这个Show应该在编码完成后

    public Vector<Vector<String>> getDirc() {
        return Dirc;
    }

    public void setDirc(Vector<Vector<String>> dirc) {
        Dirc = dirc;
    }

    public void WriteToFile(String Path)throws Exception{
        File file = new File(Path);
        if (file.exists()) { // 如果已存在,删除旧文件
            file.delete();
        }
        file.createNewFile();
        // 将格式化后的字符串写入文件
        Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

        for(Vector<String>OneLine:Dirc){
            StringBuilder stringBuilder = new StringBuilder();
            for (String s:OneLine){
                stringBuilder.append(s + " ");
            }
            write.write(stringBuilder.toString() + "\r\n");
            write.flush();
        }
        write.close();
    }
    public void addCodeToDB(Vector<Vector<WordCode>> dircCode){
        for(int i=0;i<dircCode.size();i++){
            String code="";
            for(int j=0;j<dircCode.get(i).size();j++){
                String temp=dircCode.get(i).get(j).toString()+" ";
                code+=temp;
            }
            JDBCDAO.addCode(code,i+1);
        }
    }
}
