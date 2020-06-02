package SpiderUtils;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by hi on 2020/6/2.
 */
public class Spider {
    public static void main(String[] args){
        CloseableHttpClient httpClient=HttpClients.createDefault();
        CloseableHttpResponse response=null;
        //悟空问答，搜索新冠肺炎url
        HttpGet request=new HttpGet("https://www.wukong.com/search/?keyword=%E6%96%B0%E5%86%A0%E8%82%BA%E7%82%8E");
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML,like Gecko) Chrome/74.0.3729.169 Safari/537.");
        try{
            response=httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                System.out.println(html);
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
}
