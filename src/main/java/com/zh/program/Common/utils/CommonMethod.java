package com.zh.program.Common.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zh.program.Common.Constant;
import com.zh.program.Dto.Token;
import com.zh.program.Entity.WechatUser;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonMethod {
    /**
      * 获取接口访问凭证
      *
      * @param appid 凭证
      * @param appsecret 密钥
      * @return
    */
    public static Token getToken(String appid, String appsecret, String code) throws Exception {
        Token token = null;
        String requestUrl = Constant.TOKEN_URL.replace("APPID", appid).replace("SECRET", appsecret).replace("CODE", code);
        // 发起GET请求获取凭证
        String result = HTTP.get(requestUrl, null);

        JSONObject jsonObject = JSONObject.parseObject(result);
        if (null != jsonObject) {
            try {
                token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInteger("expires_in"));
                token.setOpenId(jsonObject.getString("openid"));
            } catch (JSONException e) {
                token = null;
                // 获取token失败
                System.out.println("获取token失败");
                e.printStackTrace();
                //log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return token;
    }

    /**
     * 获取用户对象
     * @param access_token
     * @param open_id
     * @return
     */
    public static WechatUser getUser(String access_token, String open_id){
        String requestUrl = Constant.WX_SNSAPI_URL.replace("ACCESS_TOKEN", access_token).replace("OPENID", open_id);
        // 发起GET请求获取凭证
        String result = null;
        try {
            result = HTTP.get(requestUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            result = new String(result.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        String openid = jsonObject.getString("openid");
        String nickname = jsonObject.getString("nickname");
        Integer sex = jsonObject.getInteger("sex");
        String city = jsonObject.getString("city");
        String province = jsonObject.getString("province");
        String country = jsonObject.getString("country");
        String headimgurl = jsonObject.getString("headimgurl");
        String privilege = jsonObject.getString("privilege");
        String unionid = jsonObject.getString("unionid");
        WechatUser wechatUser = new WechatUser();
        wechatUser.setOpenId(openid);
        wechatUser.setNickName(nickname);
        wechatUser.setSex(sex);
        wechatUser.setCity(city);
        wechatUser.setProvince(province);
        wechatUser.setCountry(country);
        wechatUser.setHeadimgurl(headimgurl);
        wechatUser.setPrivilege(StrUtils.isBlank(privilege) ? " " : privilege);
        wechatUser.setUnionid(StrUtils.isBlank(unionid) ? " " : unionid);
        return wechatUser;
    }
    
      /**
      * URL编码（utf-8）
      * 
      * @param source
      * @return
      */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
          * 发送https请求
          * 
          * @param requestUrl 请求地址
          * @param requestMethod 请求方式（GET、POST）
          * @param outputStr 提交的数据
          * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
          */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
    	JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
 
 
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
 
 
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
 
 
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
 
 
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
        	System.out.println("连接超时：{}");
        	ce.printStackTrace();
        } catch (Exception e) {
        	System.out.println("https请求异常：{}");
        	e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

}
