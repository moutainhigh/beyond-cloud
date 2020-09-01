package com.beyond.cloud.svc.storage.mapper.ext;

import com.beyond.cloud.storage.domain.entity.Storage;
import com.beyond.cloud.svc.storage.mapper.StorageMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StorageMapperExt extends StorageMapper {

    Storage getByCommodityCode(@Param("commodityCode") String commodityCode);

}
