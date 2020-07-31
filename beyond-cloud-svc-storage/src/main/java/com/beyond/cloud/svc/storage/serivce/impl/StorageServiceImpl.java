package com.beyond.cloud.svc.storage.serivce.impl;

import java.util.Objects;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.storage.domain.entity.Storage;
import com.beyond.cloud.svc.storage.mapper.StorageMapper;
import com.beyond.cloud.svc.storage.serivce.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author beyond
 * @date 2020/7/27 14:48
 */
@Service
public class StorageServiceImpl implements StorageService {

    private final StorageMapper storageMapper;

    public StorageServiceImpl(final StorageMapper storageMapper) {this.storageMapper = storageMapper;}

    @Override
    @Transactional
    public ApiResult<Boolean> deduct(final String commodityCode, final int count) {
        Storage storage = storageMapper.getByCommodityCode(commodityCode);
        if (Objects.isNull(storage) || count > storage.getCount()) {
            throw new RuntimeException("库存不足");
        }
        storage.setCount(storage.getCount() - count);
        storageMapper.updateByPrimaryKey(storage);
        return ApiResult.ok(Boolean.TRUE);
    }
}
