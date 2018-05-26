package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.Material;
import com.springmvc.dto.PageMode;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
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
    public PageMode<Material> pageMaterial(Integer current, Integer limit, String sortColumn, String sort,
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

    public void removeMaterial(List<Integer> integers) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.or().andMaterialIdIn(integers);
        materialDAO.deleteByExample(materialQuery);
        addLog(LogType.MATERIAL, Operate.REMOVE, integers);
    }

    public Material getMaterialWithCategoryById(Integer materialId) {
        MaterialQuery adminQuery = new MaterialQuery();
        adminQuery.or().andMaterialIdEqualTo(materialId);
        List<Material> materialList = materialDAO.selectWithCategoryNameByExample(adminQuery);
        if (materialList.size() == 0) {
            return null;
        }
        Material material = materialList.get(0);
        return material;
    }

    public Material addMaterial(String materialNo, String materialName, String unit, int categoryId, String spec,
                                BigDecimal cost, String remark) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.or().andMaterialNameEqualTo(materialNo);
        if (materialDAO.countByExample(materialQuery) > 0) {
            throw new BadRequestException(MATERIAL_NO_EXIST);
        }

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Material material = new Material();
        material.setMaterialNo(materialNo);
        material.setMaterialName(materialName);
        material.setUnit(unit);
        material.setCategoryId(categoryId);
        material.setSpec(spec);
        material.setCost(cost);
        material.setRemark(remark);
        material.setCreateAt(new Date());
        material.setCreateBy(loginAdmin.getAdminId());
        material.setUpdateAt(new Date());
        material.setUpdateBy(loginAdmin.getAdminId());
        materialDAO.insertSelective(material);

        addLog(LogType.MATERIAL, Operate.ADD, material.getMaterialId());
        return getMaterialWithCategoryById(material.getMaterialId());
    }



    private static final String MATERIAL_NO_EXIST = "物料编号已存在";

    public Material updateMaterial(Integer materialId, String materialNo, String materialName, String unit,
                                   int categoryId, String spec, BigDecimal cost, String remark) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Material material = new Material();
        material.setMaterialId(materialId);
        material.setMaterialNo(materialNo);
        material.setMaterialName(materialName);
        material.setUnit(unit);
        material.setCategoryId(categoryId);
        material.setSpec(spec);
        material.setCost(cost);
        material.setRemark(remark);
        material.setUpdateAt(new Date());
        material.setUpdateBy(loginAdmin.getAdminId());
        materialDAO.updateByPrimaryKeySelective(material);

        addLog(LogType.MATERIAL, Operate.UPDATE, material.getMaterialId());
        return getMaterialWithCategoryById(materialId);
    }
}
