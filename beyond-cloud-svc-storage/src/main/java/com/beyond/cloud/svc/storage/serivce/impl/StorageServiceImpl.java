package com.beyond.cloud.svc.storage.serivce.impl;

import java.util.Objects;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.exception.BusinessException;
import com.beyond.cloud.storage.domain.entity.Storage;
import com.beyond.cloud.svc.storage.mapper.ext.StorageMapperExt;
import com.beyond.cloud.svc.storage.serivce.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author beyond
 * @date 2020/7/27 14:48
 */
@Service
public class StorageServiceImpl implements StorageService {

    private final StorageMapperExt storageMapperExt;

    public StorageServiceImpl(final StorageMapperExt storageMapperExt) {this.storageMapperExt = storageMapperExt;}

    @Override
    @Transactional
    public ApiResult<Boolean> deduct(final String commodityCode, final int count) {
        Storage storage = storageMapperExt.getByCommodityCode(commodityCode);
        if (Objects.isNull(storage) || count > storage.getCount()) {
            throw new BusinessException("库存不足");
        }
        storage.setCount(storage.getCount() - count);
        storageMapperExt.updateByPrimaryKey(storage);
        return ApiResult.ok(Boolean.TRUE);
    }
}
