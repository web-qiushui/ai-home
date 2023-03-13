package com.home.common.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.home.common.constant.config.AliyunSmsConfig;
import com.home.common.exception.RRException;
import com.home.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿里短信工具类
 */
@Slf4j
@Component
public class SmsUtils {


    @Resource
    private SysConfigService configService;

    private static SysConfigService sysConfigService;

    @PostConstruct
    private void init() {
        sysConfigService = configService;
    }

    /**
     * 发送验证码短信
     * @param phone 手机号
     * @param code 验证码
     */
    public static String SendSms(String phone, String code) {
        AliyunSmsConfig aliyunSmsConfig = sysConfigService.getAliyunSmsConfig();
        if (aliyunSmsConfig == null || Constant.Status.DISABLE.getType().equals(aliyunSmsConfig.getStatus())) {
            throw new RRException("短信功能未开启");
        }
        try {
            SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            // 这里一定要设置GMT时区
            df.setTimeZone(new SimpleTimeZone(0, "GMT"));
            Map<String, String> paras = new HashMap<>();
            paras.put("AccessKeyId", aliyunSmsConfig.getAccessKey());
            paras.put("Action", "SendSms");
            paras.put("Format", "json");
            paras.put("SignatureMethod", "HMAC-SHA1");
            paras.put("SignatureNonce", IdUtil.randomUUID());
            paras.put("SignatureVersion", "1.0");
            paras.put("Timestamp", df.format(new Date()));
            paras.put("Version", "2017-05-25");
            paras.put("PhoneNumbers", phone);
            paras.put("SignName", aliyunSmsConfig.getSign());
            paras.put("TemplateCode", aliyunSmsConfig.getTemplateCode());
            paras.put("TemplateParam", "{\"code\":\""+ code +"\"}");

            // 参数KEY排序
            TreeMap<String, String> sortParas = new TreeMap<>(paras);
            // 构造待签名的字符串
            Iterator<String> it = sortParas.keySet().iterator();
            StringBuilder sortQueryStringTmp = new StringBuilder();
            while (it.hasNext()) {
                String key = it.next();
                sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(paras.get(key)));
            }
            // 去除第一个多余的&符号
            String sortedQueryString = sortQueryStringTmp.substring(1);
            String stringToSign = "GET" + "&" +
                    specialUrlEncode("/") + "&" +
                    specialUrlEncode(sortedQueryString);
            String signature = sign(aliyunSmsConfig.getAccessSecret() + "&", stringToSign);
            // 签名最后也要做特殊URL编码
            signature = specialUrlEncode(signature);

            String url = aliyunSmsConfig.getUrl() + "?Signature=" + signature + sortQueryStringTmp;
            String res = HttpUtil.get(url);
            log.info("发送短信返回信息，res：{}", res);
            return res;
        } catch (Exception e) {
            log.error("发送短信失败", e);
            return null;
        }
    }

    /**
     * 编码
     */

    private static String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    /**
     * 签名
     */
    private static String sign(String accessSecret, String stringToSign) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return new sun.misc.BASE64Encoder().encode(signData);
    }
}
