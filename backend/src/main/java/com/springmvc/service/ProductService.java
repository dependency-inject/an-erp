package com.springmvc.service;

import com.springmvc.dao.*;
import com.springmvc.dto.*;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.*;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.springmvc.service.BaseService.LogType.PRODUCT;

@Service("ProductService")
@Transactional
public class ProductService extends BaseService {

    @Resource
    private ProductDAO productDAO;

    @Resource
    private ProductMaterialDAO productMaterialDAO;

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private OrderBillProductDAO orderBillProductDAO;

    @Resource
    private ProductInstockBillProductDAO productInstockBillProductDAO;

    @Resource
    private ProductOutstockBillProductDAO productOutstockBillProductDAO;

    @Resource
    private ProductCategoryDAO productCategoryDAO;

    /**
     * 获取货品表的所有信息
     *
     * @return 返回列表
     */
    public List<Product> getList(Integer closed) {
        ProductQuery productQuery = new ProductQuery();
        if (!ParamUtils.isNull(closed) && !closed.equals(-1)) {
            productQuery.or().andClosedEqualTo(closed > 0);
        }
        return productDAO.selectByExample(productQuery);
    }

    /**
     * 查询单个货品物料组成信息
     *
     * 将物料信息补全：
     * material.material_no
     * material.material_name
     * material.unit
     * material.category_id
     * material.spec
     *
     * @param productId
     * @return productMaterialList
     */
    public Product getProductWithMaterialById(int productId) {

        Product product = productDAO.selectByPrimaryKey(productId);
        if (product == null) {
            return null;
        }
        ProductMaterialQuery productMaterialQuery = new ProductMaterialQuery();
        productMaterialQuery.or().andProductIdEqualTo(productId);
        List<ProductMaterial> productMaterialList = productMaterialDAO.selectByExample(productMaterialQuery);
        for (ProductMaterial productMaterial:productMaterialList) {
            Material material = materialDAO.selectByPrimaryKey(productMaterial.getMaterialId());
            productMaterial.setMaterialNo(material.getMaterialNo());
            productMaterial.setMaterialName(material.getMaterialName());
            productMaterial.setUnit(material.getUnit());
            productMaterial.setCategoryId(material.getCategoryId());
            productMaterial.setSpec(material.getSpec());
        }
        product.setProductMaterialList(productMaterialList);
        return product;
    }

    /**
     * 新增货品
     *
     * 将主表信息保存：product
     * 将关联的从表信息保存：product_material
     * 添加日志信息：LogType.PRODUCT, Operate.ADD
     *
     * @param productNo
     * @param productName
     * @param unit
     * @param categoryId
     * @param spec
     * @param price
     * @param closed
     * @param remark
     * @param productMaterialList
     * @return
     */
    public Product addProduct(String productNo, String productName, String unit, Integer categoryId, String spec, BigDecimal price, Boolean closed, String remark,
                              List<ProductMaterial> productMaterialList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        Product product = new Product();
        product.setProductNo(productNo);
        product.setProductName(productName);
        product.setUnit(unit);
        product.setCategoryId(categoryId);
        product.setSpec(spec);
        product.setPrice(price);
        product.setClosed(closed);
        product.setRemark(remark);
        product.setCreateAt(new Date());
        product.setCreateBy(loginAdmin.getAdminId());
        product.setUpdateAt(new Date());
        product.setUpdateBy(loginAdmin.getAdminId());
        productDAO.insertSelective(product);

        // 新增关联 product_material
        for(ProductMaterial productMaterial:productMaterialList) {
            productMaterial.setProductId(product.getProductId());
            productMaterialDAO.insertSelective(productMaterial);
        }

        // 添加日志
        addLog(LogType.PRODUCT, Operate.ADD, product.getProductId());

        return getProductWithMaterialById(product.getProductId());
    }

    /**
     * 删除货品
     *
     * 检查要删除的主表信息是否被其他信息引用：order_bill_product, product_instock_bill_product, product_outstock_bill_product
     * 删除主表信息：product
     *
     * 删除关联的从表信息：product_material
     * 添加日志信息：LogType.PRODUCT, Operate.REMOVE
     *
     * @param productIdList
     */
    public void removeProduct(List<Integer> productIdList) {
        // 检查引用
        OrderBillProductQuery orderBillProductQuery = new OrderBillProductQuery();
        orderBillProductQuery.or().andProductIdIn(productIdList);
        if (orderBillProductDAO.countByExample(orderBillProductQuery) > 0) {
            throw new BadRequestException(PRODUCT_REFER_BY_ORDER);
        }

        ProductInstockBillProductQuery productInstockBillProductQuery = new ProductInstockBillProductQuery();
        productInstockBillProductQuery.or().andProductIdIn(productIdList);
        if (productInstockBillProductDAO.countByExample(productInstockBillProductQuery) > 0) {
            throw new BadRequestException(PRODUCT_REFER_BY_INSTOCK);
        }

        ProductOutstockBillProductQuery productOutstockBillProductQuery = new ProductOutstockBillProductQuery();
        productOutstockBillProductQuery.or().andProductIdIn(productIdList);
        if (productOutstockBillProductDAO.countByExample(productOutstockBillProductQuery) > 0) {
            throw new BadRequestException(PRODUCT_REFER_BY_OUTSTOCK);
        }

        //删除主表
        ProductQuery productQuery = new ProductQuery();
        productQuery.or().andProductIdIn(productIdList);
        productDAO.deleteByExample(productQuery);

        //删除从表
        ProductMaterialQuery productMaterialQuery = new ProductMaterialQuery();
        productMaterialQuery.or().andProductIdIn(productIdList);
        productMaterialDAO.deleteByExample(productMaterialQuery);

        // 添加日志
        addLog(LogType.PRODUCT, Operate.REMOVE, productIdList);
    }

