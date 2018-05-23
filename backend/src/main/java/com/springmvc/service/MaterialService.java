package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dto.Material;
import com.springmvc.dto.PageMode;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("MaterialService")
@Transactional
public class MaterialService extends BaseService {
    @Resource
    MaterialDAO materialDAO;

    /**
     * 查询物料信息（分页）.
     *
     * 信息来自：material（同时包含总记录数）
     * 搜索字段：物料编号、物料名称
     * (未完成)筛选：分类id
     *
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @param closed
     * @return
     */
    public PageMode<Material> pageAdmin(Integer current, Integer limit, String sortColumn, String sort,
                                        String searchKey, Integer closed) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.setOffset((current-1) * limit);
        materialQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            materialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }

        MaterialQuery.Criteria criteria = materialQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andMaterialNoLike("%" + searchKey + "%");
        }
        criteria = materialQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andMaterialNameLike("%" + searchKey + "%");
        }

        List<Material> result = materialDAO.selectWithCategoryNameByExample(materialQuery);

        return new PageMode<Material>(result, materialDAO.countByExample(materialQuery));
    }

}
