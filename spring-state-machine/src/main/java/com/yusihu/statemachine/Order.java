package com.yusihu.statemachine;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author yusihu
 * @date 2024-06-04 19:27
 */
@Entity
@Table(name="order")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Integer status;
}
