package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by hi on 2020/5/19.
 */
//连接Mysql8.0.19
public class JDBCDAO {
    private static String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
    private static String URL="jdbc:mysql://localhost:3306/qa?&useSSL=false&serverTimezone=UTC";
    private static String User="root";
    private static String PassWord="LOVEYOU0812";
    static Connection connection;
    static Statement statement;
    static{
        try{
            Class.forName(JDBC_DRIVER);
        }catch (Exception e){System.out.println("异常提醒:"+e);}
        try{
            connection= DriverManager.getConnection(URL,User,PassWord);
        }catch (Exception e){System.out.println("异常提醒:"+e);}
        try{
        statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        }catch (Exception e){System.out.println("异常提醒:"+e);}
    }
    //questionDeal
    public static Vector<String> getQuestionVectorAll() {
        Vector<String> questionVecor=new Vector<>();
        String querySql="SELECT * FROM qa";
        try{
        ResultSet resultSet=statement.executeQuery(querySql);
        while(resultSet.next()){
            String temp=resultSet.getString("question");
            System.out.print(temp);
            temp=temp.replace(" ","");
            System.out.print(temp);
            questionVecor.add(temp);
        }}catch (Exception e){System.out.println("查询异常1:"+e);}
        return questionVecor;
    }
    public static void addNewQuestion(String question){
        try{
        String insertSql="INSERT INTO question(question) VALUES(\""+question+"\")";
        statement.execute(insertSql);}catch (Exception e){System.out.println("更新异常1:"+e);}
    }
    //anwserDeal
    public static Map<String ,String> getQuestionAndAnswer(){
        Map<String,String> questionAndAnswer=new HashMap<>();
        String querySql="SELECT * FROM qa";
        try{
            ResultSet resultSet=statement.executeQuery(querySql);
            while(resultSet.next()){
                questionAndAnswer.put(resultSet.getString("question"),resultSet.getString("answer"));
            }
        }catch (Exception e){System.out.println("查询异常2:"+e);}
        return questionAndAnswer;
    }
    public static boolean addQuestionAndAnswer(String question,String answer){
        String insertSql="INSERT INTO qa VALUES(\""+question+"\",\""+answer+"\")";
        try{
            statement.executeUpdate(insertSql);
            return true;
        }catch (Exception e){System.out.print("更新异常2："+e);}
        return false;
    }
    public static void addCode(String code){
        String insertSql="INSERT qa VALUES(\""+code+"\")";
        try{
            statement.executeUpdate(insertSql);
        }catch (Exception e){System.out.print("更新异常3："+e);}
    }
}
