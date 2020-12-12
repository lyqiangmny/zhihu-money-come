package com.lyqiang.money.come.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author lyqiang
 */
public class JdSignUtil {

    private JdSignUtil() {
    }

    public static String buildSign(String timestamp, String paramJson, String appKey, String appSecret) {
        //第一步，按照顺序填充参数
        Map<String, String> map = new TreeMap<>();
        map.put("timestamp", timestamp);
        map.put("v", Constant.VERSION);
        map.put("sign_method", Constant.SIGN_METHOD);
        map.put("format", Constant.PARAM_FORMAT);
        map.put("method", Constant.JD_QUERY_METHOD);
        //param_json为空的时候需要写成 "{}"
        map.put("param_json", paramJson);
        map.put("app_key", appKey);

        StringBuilder sb = new StringBuilder(appSecret);
        //按照规则拼成字符串
        for (Map.Entry entry : map.entrySet()) {
            String name = (String) entry.getKey();
            String value = (String) entry.getValue();
            //检测参数是否为空
            if (areNotEmpty(new String[]{name, value})) {
                sb.append(name).append(value);
            }
        }
        sb.append(appSecret);
        //MD5
        return md5(sb.toString());
    }

    private static String md5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes(StandardCharsets.UTF_8));
            return byte2hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    private static boolean areNotEmpty(String[] values) {
        boolean result = true;
        if ((values == null) || (values.length == 0)) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    private static boolean isEmpty(String value) {
        int strLen;
        if ((value == null) || ((strLen = value.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
