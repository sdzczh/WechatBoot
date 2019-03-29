package com.zh.program.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 浏览记录
 */
@Data
@Entity
@Table(name = "qrcode_img")
public class QRCodeImg implements Serializable {

    private static final long serialVersionUID = -3703491852417733717L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String openId; // 用户open_id
    private String img  ;//图片遍码
    private Integer type; //0微信 1支付宝
    private String createTime;
}
