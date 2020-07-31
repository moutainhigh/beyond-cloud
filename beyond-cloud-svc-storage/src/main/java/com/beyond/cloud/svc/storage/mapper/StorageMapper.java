package com.beyond.cloud.svc.storage.mapper;

import com.beyond.cloud.storage.domain.entity.Storage;
import com.beyond.cloud.svc.storage.mapper.base.StorageBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author beyond
 * @date 2020/7/27 14:50
 */
@Mapper
public interface StorageMapper extends StorageBaseMapper {

    Storage getByCommodityCode(@Param("commodityCode") String commodityCode);

}
