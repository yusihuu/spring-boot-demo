package com.yusihu.statemachine;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yusihu
 * @date 2024-06-04 19:26
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
