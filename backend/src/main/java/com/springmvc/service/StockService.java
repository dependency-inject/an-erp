package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.ProductDAO;
import com.springmvc.dto.MaterialStockRecord;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.ProductStockRecord;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.pojo.ProductQuery;
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
}
