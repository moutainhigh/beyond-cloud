package com.beyond.cloud.svc.business.service;


import com.beyond.cloud.common.ApiResult;

/**
 * @author beyond
 * @date 2020/7/27 15:32
 */
public interface BusinessService {

    ApiResult purchase(int userId, String commodityCode, int orderCount);
}
