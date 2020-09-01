package com.beyond.cloud.svc.order.serivce.impl;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.order.domain.entity.Order;
import com.beyond.cloud.svc.order.mapper.OrderMapper;
import com.beyond.cloud.svc.order.serivce.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author beyond
 * @date 2020/7/27 14:41
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    public OrderServiceImpl(final OrderMapper orderMapper) {this.orderMapper = orderMapper;}

    @Override
    @Transactional
    public ApiResult<Order> createOrder(final String userId, final String commodityCode, final int count) {
        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(calculate(commodityCode, count));
        orderMapper.insertSelective(order);
        return ApiResult.ok(order);
    }

    private Integer calculate(final String commodityCode, final int count) {
        // 演示数据，直接返回 count
        return count;
    }
}
