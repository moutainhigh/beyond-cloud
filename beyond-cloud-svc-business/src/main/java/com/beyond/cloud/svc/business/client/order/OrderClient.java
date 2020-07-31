package com.beyond.cloud.svc.business.client.order;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.order.domain.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author beyond
 * @date 2020/7/27 15:07
 */
@FeignClient(value = "beyond-cloud-svc-order")
public interface OrderClient {

    @PostMapping(path = "/api/order")
    ApiResult<Order> createOrder(@RequestParam(value = "userId") int userId,
                                 @RequestParam(value = "commodityCode") String commodityCode,
                                 @RequestParam(value = "count")  int count);

}
