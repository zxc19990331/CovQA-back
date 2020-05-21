package NlpSystem.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;

public class Corpus {
    private Vector<Vector<String>> Dirc = new Vector<Vector<String>>();

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

    public void Show(){
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
