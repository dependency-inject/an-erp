package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.SupplierDAO;
import com.springmvc.dao.SupplierMaterialDAO;
import com.springmvc.dto.*;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.pojo.SupplierMaterialQuery;
import com.springmvc.pojo.SupplierQuery;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("StockService")
@Transactional
public class StockService extends BaseService {

    @Resource
    private SupplierMaterialDAO supplierMaterialDAO;

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private SupplierDAO supplierDAO;

    /**
     * 反查物料报价信息
     * @param materialId
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @return
     */
    public PageMode<SupplierMaterial> findSupplierPrice(Integer materialId, Integer current, Integer limit, String sortColumn, String sort) {
        SupplierMaterialQuery suppliermaterialQuery = new SupplierMaterialQuery();
        suppliermaterialQuery.setOffset((current-1) * limit);
        suppliermaterialQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn))
            suppliermaterialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        SupplierMaterialQuery.Criteria criteria = suppliermaterialQuery.or();
        criteria.andMaterialIdEqualTo(materialId);

        List<SupplierMaterial> result = supplierMaterialDAO.selectByExample(suppliermaterialQuery);

        // 完善supplierMaterial信息
        for (SupplierMaterial supplierMaterial: result) {
            Material material = materialDAO.selectByPrimaryKey(supplierMaterial.getMaterialId());
            if (material != null) {
                supplierMaterial.setMaterialNo(material.getMaterialNo());
                supplierMaterial.setMaterialName(material.getMaterialName());
            }
            Supplier supplier = supplierDAO.selectByPrimaryKey(supplierMaterial.getSupplierId());
            if (supplier != null) {
                supplierMaterial.setSupplierName(supplier.getSupplierName());
            }
        }
        return new PageMode<SupplierMaterial>(result, supplierMaterialDAO.countByExample(suppliermaterialQuery));
    }

    /**
     * 获取缺料情况
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @return
     */

    public PageMode<MaterialLack> getMaterialLack(Integer current, Integer limit, String sortColumn, String sort,String searchKey) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.setOffset((current-1) * limit);
        materialQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn))
            materialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        // 搜物料编号
        MaterialQuery.Criteria criteria = materialQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andMaterialNoLike("%" + searchKey + "%");
        }
        // 搜物料名称
        criteria = materialQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andMaterialNameEqualTo("%" + searchKey + "%");
        }

        List<MaterialLack> resultML = materialDAO.selectWithLackByExample(materialQuery)  ;
        return new PageMode<MaterialLack>(resultML, materialDAO.countByExample(materialQuery));
    }
}
