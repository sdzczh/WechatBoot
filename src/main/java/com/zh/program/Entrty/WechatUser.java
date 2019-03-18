package com.zh.program.Entrty;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 微信用户资料
 */
@Data
@Entity
@Table(name = "t_user")
public class WechatUser implements Serializable {

    private static final long serialVersionUID = -3703491852417733717L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String openId; // 微信用户openId
    private String nickName;
    private Integer sex; // 0 未知 1 男生 2 女生
    private String province; //普通用户个人资料填写的省份
    private String city; //普通用户个人资料填写的城市
    private String country;//国家，如中国为CN
    private String headimgurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    private String privilege;//用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
    private String unionid;//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
    private String createTime;
    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", openId=" + openId +
                ", nikeName=" + nickName +
                ", sex=" + sex +
                ", province=" + province +
                ", country=" + country +
                ", city=" + city +
                ", headimgurl=" + headimgurl +
                ", privilege=" + privilege +
                ", unionid=" + unionid +
                ", createTime=" + createTime +
                "}";
    }
}
