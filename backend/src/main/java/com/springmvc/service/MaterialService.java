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

@Service("MaterialService")
@Transactional
public class MaterialService extends BaseService {
    @Resource
    MaterialDAO materialDAO;

    @Resource
    ProductMaterialDAO productMaterialDAO;

    @Resource
    DrawMaterialBillMaterialDAO drawMaterialBillMaterialDAO;

    @Resource
    ReturnMaterialBillMaterialDAO returnMaterialBillMaterialDAO;

    @Resource
    MaterialInstockBillMaterialDAO materialInstockBillMaterialDAO;

    @Resource
    MaterialOutstockBillMaterialDAO materialOutstockBillMaterialDAO;

    @Resource
    SupplierMaterialDAO supplierMaterialDAO;

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
        // 类别筛选
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
     * 删除主表信息：material
     * 删除关联的从表信息：supplier_material
     * 添加日志信息：LogType.MATERIAL, Operate.REMOVE
     *
     * @param materialIdList
     */
    public void removeMaterial(List<Integer> materialIdList) {
        // 检查引用
        ProductMaterialQuery productMaterialQuery = new ProductMaterialQuery();
        productMaterialQuery.or().andMaterialIdIn(materialIdList);
        if (productMaterialDAO.countByExample(productMaterialQuery) > 0) {
            throw new BadRequestException(MATERIAL_REFER_BY_PRODUCT);
        }

        DrawMaterialBillMaterialQuery drawMaterialBillMaterialQuery = new DrawMaterialBillMaterialQuery();
        drawMaterialBillMaterialQuery.or().andMaterialIdIn(materialIdList);
        if (drawMaterialBillMaterialDAO.countByExample(drawMaterialBillMaterialQuery) > 0) {
            throw new BadRequestException(MATERIAL_REFER_BY_DRAW);
        }

        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery = new ReturnMaterialBillMaterialQuery();
        returnMaterialBillMaterialQuery.or().andMaterialIdIn(materialIdList);
        if (returnMaterialBillMaterialDAO.countByExample(returnMaterialBillMaterialQuery) > 0) {
            throw new BadRequestException(MATERIAL_REFER_BY_RETURN);
        }

        MaterialInstockBillMaterialQuery materialInstockBillMaterialQuery = new MaterialInstockBillMaterialQuery();
        materialInstockBillMaterialQuery.or().andMaterialIdIn(materialIdList);
        if (materialInstockBillMaterialDAO.countByExample(materialInstockBillMaterialQuery) > 0) {
            throw new BadRequestException(MATERIAL_REFER_BY_INSTOCK);
        }

        MaterialOutstockBillMaterialQuery materialOutstockBillMaterialQuery = new MaterialOutstockBillMaterialQuery();
        materialOutstockBillMaterialQuery.or().andMaterialIdIn(materialIdList);
        if (materialOutstockBillMaterialDAO.countByExample(materialOutstockBillMaterialQuery) > 0) {
            throw new BadRequestException(MATERIAL_REFER_BY_OUTSTOCK);
        }

        //删除主表
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.or().andMaterialIdIn(materialIdList);
        materialDAO.deleteByExample(materialQuery);
        //删除从表
        SupplierMaterialQuery supplierMaterialQuery = new SupplierMaterialQuery();
        supplierMaterialQuery.or().andMaterialIdIn(materialIdList);
        supplierMaterialDAO.deleteByExample(supplierMaterialQuery);

        // 添加日志
        addLog(LogType.MATERIAL, Operate.REMOVE, materialIdList);
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
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.or().andMaterialIdEqualTo(materialId);
        List<Material> materialList = materialDAO.selectWithCategoryNameByExample(materialQuery);
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

    /**
     * 更新物料成本信息
     *
     * 更新主表信息：material
     * 更新关联的从表信息：暂无
     * 添加日志信息：LogType.MATERIAL_COST, Operate.UPDATE
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
        addLog(LogType.MATERIAL_COST, Operate.UPDATE, material.getMaterialId());
        return getMaterialById(material.getMaterialId());
    }

    private static final String MATERIAL_REFER_BY_PRODUCT = "物料被货品组成引用";
    private static final String MATERIAL_REFER_BY_DRAW = "物料被领料单引用";
    private static final String MATERIAL_REFER_BY_RETURN = "物料被退料单引用";
    private static final String MATERIAL_REFER_BY_INSTOCK = "物料被物料入库单引用";
    private static final String MATERIAL_REFER_BY_OUTSTOCK = "物料被物料出库单引用";
}