package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.SupplierDAO;
import com.springmvc.dao.SupplierMaterialDAO;
import com.springmvc.dto.*;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.pojo.SupplierMaterialQuery;
import com.springmvc.pojo.SupplierQuery;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service("SupplierService")
@Transactional
public class SupplierService extends BaseService {
    @Resource
    private SupplierDAO supplierDAO;

    @Resource
    private SupplierMaterialDAO supplierMaterialDAO;

    @Resource
    private MaterialDAO materialDAO;

    private static final String SUPPLIER_MATERIAL_EXIST = "货品已在供料信息中，不可重复";

    /**
     * 新增供应商信息
     *
     * 检查供应商是否存在
     * 供应商信息保存：Supplier
     * 添加日志信息  LogType.SUPPLIER, Operate.ADD
     *
     * @param supplierName
     * @param contact
     * @param contactPhone
     * @param region
     * @param address
     */
    public Supplier addSupplier( String supplierName, String contact,String contactPhone,String region,String address) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierName);
        supplier.setContact(contact);
        supplier.setContactPhone(contactPhone);
        supplier.setRegion(region);
        supplier.setAddress(address);
        supplier.setCreateAt(new Date());
        supplier.setCreateBy(loginAdmin.getAdminId());
        supplier.setUpdateAt(new Date());
        supplier.setUpdateBy(loginAdmin.getAdminId());
        supplierDAO.insertSelective(supplier);

        // 添加日志
        addLog(LogType.SUPPLIER, Operate.ADD, supplier.getSupplierId());
        return getSupplierById(supplier.getSupplierId());
    }



    /**
     * 查询供应商信息
     *
     * 将主表信息取出：Supplier
     *
     * @param supplierId
     */
    public Supplier getSupplierById(int supplierId) {
        return supplierDAO.selectByPrimaryKey(supplierId);
    }


    /**
     * 查询供应商信息（分页）
     *
     * 将主表信息取出：supplier（同时包含总记录数）
     * 搜索字段：供应商名称、联系人
     *
     * @param current  当前页数
     * @param limit   每页限制
     * @param sortColumn   排序字段
     * @param sort           升降序
     * @param searchKey
     */
    public PageMode<Supplier> pageSupplier(Integer current, Integer limit, String sortColumn,String sort,
                                     String searchKey) {
        SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.setOffset((current-1) * limit);
        supplierQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            supplierQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }

        // 搜索供应商名称
        SupplierQuery.Criteria criteria = supplierQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andSupplierNameLike("%" + searchKey + "%");
        }
        // 搜索联系人
        criteria = supplierQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andContactLike("%" + searchKey + "%");
        }

        List<Supplier> result = supplierDAO.selectByExample(supplierQuery)  ;

        return new PageMode<Supplier>(result, supplierDAO.countByExample(supplierQuery));
    }


    /**
     * 删除供应商信息
     *
     * 删除主表信息：supplier
     * 删除关联的从表信息：supplier_material
     * 添加日志信息：LogType.SUPPLIER, Operate.REMOVE
     *
     * @param idList
     */
    public void removeSupplier(List<Integer> idList) {
        // 删除 supplier
        SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.or().andSupplierIdIn(idList);
        supplierDAO.deleteByExample(supplierQuery);
        // 关联 supplier_material
        SupplierMaterialQuery supplierMaterialQuery = new SupplierMaterialQuery();
        supplierMaterialQuery.or().andSupplierIdIn(idList);
        supplierMaterialDAO.deleteByExample(supplierMaterialQuery);
        // 添加日志
        addLog(LogType.SUPPLIER, Operate.REMOVE, idList);
    }

    /**
     * 更新供应商信息
     *
     * 更新主表信息：supplier
     * 添加日志信息：LogType.SUPPLIER, Operate.UPDATE
     *
     * @param supplierName
     * @param contact
     * @param contactPhone
     * @param region
     * @param address
     */
    public Supplier updateSupplier(Integer supplierId, String supplierName, String contact, String contactPhone, String region, String address) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierId);
        supplier.setSupplierName(supplierName);
        supplier.setContact(contact);
        supplier.setContactPhone(contactPhone);
        supplier.setRegion(region);
        supplier.setAddress(address);
        supplier.setUpdateAt(new Date());
        supplier.setUpdateBy(loginAdmin.getAdminId());
        supplierDAO.updateByPrimaryKeySelective(supplier);

        // 添加日志
        addLog(LogType.SUPPLIER, Operate.UPDATE, supplier.getSupplierId());
        return getSupplierById(supplier.getSupplierId());
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 新增供应商物料信息,需要改成按materialId来查的格式
     *
     * 检查现有供应商物料表中是否存在
     * 供应商信息保存：SupplierMaterial
     * 添加日志信息  LogType.SUPPLIER, Operate.ADD
     *
     * @param supplierId
     * @param materialId
     * @param price
     * @param remark
     */
    public SupplierMaterial addSupplierMaterial(Integer supplierId, Integer materialId, BigDecimal price, String remark ){
        SupplierMaterialQuery supplierMaterialQuery = new SupplierMaterialQuery();
        supplierMaterialQuery.or().andSupplierIdEqualTo(supplierId)
                .andMaterialIdEqualTo(materialId);
        if (supplierMaterialDAO.countByExample(supplierMaterialQuery) > 0) {
            throw new BadRequestException(SUPPLIER_MATERIAL_EXIST);
        }

        SupplierMaterial supplierMaterial = new SupplierMaterial();
        supplierMaterial.setSupplierId(supplierId);
        supplierMaterial.setMaterialId(materialId);
        supplierMaterial.setPrice(price);
        supplierMaterial.setRemark(remark);
        supplierMaterialDAO.insertSelective(supplierMaterial);

        // 添加日志
        addLog(LogType.SUPPLIER_MATERIAL, Operate.ADD, supplierMaterial.getSupplierMaterialId());
        return getSupplierMaterialById(supplierMaterial.getSupplierMaterialId());
    }

    /**
     * 查询供应商物料信息查询 单个
     *
     * 将主表信息取出：supplier_material
     *
     * @param supplierMaterialId
     * @return
     */
    public  SupplierMaterial getSupplierMaterialById(int supplierMaterialId) {
        SupplierMaterial supplierMaterial = supplierMaterialDAO.selectByPrimaryKey(supplierMaterialId);
        Material material = materialDAO.selectByPrimaryKey(supplierMaterial.getMaterialId());
        if (material != null) {
            supplierMaterial.setMaterialName(material.getMaterialName());
            supplierMaterial.setMaterialNo(material.getMaterialNo());
        }
        return supplierMaterial;
    }

    /**
     * 查询供应商物料信息（分页）
     *
     * 将主表信息取出：supplier_material（同时包含总记录数）
     * 搜索字段：物料编号、物料名称
     *
     * @param supplierId  所属supperID
     * @param current  当前页数
     * @param limit   每页限制
     * @param sortColumn   排序字段
     * @param sort           升降序
     * @param searchKey
     * @return
     */
    public PageMode<SupplierMaterial> pageSupplierMaterial(Integer supplierId, Integer current, Integer limit,
                                                           String sortColumn, String sort, String searchKey) {
        SupplierMaterialQuery suppliermaterialQuery = new SupplierMaterialQuery();
        suppliermaterialQuery.setOffset((current-1) * limit);
        suppliermaterialQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            suppliermaterialQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }

        SupplierMaterialQuery.Criteria criteria = suppliermaterialQuery.or();
        criteria.andSupplierIdEqualTo(supplierId);

        // 搜索物料编号和物料名称
        if (!ParamUtils.isNull(searchKey)) {
            MaterialQuery materialQuery = new MaterialQuery();
            materialQuery.or().andMaterialNoLike("%" + searchKey + "%");
            materialQuery.or().andMaterialNameLike("%" + searchKey + "%");
            List<Material> materialList = materialDAO.selectByExample(materialQuery);
            if (materialList.size() == 0) {
                criteria.andMaterialIdEqualTo(0);
            } else {
                Set<Integer> materialIdSet = new HashSet<Integer>();
                for (Material material: materialList) {
                    materialIdSet.add(material.getMaterialId());
                }
                criteria.andMaterialIdIn(new ArrayList<Integer>(materialIdSet));
            }
        }

        List<SupplierMaterial> result = supplierMaterialDAO.selectByExample(suppliermaterialQuery);
        for (int i = 0; i < result.size(); ++i) {
            SupplierMaterial supplierMaterial = getSupplierMaterialById(result.get(i).getSupplierMaterialId());
            result.set(i, supplierMaterial);
        }
        return new PageMode<SupplierMaterial>(result, supplierMaterialDAO.countByExample(suppliermaterialQuery));
    }

    /**
     * 删除供应商物料信息
     *
     * 删除主表信息：supplier_material
     * 添加日志信息：LogType.SUPPLIER_MATERIAL, Operate.REMOVE
     *
     * @param idList
     */
    public void removeSupplierMaterial(List<Integer> idList) {
        // 删除 admin
        SupplierMaterialQuery supplierMaterialQuery = new SupplierMaterialQuery();
        supplierMaterialQuery.or().andSupplierIdIn(idList);
        supplierMaterialDAO.deleteByExample(supplierMaterialQuery);
        // 添加日志
        addLog(LogType.SUPPLIER_MATERIAL, Operate.REMOVE, idList);
    }

    /**
     * 更新供应商物料信息
     *
     * 进行必要的检查：当前物料是存在
     * 更新主表信息：supplier_material
     * 添加日志信息：LogType.SUPPLIER_MATERIAL, Operate.UPDATE
     *
     * @param supplierId
     * @param materialId
     * @param price
     * @param remark
     */
    public SupplierMaterial updateSupplierMaterial(Integer supplierMaterialId, Integer supplierId, Integer materialId,
                                                   BigDecimal price, String remark) {
        SupplierMaterialQuery supplierMaterialQuery = new SupplierMaterialQuery();
        supplierMaterialQuery.or().andSupplierIdEqualTo(supplierId)
                .andMaterialIdEqualTo(materialId)
                .andSupplierMaterialIdNotEqualTo(supplierMaterialId);
        if (supplierMaterialDAO.countByExample(supplierMaterialQuery) > 0) {
            throw new BadRequestException(SUPPLIER_MATERIAL_EXIST);
        }

        SupplierMaterial supplierMaterial = new SupplierMaterial();
        supplierMaterial.setSupplierMaterialId(supplierMaterialId);
        supplierMaterial.setSupplierId(supplierId);
        supplierMaterial.setMaterialId(materialId);
        supplierMaterial.setPrice(price);
        supplierMaterial.setRemark(remark);
        supplierMaterialDAO.updateByPrimaryKeySelective(supplierMaterial);

        // 添加日志
        addLog(LogType.SUPPLIER_MATERIAL, Operate.UPDATE, supplierMaterial.getSupplierMaterialId());
        return getSupplierMaterialById(supplierMaterial.getSupplierMaterialId());
    }

    /**
     * 查询所有物料
     */
    public List<Material> getMaterialList() {
        return materialDAO.selectByExample(new MaterialQuery());
    }
}
