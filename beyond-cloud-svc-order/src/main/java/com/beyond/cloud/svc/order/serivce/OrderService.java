package com.beyond.cloud.svc.order.serivce;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.order.domain.entity.Order;

/**
 * @author beyond
 * @date 2020/7/27 13:35
 */
public interface OrderService {

    ApiResult<Order> createOrder(String userId, String commodityCode, int count);

}
