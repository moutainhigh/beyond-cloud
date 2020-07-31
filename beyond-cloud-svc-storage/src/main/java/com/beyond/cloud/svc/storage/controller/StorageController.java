package com.beyond.cloud.svc.storage.controller;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.svc.storage.serivce.StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author beyond
 * @date 2020/7/27 15:26
 */
@RestController
@RequestMapping("/api")
public class StorageController {

    private final StorageService storageService;

    public StorageController(final StorageService storageService) {this.storageService = storageService;}

    @PostMapping("/storage")
    public ApiResult<Boolean> deduct(@RequestParam("commodityCode") String commodityCode,
                                     @RequestParam("count") int count) {
        return storageService.deduct(commodityCode, count);
    }

}
