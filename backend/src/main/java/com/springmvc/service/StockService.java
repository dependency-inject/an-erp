package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.SupplierDAO;
import com.springmvc.dao.SupplierMaterialDAO;
import com.springmvc.dto.*;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.dao.ProductDAO;
import com.springmvc.pojo.ProductQuery;
import com.springmvc.pojo.SupplierMaterialQuery;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Service("StockService")
@Transactional
public class StockService extends BaseService {

    @Resource
    private ProductDAO productDAO;

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private SupplierMaterialDAO supplierMaterialDAO;

    @Resource
    private SupplierDAO supplierDAO;

    /**
     * 查询货品库存信息
     * <p>
     * 进行必要的检查：login_name 是否已存在
     * 主表信息：product_outstock_bill; product_instock_bill
     * 关联的从表信息：product_outstock_bill_product; product_instock_bill_product
     * 日志信息：无
     *
     * @param current    当前页
     * @param limit      每页显示条数
     * @param sortColumn 依据什么字段排序
     * @param sort       正序或倒序
     * @param searchKey  搜索的关键字，货品号码或货品名称，模糊搜索
     * @return 统计出的货品信息列表
     */
    public PageMode<ProductStockRecord> pageProduct(@RequestParam Integer current, @RequestParam Integer limit,
                                                    String sortColumn, String sort, String searchKey) {
        ProductQuery productQuery = new ProductQuery();
        productQuery.setOffset((current - 1) * limit);
        productQuery.setLimit(limit);

        if (!ParamUtils.isNull(sortColumn)) {
            productQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }

        ProductQuery.Criteria criteria = productQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andProductNoLike("%" + searchKey + "%");
        }

        criteria = productQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andProductNameLike("%" + searchKey + "%");
        }
        List<ProductStockRecord> result = productDAO.selectProductStockByExample(productQuery);
        return new PageMode<ProductStockRecord>(result, productDAO.statisticsProductStockByExample(productQuery),
                productDAO.countByExample(productQuery));
    }


    /**
     * 同上，对库存材料的统计
     *
     * @param current    当前页
     * @param limit      每页数量
     * @param sortColumn 排序依据
     * @param sort       正反排序
     * @param searchKey  模糊搜索关键字
     * @return 库存物料统计
     */
    public PageMode<MaterialStockRecord> pageMaterial(@RequestParam Integer current, @RequestParam Integer limit,
                                                      String sortColumn, String sort, String searchKey) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.setOffset((current - 1) * limit);
        materialQuery.setLimit(limit);

        if (!ParamUtils.isNull(sortColumn)) {
            materialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }

        MaterialQuery.Criteria criteria = materialQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andMaterialNameLike("%" + searchKey + "%");
        }
        criteria = materialQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andMaterialNoLike("%" + searchKey + "%");
        }
        List<MaterialStockRecord> result = materialDAO.selectWithStockByExample(materialQuery);
        return new PageMode<MaterialStockRecord>(result, materialDAO.statisticsWithStockByExample(materialQuery),
                materialDAO.countByExample(materialQuery));
    }

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

    /**
     * 查询物料成本、数量信息（分页）
     *
     * 将主表信息取出：material（同时包含总记录数）
     * 搜索字段：物料编号、物料名称
     *
     *
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @return
     */
    public PageMode<MaterialStockCostRecord> pageMaterialCost(Integer current, Integer limit,
                                                              String sortColumn, String sort, String searchKey){

        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.setOffset((current-1)*limit);
        materialQuery.setLimit(limit);
        if(!ParamUtils.isNull(sortColumn)) {
            materialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn)+" "+sort);
        }
        //搜索物料编号
        MaterialQuery.Criteria criteria = materialQuery.or();
        if(!ParamUtils.isNull(searchKey)) {
            criteria.andMaterialNoLike("%"+searchKey+"%");
        }
        //搜索物料名称
        criteria = materialQuery.or();
        if(!ParamUtils.isNull(searchKey)) {
            criteria.andMaterialNameLike("%"+searchKey+"%");
        }

        List<MaterialStockCostRecord> result = materialDAO.selectWithStockCostByExample(materialQuery);

        return new PageMode<MaterialStockCostRecord>(result, materialDAO.statisticsWithStockCostByExample(materialQuery),
                materialDAO.countByExample(materialQuery));
    }
}