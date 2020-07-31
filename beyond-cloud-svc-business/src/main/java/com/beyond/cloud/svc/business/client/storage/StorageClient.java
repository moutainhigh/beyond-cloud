package com.beyond.cloud.svc.business.client.storage;

import com.beyond.cloud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author beyond
 * @date 2020/7/27 15:07
 */
@FeignClient(value = "beyond-cloud-svc-storage")
public interface StorageClient {

    @PostMapping(path = "/api/storage")
    ApiResult<Boolean> deduct(@RequestParam(value = "commodityCode") String commodityCode,
                              @RequestParam(value = "count") int count);

}
