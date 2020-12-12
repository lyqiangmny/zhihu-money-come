package com.lyqiang.money.come.controller;

import com.lyqiang.money.come.quartz.ZhiHuJdMoneyCome;
import com.lyqiang.money.come.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lyqiang
 */
@RestController
public class TestController {

    @Autowired
    private ZhiHuJdMoneyCome zhiHuJdMoneyCome;

    @GetMapping(value = "/api/test/send")
    public Object testSend(@RequestParam(required = false) String orderTimeStr) {
        if (StringUtils.isBlank(orderTimeStr)) {
            return "没有输入时间";
        }
        LocalDateTime orderTime = LocalDateTime.parse(orderTimeStr, DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT));
        LocalDateTime startTime = orderTime.minusMinutes(10);
        LocalDateTime endTime = orderTime.plusMinutes(10);
        zhiHuJdMoneyCome.queryFromJd(startTime, endTime);
        return "检查微信消息";
    }
}
