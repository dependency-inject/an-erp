package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.Material;
import com.springmvc.dto.Supplier;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.SupplierMaterial;
import com.springmvc.service.SupplierService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    SupplierService supplierService;

    /**
     * 增加供货商
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_ADD)
    public Supplier add(@RequestParam String supplierName, @RequestParam String contact,
                     @RequestParam String contactPhone, @RequestParam String region,
                     @RequestParam String address ) {
        return supplierService.addSupplier(supplierName, contact, contactPhone, region, address);
    }

    /**
     * 获取单个供货商
     */
    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public Supplier getById(@RequestParam Integer supplierId) {
        return supplierService.getSupplierById(supplierId);
    }

    /**
     * 批量删除供货商
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_REMOVE)
    public String remove(@RequestParam String idList) {
        supplierService.removeSupplier(ParamUtils.toIntList(idList));
        return "success";
    }

    /**
     * 查找供货商（包括分页和按字段）
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<Supplier> search(@RequestParam Integer current, @RequestParam Integer limit,
                                  String sortColumn, String sort, String searchKey) {
        return supplierService.pageSupplier(current, limit, sortColumn, sort, searchKey);
    }

    /**
     * 更新供货商
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_UPDATE)
    public Supplier update(@RequestParam Integer supplierId,@RequestParam String supplierName, @RequestParam String contact,
                           @RequestParam String contactPhone, @RequestParam String region,
                           @RequestParam String address ) {
        return supplierService.updateSupplier(supplierId,supplierName, contact, contactPhone, region, address);
    }

    /**
     * 添加供货商物料
     */
    @RequestMapping(value = "/addMaterial", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_UPDATE)
    public SupplierMaterial addMaterial(@RequestParam Integer supplierId, @RequestParam Integer materialId,
                                        @RequestParam BigDecimal price, @RequestParam String remark ) {
        return supplierService.addSupplierMaterial(supplierId, materialId, price, remark);
    }

    /**
     * 查找供货商物料（单个）
     */
    @RequestMapping(value = "/getMaterialById", method = RequestMethod.POST)
    @ResponseBody
    public SupplierMaterial getMaterialById(@RequestParam Integer supplierMaterialId) {
        return supplierService.getSupplierMaterialById(supplierMaterialId);
    }

    /**
     * 删除供货商物料
     */
    @RequestMapping(value = "/removeMaterial", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_UPDATE)
    public String removeMaterial(@RequestParam String idList) {
        supplierService.removeSupplierMaterial(ParamUtils.toIntList(idList));
        return "success";
    }

    /**
     *查找供货商物料（分页和排序）
     */
    @RequestMapping(value = "/searchMaterial", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<SupplierMaterial> searchMaterial(@RequestParam Integer supplierId,@RequestParam Integer current, @RequestParam Integer limit,
                                     String sortColumn, String sort, String searchKey) {
        return supplierService.pageSupplierMaterial(supplierId,current, limit, sortColumn, sort, searchKey);
    }

    /**
     * 更新供货商物料
     */
    @RequestMapping(value = "/updateMaterial", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_UPDATE)
    public SupplierMaterial updateMaterial(@RequestParam Integer supplierMaterialId, @RequestParam Integer supplierId, @RequestParam Integer materialId,
                                           @RequestParam BigDecimal price, @RequestParam String remark ) {
        return supplierService.updateSupplierMaterial(supplierMaterialId,supplierId, materialId, price, remark);
    }

    /**
     * 获取物料列表
     */
    @RequestMapping(value = "/getMaterialList", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_UPDATE)
    public List<Material> getMaterialList() {
        return supplierService.getMaterialList();
    }
}
