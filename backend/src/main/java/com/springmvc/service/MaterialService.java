package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.Material;
import com.springmvc.dto.PageMode;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("MaterialService")
@Transactional
public class MaterialService extends BaseService {
    @Resource
    MaterialDAO materialDAO;

    /**
     * 查询物料信息（分页），包括分类名称.
     *
     * 信息来自：material left join material_category（同时包含总记录数）
     * 搜索字段：物料编号、物料名称
     *
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @param categoryId
     * @return
     */
    public PageMode<Material> pageMaterial(Integer current, Integer limit, String sortColumn, String sort,
                                           String searchKey, Integer categoryId) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.setOffset((current-1) * limit);
        materialQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            materialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }
        if (!ParamUtils.isNull(categoryId) && !categoryId.equals(-1)) {
            materialQuery.setCategoryId(categoryId);
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

    /**
     * 删除物料信息.
     *
     * 传入要删除的物料id
     * 从表中删除：material
     * 添加日志信息：LogType.MATERIAL, Operate.REMOVE
     *
     * @param integers
     */
    public void removeMaterial(List<Integer> integers) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.or().andMaterialIdIn(integers);
        materialDAO.deleteByExample(materialQuery);
        addLog(LogType.MATERIAL, Operate.REMOVE, integers);
    }

    /**
     * 查询物料信息（单个）.
     *
     * 查询表：material
     *
     * @param materialId
     * @return
     */
    public Material getMaterialById(Integer materialId) {
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.or().andMaterialIdEqualTo(materialId);
        Material material = materialDAO.selectByPrimaryKey(materialId);
        return material;
    }

    /**
     * 查询物料信息（单个），包括分类名称.
     *
     * 查询表：material left join material_category
     *
     * @param materialId
     * @return
     */
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

    /**
     * 新增物料记录.
     *
     * 将表信息保存：material
     * 添加日志信息：LogType.MATERIAL, Operate.ADD
     *
     * @param materialNo
     * @param materialName
     * @param unit
     * @param categoryId
     * @param spec
     * @param remark
     * @return
     */
    public Material addMaterial(String materialNo, String materialName, String unit,
                                int categoryId, String spec, String remark) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Material material = new Material();
        material.setMaterialNo(materialNo);
        material.setMaterialName(materialName);
        material.setUnit(unit);
        material.setCategoryId(categoryId);
        material.setSpec(spec);
        material.setRemark(remark);
        material.setCreateAt(new Date());
        material.setCreateBy(loginAdmin.getAdminId());
        material.setUpdateAt(new Date());
        material.setUpdateBy(loginAdmin.getAdminId());
        materialDAO.insertSelective(material);

        addLog(LogType.MATERIAL, Operate.ADD, material.getMaterialId());
        return getMaterialWithCategoryById(material.getMaterialId());
    }

    /**
     * 更新物料信息.
     *
     * 更新表：material
     * 添加日志信息：LogType.MATERIAL, Operate.UPDATE
     *
     * @param materialId
     * @param materialNo
     * @param materialName
     * @param unit
     * @param categoryId
     * @param spec
     * @param remark
     * @return
     */
    public Material updateMaterial(Integer materialId, String materialNo, String materialName, String unit,
                                   int categoryId, String spec, String remark) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Material material = new Material();
        material.setMaterialId(materialId);
        material.setMaterialNo(materialNo);
        material.setMaterialName(materialName);
        material.setUnit(unit);
        material.setCategoryId(categoryId);
        material.setSpec(spec);
        material.setRemark(remark);
        material.setUpdateAt(new Date());
        material.setUpdateBy(loginAdmin.getAdminId());
        materialDAO.updateByPrimaryKeySelective(material);

        addLog(LogType.MATERIAL, Operate.UPDATE, material.getMaterialId());
        return getMaterialWithCategoryById(materialId);
    }
}
