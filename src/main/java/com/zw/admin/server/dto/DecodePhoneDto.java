package com.zw.admin.server.dto;

import lombok.Data;

/**
 * @author larry
 * @Description:
 * @date 2021/2/4 16:25
 */
@Data
public class DecodePhoneDto {

    //会话密钥
    private String sessionKey;

    //加密初始向量向量
    private String iv;

    //加密后的密文
    private String encryptedData;

    private String jsCode;
}
