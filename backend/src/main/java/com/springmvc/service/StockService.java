package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.SupplierDAO;
import com.springmvc.dao.SupplierMaterialDAO;
import com.springmvc.dto.*;
import com.springmvc.exception.BadRequestException;
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
    private SupplierMaterialDAO suppliermaterialDAO;

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
    public PageMode<SupplierMaterial> findsupplierPrice(Integer materialId, Integer current, Integer limit, String sortColumn, String sort) {
        SupplierMaterialQuery suppliermaterialQuery = new SupplierMaterialQuery();
        suppliermaterialQuery.setOffset((current-1) * limit);
        suppliermaterialQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn))
            suppliermaterialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        SupplierMaterialQuery.Criteria criteria = suppliermaterialQuery.or();
        criteria.andMaterialIdEqualTo(materialId);


        List<SupplierMaterial> resultSM = suppliermaterialDAO.selectByExample(suppliermaterialQuery)  ;

        //获取supplier
        SupplierQuery supplierQuery = new SupplierQuery();
        List<Supplier> resultsup = supplierDAO.selectByExample(supplierQuery)  ;

        HashMap<Integer,Supplier> suppliers=new HashMap<Integer, Supplier>();
        for(Supplier s:resultsup){
            suppliers.put(s.getSupplierId(),s);
        }

        //

        //完善suppliMaterial信息
        List<SupplierMaterial> resultms=new ArrayList<SupplierMaterial>();
        for(SupplierMaterial sm:resultSM){
            MaterialQuery materialQuery = new MaterialQuery();
            materialQuery.or().andMaterialIdEqualTo(sm.getMaterialId());
            List<Material> materialList = materialDAO.selectByExample(materialQuery);
            Material material = materialList.get(0);
            sm.setMaterialName(material.getMaterialName());
            sm.setMaterialNo(material.getMaterialNo());
            sm.setSupplierName(suppliers.get(sm.getSupplierId()).getSupplierName());
            resultms.add(sm);
        }
        return new PageMode<SupplierMaterial>(resultms, suppliermaterialDAO.countByExample(suppliermaterialQuery));
    }

    /**
     * 获取缺料情况
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchkey 只允许通过materialNO进行查询
     * @return
     */

    public PageMode<MaterialLack> getMaterialLack(Integer current, Integer limit, String sortColumn, String sort,String searchkey) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.setOffset((current-1) * limit);
        materialQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn))
            materialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        MaterialQuery.Criteria criteria = materialQuery.or();
        if (!ParamUtils.isNull(searchkey)) {
            criteria.andMaterialNoEqualTo(searchkey);
        }


        List<MaterialLack> resultML = materialDAO.selectWithLackByExample(materialQuery)  ;
        return new PageMode<MaterialLack>(resultML, materialDAO.countByExample(materialQuery));
    }
}
