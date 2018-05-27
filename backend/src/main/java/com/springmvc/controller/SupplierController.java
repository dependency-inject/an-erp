package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
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

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    SupplierService supplierService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_ADD)
    public Supplier add(@RequestParam String supplierName, @RequestParam String contact,
                     @RequestParam String contactPhone, @RequestParam String region,
                     @RequestParam String address ) {
        return supplierService.addSupplier(supplierName, contact, contactPhone, region, address);
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public Supplier getById(@RequestParam Integer supplierId) {
        return supplierService.getSupplierById(supplierId);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_REMOVE)
    public String remove(@RequestParam String idList) {
        supplierService.removeSupplier(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<Supplier> search(@RequestParam Integer current, @RequestParam Integer limit,
                                  String sortColumn, String sort, String searchKey) {
        return supplierService.pageSupplier(current, limit, sortColumn, sort, searchKey);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_UPDATE)
    public Supplier update(@RequestParam Integer supplierId,@RequestParam String supplierName, @RequestParam String contact,
                           @RequestParam String contactPhone, @RequestParam String region,
                           @RequestParam String address ) {
        return supplierService.updatesupplier(supplierId,supplierName, contact, contactPhone, region, address);
    }


    @RequestMapping(value = "/addmaterial", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_ADD)
    public SupplierMaterial addMaterial(@RequestParam Integer supplierId, @RequestParam String materialNo,
                                        @RequestParam BigDecimal price, @RequestParam String remark ) {

        return supplierService.addSupplierMaterial(supplierId, materialNo, price, remark);
    }

    @RequestMapping(value = "/getmaterialById", method = RequestMethod.POST)
    @ResponseBody
    public SupplierMaterial getMaterialById(@RequestParam Integer supplierMaterialId) {
        return supplierService.getSupplierMaterialById(supplierMaterialId);
    }

    @RequestMapping(value = "/removematerial", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_REMOVE)
    public String removeMaterial(@RequestParam String idList) {
        supplierService.removeSupplierMaterial(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/searchmaterial", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<SupplierMaterial> searchMaterial(@RequestParam Integer supplierId,@RequestParam Integer current, @RequestParam Integer limit,
                                     String sortColumn, String sort, String searchKey) {
        return supplierService.pageSupplierMaterial(supplierId,current, limit, sortColumn, sort, searchKey);
    }

    @RequestMapping(value = "/updatematerial", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.SUPPLIER_UPDATE)
    public SupplierMaterial updateMaterial(@RequestParam Integer supplierMaterialId,@RequestParam Integer supplierId, @RequestParam String materialNo,
                                           @RequestParam BigDecimal price, @RequestParam String remark ) {
        return supplierService.updatesupplierMaterial(supplierMaterialId,supplierId, materialNo, price, remark);
    }


}
