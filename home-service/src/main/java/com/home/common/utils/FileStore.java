package com.home.common.utils;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;

/**
 * 上传文件工具类
 */
public class FileStore {

    /**
     * 生成随机文件名
     */
    public static String generateRandomFilename() {
        //生成随机数
        Random rand = new Random();
        int random = rand.nextInt();

        Calendar calCurrent = Calendar.getInstance();
        int intMonth = calCurrent.get(Calendar.MONTH) + 1;
        int intYear = calCurrent.get(Calendar.YEAR);
        String now = "/" + intYear + "/" + intMonth + "/" + System.currentTimeMillis();

        return now + (random > 0 ? random : (-1) * random);
    }

    public static boolean upload(MultipartFile file, String path) {
        File rootDest = new File(path);
        // 检测是否存在目录
        if (!rootDest.getParentFile().exists()) {
            // 新建文件夹
            rootDest.getParentFile().mkdirs();
        }
        File dest = new File(path);
        try {
            // 文件写入
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean uploadBase64(MultipartFile file, String path) {
        File rootDest = new File(path);
        // 检测是否存在目录
        if (!rootDest.getParentFile().exists()) {
            // 新建文件夹
            rootDest.getParentFile().mkdirs();
        }
        File dest = new File(path);
        try {
            // 文件写入
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null) {
            return false;
        }

        String[] baseStrs = imgStr.split(",");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(baseStrs[1]);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}