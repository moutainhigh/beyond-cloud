package com.beyond.cloud.svc.order.client.account;

import com.beyond.cloud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author beyond
 * @date 2020/7/27 15:07
 */
@FeignClient(value = "beyond-cloud-svc-account")
public interface AccountClient {

    @PostMapping(path = "/api/account/debit")
    ApiResult debit(@RequestParam(value = "userId") String userId,
                    @RequestParam(value = "money") int money);

}
