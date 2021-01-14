package com.lyqiang.money.come.wxsend;

import cn.hutool.http.HttpUtil;
import com.lyqiang.money.come.util.PropertiesReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyqiang
 */
@Component
@Slf4j
public class PushWxMessage {

    @Autowired
    private PropertiesReader propertiesReader;

    /**
     * text：消息标题，最长为256，必填。
     * desp：消息内容，最长64Kb，可空，支持MarkDown。
     */
    public void sendWxMessage(String title, String content) {
        String url = propertiesReader.getWxSendUrl();
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("text", title);
        paramMap.put("desp", content);
        String s = HttpUtil.post(url, paramMap);
        log.info("调用 server 酱发送消息，返回：{}", s);
    }

}
