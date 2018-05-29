package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dto.MaterialStockCostRecord;
import com.springmvc.dto.PageMode;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("StockService")
@Transactional
public class StockService extends BaseService {

    @Resource
    private MaterialDAO materialDAO;

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
