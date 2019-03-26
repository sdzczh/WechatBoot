package com.zh.program.Common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zh.program.Common.Constant;
import com.zh.program.Dto.AccessToken;
import com.zh.program.Dto.JsapiTicket;
import org.apache.http.client.utils.HttpClientUtils;
import org.hibernate.exception.DataException;
import org.springframework.data.redis.core.RedisTemplate;
import org.weixin4j.util.PayUtil;
import org.weixin4j.util.SHA1;

import javax.annotation.Resource;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignatureUtils {
    @Resource
    private RedisTemplate<String, String> redis;

    /**
     * 获取access_token
     * @return
     */
    public static String getAccessToken() {
        String access_token = "";
        String grant_type = "client_credential";//获取access_token填写client_credential
        String AppId= Constant.WX_OPEN_ID;//第三方用户唯一凭证
        String secret= Constant.WX_APP_SECRET;//第三方用户唯一凭证密钥，即appsecret
        //这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;

        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
            System.out.println("JSON字符串："+demoJson);
            access_token = demoJson.getString("access_token");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    /**
     * 获取jsapi_ticket
     */

    public static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";//这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
            System.out.println("JSON字符串："+demoJson);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public static void main(String[] args) {
        //1、获取AccessToken
        String accessToken = getAccessToken();

        //2、获取Ticket
        String jsapi_ticket = getTicket(accessToken);

        //3、时间戳和随机字符串
        String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳

        System.out.println("accessToken:"+accessToken+"\njsapi_ticket:"+jsapi_ticket+"\n时间戳："+timestamp+"\n随机字符串："+noncestr);

        //4、获取url
        String url="http://tlhe.cn/";
    /*根据JSSDK上面的规则进行计算，这里比较简单，我就手动写啦
    String[] ArrTmp = {"jsapi_ticket","timestamp","nonce","url"};
    Arrays.sort(ArrTmp);
    StringBuffer sf = new StringBuffer();
    for(int i=0;i<ArrTmp.length;i++){
    	sf.append(ArrTmp[i]);
    }
    */

        //5、将参数排序并拼接字符串
        String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;

        //6、将字符串进行sha1加密
        String signature = SHA1.encode(str);
        System.out.println("参数："+str+"\n签名："+signature);
    }

}
