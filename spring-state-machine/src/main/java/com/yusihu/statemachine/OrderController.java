package com.yusihu.statemachine;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yusihu
 * @date 2024-06-04 19:44
 */

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderServiceImpl orderService;
    /**
     * 根据id查询订单
     *
     * @return
     */
    @RequestMapping("/getById")
    public Order getById(@RequestParam("id") Integer id) {
        //根据id查询订单

        return orderService.getById(id);
    }
    /**
     * 创建订单
     *
     * @return
     */
    @RequestMapping("/create")
    public String create(@RequestBody Order order) {
        //创建订单
        orderService.create(order);
        return "sucess";
    }
    /**
     * 对订单进行支付
     *
     * @param id
     * @return
     */
    @RequestMapping("/pay")
    public String pay(@RequestParam("id") Integer id) {
        //对订单进行支付
        orderService.pay(id);
        return "success";
    }

    /**
     * 对订单进行发货
     *
     * @param id
     * @return
     */
    @RequestMapping("/deliver")
    public String deliver(@RequestParam("id") Integer id) {
        //对订单进行确认收货
        orderService.deliver(id);
        return "success";
    }
    /**
     * 对订单进行确认收货
     *
     * @param id
     * @return
     */
    @RequestMapping("/receive")
    public String receive(@RequestParam("id") Integer id) {
        //对订单进行确认收货
        orderService.receive(id);
        return "success";
    }
}
