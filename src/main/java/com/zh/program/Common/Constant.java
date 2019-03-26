package com.zh.program.Common;

/**
 * Created by Administrator on 2018/7/16 0016.
 */
public class Constant {
    /**
     * 随机数
     */
    public static final String[] ROUND_NUM={"1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    /**
     * 应用授权作用域
     */
    //不弹出授权页面，直接跳转，只能获取用户openid
    public final static String SNSAPI_BASE = "snsapi_base";
    //弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息
    public final static String SNSAPI_USERINFO = "snsapi_userinfo";

    // 凭证获取（GET）
    public final static String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    // 用户同意授权,获取code
    public final static String WX_OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    // 用户同意授权,回调url
    public final static String WX_REDIRECT_URL = "http://tlhe.cn/web.html";

    // 拉去用户信息url
    public final static String WX_SNSAPI_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //微信openId
    public final static String WX_OPEN_ID = "wx7968b1618f078cc4";

    //微信appSecret
    public final static String WX_APP_SECRET = "8292b0a21e4f201417706ec5ee24f832";
}
