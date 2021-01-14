package com.lyqiang.money.come.quartz;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyqiang.money.come.util.Constant;
import com.lyqiang.money.come.util.JdOrderStateEnum;
import com.lyqiang.money.come.util.JdSignUtil;
import com.lyqiang.money.come.util.PropertiesReader;
import com.lyqiang.money.come.wxsend.PushWxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyqiang
 */
@Slf4j
@Component
public class ZhiHuJdMoneyCome {

    @Autowired
    private PropertiesReader propertiesReader;

    @Autowired
    private PushWxMessage pushWxMessage;

    @Scheduled(cron = "0 */5 * * * ?")
    public void query() {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime fiveMinBefore = nowTime.minusMinutes(5);
        queryFromJd(fiveMinBefore, nowTime);
    }

    public void queryFromJd(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> paramMap = packageParam(startTime, endTime);
        String httpResp = HttpUtil.get(Constant.JD_API_URL, paramMap);
        JSONObject respJson = JSON.parseObject(httpResp);
        JSONObject queryResponse = respJson.getJSONObject("jd_union_open_order_row_query_response");
        String result = queryResponse.getString("result");
        JSONObject resultJson = JSON.parseObject(result);
        JSONArray dataArr = resultJson.getJSONArray("data");
        if (dataArr == null || dataArr.isEmpty()) {
            log.info("没有获取到订单数据,继续努力~");
            return;
        }
        packageContentAndSendWx(dataArr);
    }

    private void packageContentAndSendWx(JSONArray dataArr) {
        String title = "老板出单了啊，共" + dataArr.size() + "件，金额：";
        BigDecimal totalMoney = BigDecimal.ZERO;
        StringBuilder contentBuilder = new StringBuilder();
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject data = dataArr.getJSONObject(i);
            BigDecimal estimateFee = data.getBigDecimal("estimateFee");
            totalMoney = totalMoney.add(estimateFee);
            String skuName = data.getString("skuName");
            int validCode = data.getIntValue("validCode");
            String orderStateName = JdOrderStateEnum.getNameByCode(validCode);
            log.info("新出单子了，商品名称：{}, 金额：{}，状态：{}", skuName, estimateFee, orderStateName);
            contentBuilder.append("- 商品名称： ").append(skuName)
                    .append("，金额：").append(estimateFee)
                    .append("，状态：").append(orderStateName)
                    .append("\n\n");
        }
        pushWxMessage.sendWxMessage(title + totalMoney.toString(), contentBuilder.toString());
    }

    private Map<String, Object> packageParam(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime nowTime = LocalDateTime.now();
        String nowTimeStr = nowTime.format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT));

        String startTimeStr = startTime.format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT));
        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT));

        Map<String, Object> bizParam = new HashMap<>(2);
        Map<String, Object> orderReqParam = new HashMap<>(8);

        orderReqParam.put("startTime", startTimeStr);
        orderReqParam.put("endTime", endTimeStr);
        orderReqParam.put("type", 1);
        orderReqParam.put("pageIndex", 1);
        orderReqParam.put("pageSize", 10);
        bizParam.put("orderReq", orderReqParam);

        Map<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("app_key", propertiesReader.getAppKey());
        paramMap.put("sign_method", Constant.SIGN_METHOD);
        paramMap.put("format", Constant.PARAM_FORMAT);
        paramMap.put("v", Constant.VERSION);
        paramMap.put("timestamp", nowTimeStr);
        paramMap.put("method", Constant.JD_QUERY_METHOD);
        String paramJson = JSON.toJSONString(bizParam);
        paramMap.put("param_json", paramJson);
        paramMap.put("sign", JdSignUtil.buildSign(nowTimeStr, paramJson, propertiesReader.getAppKey(),
                propertiesReader.getAppSecret()));
        return paramMap;
    }

}
