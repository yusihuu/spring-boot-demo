package com.yusihu.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yusihu
 * @date 2024-06-04 19:35
 */
@Service
@Slf4j
public class OrderServiceImpl{
    @Resource
    private StateMachine<OrderStatus, OrderStatusChangeEvent> orderStateMachine;
    @Resource
    private StateMachinePersister<OrderStatus, OrderStatusChangeEvent, String> stateMachineMemPersister;
    @Resource
    private OrderRepository orderMapper;

    public Order getById(Integer id) {
        return orderMapper.findById(id).orElse(null);
    }
    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    public Order create(Order order) {
        order.setStatus(OrderStatus.WAIT_PAYMENT.getKey());
        orderMapper.save(order);
        return order;
    }
    /**
     * 对订单进行支付
     *
     * @param id
     * @return
     */
    public Order pay(Integer id) {
        Order order = orderMapper.findById(id).orElse(null);
        log.info("线程名称：{},尝试支付，订单号：{}" ,Thread.currentThread().getName() , id);
        if (!sendEvent(OrderStatusChangeEvent.PAYED, order)) {
            log.error("线程名称：{},支付失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("支付失败, 订单状态异常");
        }
        return order;
    }
    /**
     * 对订单进行发货
     *
     * @param id
     * @return
     */
    public Order deliver(Integer id) {
        Order order = orderMapper.findById(id).orElse(null);
        log.info("线程名称：{},尝试发货，订单号：{}" ,Thread.currentThread().getName() , id);
        if (!sendEvent(OrderStatusChangeEvent.DELIVERY, order)) {
            log.error("线程名称：{},发货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("发货失败, 订单状态异常");
        }
        return order;
    }
    /**
     * 对订单进行确认收货
     *
     * @param id
     * @return
     */
    public Order receive(Integer id) {
        Order order = orderMapper.findById(id).orElse(null);
        log.info("线程名称：{},尝试收货，订单号：{}" ,Thread.currentThread().getName() , id);
        if (!sendEvent(OrderStatusChangeEvent.RECEIVED, order)) {
            log.error("线程名称：{},收货失败, 状态异常，订单信息：{}", Thread.currentThread().getName(), order);
            throw new RuntimeException("收货失败, 订单状态异常");
        }
        return order;
    }
    /**
     * 发送订单状态转换事件
     * synchronized修饰保证这个方法是线程安全的
     *
     * @param changeEvent
     * @param order
     * @return
     */
//    private synchronized boolean sendEvent(OrderStatusChangeEvent changeEvent, Order order) {
//        boolean result = false;
//        try {
//            //启动状态机
//            orderStateMachine.start();
//            //尝试恢复状态机状态
//            stateMachineMemPersister.restore(orderStateMachine, String.valueOf(order.getId()));
//            Message message = MessageBuilder.withPayload(changeEvent).setHeader("order", order).build();
//            result = orderStateMachine.sendEvent(message);
//            //持久化状态机状态
//            stateMachineMemPersister.persist(orderStateMachine, String.valueOf(order.getId()));
//        } catch (Exception e) {
//            log.error("订单操作失败:{}", e);
//        } finally {
//            orderStateMachine.stop();
//        }
//        return result;
//    }

    public synchronized boolean sendEvent(OrderStatusChangeEvent event, Order order){
        boolean result = false;
        try {
            //从工厂中获取stateMachine
            orderStateMachine.start();
            //重置状态机
            stateMachineMemPersister.restore(orderStateMachine, String.valueOf(order.getId()));
            //构建消息体
            Map<String,Object> param = new HashMap<>();
            param.put("order", order);
            Message<OrderStatusChangeEvent> message = builderEventsMessage(event, param);
            //向状态机发送消息
            result = orderStateMachine.sendEvent(message);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return result;
    }

    /**
     * 构建事件的消息体
     *
     * @param events       事件
     * @param headers 参数
     * @return Message<Events>
     */
    private Message<OrderStatusChangeEvent> builderEventsMessage(OrderStatusChangeEvent events, Map<String,Object> headers) {


        return new Message<OrderStatusChangeEvent>() {
            @Override
            public OrderStatusChangeEvent getPayload() {
                //执行的事件
                return events;
            }

            @Override
            public MessageHeaders getHeaders() {
                return new MessageHeaders(headers);
            }
        };
    }

}
