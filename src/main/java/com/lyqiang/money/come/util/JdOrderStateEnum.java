package com.lyqiang.money.come.util;

import lombok.Getter;

/**
 * @author lyqiang
 * 订单状态
 */
@Getter
public enum JdOrderStateEnum {

    //

    WAIT_PAY(15, "待付款"),
    PAYED(16, "已付款"),
    FINISH(17, "已完成"),
    ELSE(99, "无效");

    private final int code;

    private final String name;

    JdOrderStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (JdOrderStateEnum codeEnum : JdOrderStateEnum.values()) {
            if (code == codeEnum.getCode()) {
                return codeEnum.getName();
            }
        }
        return ELSE.getName();
    }
}
