package com.beyond.cloud.svc.order.controller;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.order.domain.entity.Order;
import com.beyond.cloud.svc.order.serivce.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author beyond
 * @date 2020/7/27 15:11
 */
@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {this.orderService = orderService;}

    @PostMapping("/order")
    public ApiResult<Order> createOrder(@RequestParam("userId") String userId,
                                        @RequestParam("commodityCode") String commodityCode,
                                        @RequestParam("count") int count) {
        return orderService.createOrder(userId, commodityCode, count);
    }

}
