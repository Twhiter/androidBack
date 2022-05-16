package com.springtest.demo.service;

import com.springtest.demo.dao.MerchantExportUserDao;
import com.springtest.demo.dao.UserExportMerchantDao;
import com.springtest.demo.dto.ExportOrImport;
import com.springtest.demo.dto.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportAndImportService {

    @Autowired
    private UserExportMerchantDao userExportMerchantDao;

    @Autowired
    private MerchantExportUserDao merchantExportUserDao;


    public Page<ExportOrImport> getAllExports(int pageSize, int pageNum) {

        Page<ExportOrImport> pageObj = new Page<>();
        pageObj.currentPage = pageNum;
        pageObj.pageSize = pageSize;


        pageObj.data = userExportMerchantDao.getAllExports(pageSize, pageNum);

        int count = Math.toIntExact(userExportMerchantDao.selectCount(null));

        pageObj.maxPage = (int) Math.ceil(1.0 * count / pageSize);

        return pageObj;
    }

    public Page<ExportOrImport> getAllImports(int pageSize, int pageNum) {

        Page<ExportOrImport> pageObj = new Page<>();
        pageObj.currentPage = pageNum;
        pageObj.pageSize = pageSize;


        pageObj.data = merchantExportUserDao.getAllImports(pageSize, pageNum);

        int count = Math.toIntExact(merchantExportUserDao.selectCount(null));

        pageObj.maxPage = (int) Math.ceil(1.0 * count / pageSize);

        return pageObj;
    }


}
