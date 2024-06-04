package com.yusihu.statemachine;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yusihu
 * @date 2024-06-04 17:35
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    /**
     * 待支付，待发货，待收货，已完成
      */
    WAIT_PAYMENT(1, "待支付"),
    WAIT_DELIVER(2, "待发货"),
    WAIT_RECEIVE(3, "待收货"),
    FINISH(4, "已完成");

    private Integer key;
    private String desc;

    public static OrderStatus getByKey(Integer key) {
        for (OrderStatus status : values()) {
            if (status.getKey().equals(key)) {
                return status;
            }
        }
        throw new RuntimeException("key=" + key + " OrderStatus enum not exists.");
    }
}
