package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.RoleDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.Material;
import com.springmvc.dto.PageMode;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.pojo.RoleQuery;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("MaterialService")
@Transactional
public class MaterialService extends BaseService {

    @Resource
    private MaterialDAO materialDAO;

    /**
     * 查询物料信息（分页）
     *
     * 将主表信息取出：material（同时包含总记录数）
     * 搜索字段：物料编号、物料名称
     * 筛选字段：暂无
     *
     *
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @return
     */
    public PageMode<Material> pageMaterial(Integer current, Integer limit,
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

        List<Material> result = materialDAO.selectByExample(materialQuery);

        return new PageMode<Material>(result, materialDAO.countByExample(materialQuery));
    }

    /**
     * 查询物料信息（单个）
     *
     * 将主表信息取出：material
     *
     * @param materialId
     * @return
     */
    public Material getMaterialById (int materialId) {
        Material material = materialDAO.selectByPrimaryKey(materialId);
        return material;
    }

    /**
     * 更新物料成本信息
     *
     * 更新主表信息：material
     * 更新关联的从表信息：暂无
     * 添加日志信息：LogType.MATERIAL, Operate.UPDATE
     *
     * @param materialId
     * @param cost
     * @return
     */

    public Material updateCost(Integer materialId, BigDecimal cost) {

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Material material = new Material();
        material.setMaterialId(materialId);
        material.setCost(cost);
        material.setUpdateAt(new Date());
        material.setUpdateBy(loginAdmin.getAdminId());
        materialDAO.updateByPrimaryKeySelective(material);
        // 添加日志
        addLog(LogType.MATERIAL, Operate.UPDATE, material.getMaterialId());
        return getMaterialById(material.getMaterialId());
    }

}
