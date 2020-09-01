package com.beyond.cloud.svc.account.mapper.ext;

import com.beyond.cloud.account.domain.entity.Account;
import com.beyond.cloud.svc.account.mapper.AccountMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapperExt extends AccountMapper {

    Account selectAccountByUserId(@Param("userId") String userId);

}
