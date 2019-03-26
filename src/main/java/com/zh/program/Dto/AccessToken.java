package com.zh.program.Dto;

import lombok.Data;

/**
 * 获取微信token的类
 */
@Data
public class AccessToken {
    private String access_token;

    private String expires_in;
}
