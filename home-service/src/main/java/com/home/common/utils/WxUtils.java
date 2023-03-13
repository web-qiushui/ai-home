package com.home.common.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WxUtils {

    static String appId = "wxdbe8e58c712fe191";
    static String appSecret = "0cefa458e4fde0f41dd8515fccaa9024";
    private static String token = "34_dRfcpbyyz4RluDnTY-eYlWoDJDJ3R2tEeFAyZ09qKxkTFvWjo91HknsLReVUDCC-InzPBIpPYG-H-b6U2JRSxk2L_qfmstMH-sHQj5BrQotNErdL1d5bIbXuGLpV32ZWV5XzWf-wKGRyhMxYOIPcAFARLS";
    /**
     * 获取用户信息
     */
    private static String login_url = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code";

    /**
     * 获取AccessToken
     */
    private static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    /**
     * 获取unionid
     */
    private static String get_paid_unionid_url = "https://api.weixin.qq.com/wxa/getpaidunionid";

    public static void main(String[] args) {
//        code2Session(appId,appSecret, "0231DFeR1ZkKG51P9MeR1S8IeR11DFem");
//        String accessToken = getAccessToken(appId, appSecret);
//        System.out.println(accessToken);
//        getUnionId("oIU8t5LwksAe-zhQkEMaGuYdK7d8");
    }

    /**
     * 微信登录
     * @param appid 微信AppId
     * @param secret 微信 AppSecret
     * @param code 用户登录code
     * @return 登录信息
     */
    public static JSONObject code2Session(String appid, String secret, String code) {
        String url = login_url + "&appid=" + appid + "&secret=" + secret + "&js_code=" + code;
        String rest = HttpUtil.get(url);
        log.info("第三方接口-->微信登录-->返回参数：{}", rest);
        return JSON.parseObject(rest);
    }

    /**
     * 获取AccessToken
     * @param appid 微信AppId
     * @param secret 微信 AppSecret
     * @return token
     */
    public static String getAccessToken(String appid, String secret) {
        String url = access_token_url + "&appid=" + appid + "&secret=" + secret;
        String rest = HttpUtil.get(url);
        log.info("第三方接口-->微信获取AccessToken-->返回参数：{}", rest);
        JSONObject jsonObject = JSON.parseObject(rest);
        if (jsonObject.get("errcode") == null && jsonObject.get("access_token") != null) {
            token = jsonObject.getString("access_token");
        }
        return token;
    }

    /**
     * 获取unionId
     * @param openId 微信OpenId
     * @return token
     */
    public static String getUnionId(String openId) {
        String url = get_paid_unionid_url + "?access_token="+ token +"&openid=" + openId;
        String rest = HttpUtil.get(url);
        log.info("第三方接口-->微信获取unionId-->返回参数：{}", rest);
        return token;
    }

    /**
     * 校验
     * @param rawData
     * @param sessionKey
     * @return signature
     */
    public static Boolean signature(String rawData, String sessionKey, String signature) {
        String sign = SecureUtil.sha1(rawData + sessionKey);
        return signature.equals(sign);
    }



}
