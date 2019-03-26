package com.zh.program.Dto;

import lombok.Data;

/**
 * 微信公众号分享使用的参数
 */
@Data
public class JsapiTicket {
    private Integer errcode;

    private String errmsg;

    private String ticket;

    private Integer expires_in;
}
