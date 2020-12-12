package com.lyqiang.money.come.util;

import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.dialect.PropsUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author lyqiang
 */
@Getter
@Slf4j
@Component
public class PropertiesReader {

    private String appKey;

    private String appSecret;

    private String wxSendUrl;

    @PostConstruct
    private void init() {
        String userHome = System.getProperty("user.home");
        log.info("用户目录是：{}", userHome);
        String fileName = userHome + File.separator + Constant.SECRET_FILE_NAME;
        Props props = null;
        try {
            props = PropsUtil.get(fileName);
        } catch (Exception ex) {
            log.error("获取配置文件异常，请检查，filePath:{}", fileName);
            throw ex;
        }
        String appKeyStr = props.getStr(Constant.APP_KEY);
        String appSecretStr = props.getStr(Constant.APP_SECRET);
        String wxSendUrlStr = props.getStr(Constant.WX_SEND_URL);

        checkValueNotFound(Constant.APP_KEY, appKeyStr);
        checkValueNotFound(Constant.APP_SECRET, appSecretStr);
        checkValueNotFound(Constant.WX_SEND_URL, wxSendUrlStr);

        this.appKey = appKeyStr;
        this.appSecret = appSecretStr;
        this.wxSendUrl = wxSendUrlStr;
    }

    private void checkValueNotFound(String key, String value) {
        if (StringUtils.isBlank(value)) {
            throw new RuntimeException("没有找到 " + key + "，请检查配置");
        }
    }

}
