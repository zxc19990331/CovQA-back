package NlpSystem.domain;

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
}