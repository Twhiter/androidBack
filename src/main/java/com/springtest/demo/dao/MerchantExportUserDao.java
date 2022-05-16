package com.springtest.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springtest.demo.dto.ExportOrImport;
import com.springtest.demo.entity.MerchantExportUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MerchantExportUserDao extends BaseMapper<MerchantExportUser> {


    @Select("select *, export_id as id,meu.merchant_id as `from`, m.merchant_user_id as `to` " +
            " from merchant_export_user meu join merchant m on meu.merchant_id = m.merchant_id " +
            " limit ${(pageNumber - 1) * pageSize},#{pageSize}")
    List<ExportOrImport> getAllImports(int pageSize, int pageNumber);

}
