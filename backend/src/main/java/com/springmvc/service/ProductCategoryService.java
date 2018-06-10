package com.springmvc.service;

import com.springmvc.dao.ProductCategoryDAO;
import com.springmvc.dao.ProductDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.ProductCategory;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.ProductCategoryQuery;
import com.springmvc.pojo.ProductQuery;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("ProductCategoryService")
@Transactional
public class ProductCategoryService extends BaseService {

    // DAO变量声明
    @Resource
    private ProductCategoryDAO productCategoryDAO;

    @Resource
    private ProductDAO productDAO;

    /**
     * 获取货品类别表的所有信息
     *
     * @return 返回列表
     */
    public List<ProductCategory> getList() {
        return productCategoryDAO.selectByExample(new ProductCategoryQuery());
    }

    /**
     * 根据id获取一个类别信息
     *
     * @param categoryId 类别id
     * @return 返回记录
     */
    public ProductCategory getProductCategoryById(int categoryId) {
        return productCategoryDAO.selectByPrimaryKey(categoryId);
    }

    /**
     * 新增货品类别信息
     *
     * @param categoryName 类别名称
     * @param parentId 父类别id
     * @return 返回新建的类别信息
     */
    public ProductCategory addProductCategory(String categoryName, Integer parentId){
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        // 构建productCategory
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName(categoryName);
        productCategory.setParentId(parentId);
        productCategory.setCreateAt(new Date());
        productCategory.setCreateBy(loginAdmin.getAdminId());
        productCategory.setUpdateAt(new Date());
        productCategory.setUpdateBy(loginAdmin.getAdminId());

        // 添加productCategory
        productCategoryDAO.insertSelective(productCategory);

        // 添加日志
        addLog(LogType.PRODUCT_CATEGORY, Operate.ADD, productCategory.getCategoryId());

        return getProductCategoryById(productCategory.getCategoryId());
    }

    /**
     * 更新货品类别信息
     *
     * @param categoryId 待更新的类别id
     * @param categoryName 类别名称
     * @param parentId 父类别id
     * @return 返回更新的类别信息
     */
    public ProductCategory updateProductCategory(Integer categoryId, String categoryName, Integer parentId){
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        // 构建相同id的productCategory
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(categoryId);
        productCategory.setCategoryName(categoryName);
        // 不更新上级类别信息
//        productCategory.setParentId(parentId);
        productCategory.setUpdateAt(new Date());
        productCategory.setUpdateBy(loginAdmin.getAdminId());

        // 更新数据
        productCategoryDAO.updateByPrimaryKeySelective(productCategory);

        // 添加日志信息
        addLog(LogType.PRODUCT_CATEGORY, Operate.UPDATE, productCategory.getCategoryId());

        return getProductCategoryById(productCategory.getCategoryId());
    }

    /**
     * 删除货品类别信息
     *
     * @param idList 待删除的类别id列表
     */
    public void removeProductCategory(List<Integer> idList){
        // 检查是否被product引用
        ProductQuery productQuery = new ProductQuery();
        productQuery.or().andCategoryIdIn(idList);
        if(productDAO.countByExample(productQuery) > 0){
            throw new BadRequestException(PRODUCTCATEGORY_REFEREF_BY_PRODUCT);
        }

        // 检查是否拥有子类
        ProductCategoryQuery productCategoryQuery = new ProductCategoryQuery();
        productCategoryQuery.or().andParentIdIn(idList);
        if(productCategoryDAO.countByExample(productCategoryQuery) > 0){
            throw new BadRequestException(CANNOT_REMOVE_CATEGORY_NOT_EMPTY);
        }

        // 删除数据
        ProductCategoryQuery productCategoryQueryDelete = new ProductCategoryQuery();
        productCategoryQueryDelete.or().andCategoryIdIn(idList);
        productCategoryDAO.deleteByExample(productCategoryQueryDelete);

        // 添加日志
        addLog(LogType.PRODUCT_CATEGORY, Operate.REMOVE, idList);
    }

    private static final String PRODUCTCATEGORY_REFEREF_BY_PRODUCT = "货品类别被货品表引用";
    private static final String CANNOT_REMOVE_CATEGORY_NOT_EMPTY = "不能删除含有子类的类别";
}
