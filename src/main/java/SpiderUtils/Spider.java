package SpiderUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import server.MainServer;

/**
 * Created by hi on 2020/6/2.
 */
public class Spider {
    //爬取搜狗问问页面的问答连接
    private static ArrayList<String> hrefList=new ArrayList();
    private static ArrayList<ArrayList<String>>questionAndAnswer=new ArrayList<>();
    public static ArrayList<ArrayList<String>> getQuestionAndAnswer() {
        return questionAndAnswer;
    }
    public static void getHref(int page){
        CloseableHttpClient httpClient=HttpClients.createDefault();
        CloseableHttpResponse response=null;
        //悟空问答，搜索新冠肺炎url
        HttpGet request=new HttpGet("https://www.sogou.com/sogou?query=%E6%96%B0%E5%86%A0%E8%82%BA%E7%82%8E&insite=wenwen.sogou.com&pid=sogou-wsse-a9e18cb5dd9d3ab4&rcer=&page="+page+"&ie=utf8");
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML,like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        try{
            response=httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                //System.out.println(html);
                Document document=Jsoup.parse(html);
                Elements postItems=document.getElementsByClass("fb");
                for(Element postItem:postItems){
                    Elements link=postItem.select("a[target='_blank']");
                    //System.out.println(link.attr("href"));
                    hrefList.add(link.attr("href"));
                }
            }else {
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        }catch (ClientProtocolException e){System.out.print("客户端协议异常："+e);}
        catch (IOException e){System.out.print("IO异常："+e);}
        finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }
    //返回一个数组，第一个元素为问题，第二个元素为答案
    public static ArrayList<String> getQuestionAndAnswer(String url){
        ArrayList<String>temp=new ArrayList<>();
        CloseableHttpClient httpClient=HttpClients.createDefault();
        CloseableHttpResponse response=null;
        //搜狗问问，搜索新冠肺炎url
        HttpGet request=new HttpGet(url);
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML,like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        try{
            response=httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                //System.out.println(html);
                Document document=Jsoup.parse(html);
                Element title=document.getElementById("question_title");
                //System.out.print(title.text());
                temp.add(title.text().replaceAll("[^\\u4E00-\\u9FA5]",""));
                Elements preTag=document.getElementsByTag("pre");
                //System.out.print(preTag.get(0).text());
                temp.add(preTag.get(0).text().replaceAll("[\\u25c6~\\u25c7]|[\\——]|[\\▲]|[\\△]]",""));
            }else {
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        }catch (ClientProtocolException e){System.out.print("客户端协议异常："+e);}
        catch (IOException e){System.out.print("IO异常："+e);}
        finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return temp;
    }
    //爬取前maxPage页的问题与答案
    public static void spiderQuestionAndAnswer(int maxPage){
        for(int i=1;i<=maxPage;i++){
            getHref(i);
        }
        for(String href:hrefList){
            questionAndAnswer.add(getQuestionAndAnswer(href));
        }
    }
  public static void main(String[] args){
      /*int maxPage=3;
        spiderQuestionAndAnswer(maxPage);
        for(ArrayList<String>temp:questionAndAnswer){
            for(String qa:temp){
                System.out.println(qa);
            }
            System.out.println();
        }*/
        //MainServer.updateDB();
        //正则表达式字符处理测试
        String temp="为阴性达到出院标准】,,,,▲◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇——————————";
        System.out.print(temp.replaceAll("[^\\u4E00-\\u9FA5]",""));
   }
}
