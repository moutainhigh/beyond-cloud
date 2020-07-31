package com.beyond.cloud.svc.storage.serivce;


import com.beyond.cloud.common.ApiResult;

/**
 * @author beyond
 * @date 2020/7/27 14:47
 */
public interface StorageService {

    ApiResult<Boolean> deduct(String commodityCode, int count);

}
