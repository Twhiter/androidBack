package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.dto.ExportOrImport;
import com.springtest.demo.entity.UserExportMerchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserExportMerchantDao extends BaseMapper<UserExportMerchant> {


    @Select("select *,export_id as id,user_id as `from`,m.merchant_id as `to` from " +
            "user_export_merchant uem join merchant m on uem.user_id=m.merchant_user_id " +
            "limit ${(pageNumber - 1) * pageSize},#{pageSize}")
    List<ExportOrImport> getAllExports(int pageSize, int pageNumber);


}