    /**
     * 查询货品信息（分页）
     *
     * 将主表信息取出：product（同时包含总记录数）
     * 搜索字段：货品编号，货品名称
     * 筛选字段：货品状态
     * 过滤不显示的信息：productId
     *
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @param closed
     * @return
     */
    public PageMode<Product> pageProduct(Integer current, Integer limit, String sortColumn, String sort,
                                     String searchKey, Integer closed, Integer categoryId) {
        ProductQuery productQuery = new ProductQuery();
        productQuery.setOffset((current-1) * limit);
        productQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            productQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }
        // 类别筛选
        if (!ParamUtils.isNull(categoryId) && !categoryId.equals(-1)) {
            productQuery.setCategoryId(categoryId);
        }

        // 搜索货品编号
        ProductQuery.Criteria criteria = productQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andProductNoLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(closed) && !closed.equals(-1)) {
            criteria.andClosedEqualTo(closed > 0);
        }
        // 搜索货品名称
        criteria = productQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andProductNameLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(closed) && !closed.equals(-1)) {
            criteria.andClosedEqualTo(closed > 0);
        }

        List<Product> result = productDAO.selectWithCategoryNameByExample(productQuery);

        return new PageMode<Product>(result, productDAO.countByExample(productQuery));
    }

    /**
     * 更新货品信息
     *
     * 更新主表信息：product
     * 更新关联的从表信息：product_material
     * 添加日志信息：LogType.PRODUCT, Operate.UPDATE
     *
     * @param productId
     * @param productNo
     * @param productName
     * @param unit
     * @param categoryId
     * @param spec
     * @param price
     * @param closed
     * @param remark
     * @param productMaterialList
     * @return
     */
    public Product updateProduct(Integer productId, String productNo, String productName, String unit,
                                 Integer categoryId, String spec, BigDecimal price, Boolean closed,
                                 String remark, List<ProductMaterial> productMaterialList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        Product product = new Product();
        product.setProductId(productId);
        product.setProductNo(productNo);
        product.setProductName(productName);
        product.setUnit(unit);
        product.setCategoryId(categoryId);
        product.setSpec(spec);
        product.setPrice(price);
        product.setClosed(closed);
        product.setRemark(remark);
        product.setUpdateAt(new Date());
        product.setUpdateBy(loginAdmin.getAdminId());
        productDAO.updateByPrimaryKeySelective(product);

        // 先删除原来所有product_material
        ProductMaterialQuery productMaterialQuery = new ProductMaterialQuery();
        productMaterialQuery.or().andProductIdEqualTo(product.getProductId());
        productMaterialDAO.deleteByExample(productMaterialQuery);

        // 再新增现有关联product_material
        for (ProductMaterial productMaterial : productMaterialList) {
            productMaterial.setProductId(product.getProductId());
            productMaterialDAO.insertSelective(productMaterial);
        }

        // 添加日志
        addLog(LogType.PRODUCT, Operate.UPDATE, product.getProductId());
        return getProductWithMaterialById(product.getProductId());
    }

    /**
     * 更新货品状态信息
     *
     * 更新主表信息：product
     * 更新关联的从表信息：暂无
     * 添加日志信息：LogType.PRODUCT, Operate.UPDATE
     *
     * @param productIdList
     * @param closed
     */
    public void updateProductClosedState(List<Integer> productIdList, Boolean closed) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Product product = new Product();
        product.setClosed(closed);
        product.setUpdateAt(new Date());
        product.setUpdateBy(loginAdmin.getAdminId());

        ProductQuery productQuery = new ProductQuery();
        productQuery.or().andProductIdIn(productIdList);
        productDAO.updateByExampleSelective(product, productQuery);

        // 添加日志
        addLog(LogType.PRODUCT, Operate.UPDATE, productIdList);
    }

    private static final String PRODUCT_REFER_BY_ORDER = "货品被订单引用";
    private static final String PRODUCT_REFER_BY_INSTOCK = "货品被货品入库单引用";
    private static final String PRODUCT_REFER_BY_OUTSTOCK = "货品被货品出库单引用";
}

