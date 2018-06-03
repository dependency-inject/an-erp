package com.springmvc.service;

import com.springmvc.dao.*;
import com.springmvc.dao.MaterialOutstockBillMaterialDAO;
import com.springmvc.dto.*;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.*;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


@Service("MaterialOutstockService")
@Transactional

public class MaterialOutstockService extends BaseService {
    @Resource
    private MaterialDAO materialDAO;
    @Resource
    private WarehouseDAO warehouseDAO;
    @Resource
    private AdminDAO adminDAO;
    @Resource
    private MaterialOutstockBillDAO materialOutstockBillDAO;
    @Resource
    private MaterialOutstockBillMaterialDAO materialOutstockBillMaterialDAO;

    /**
     * 删除物料出库单
     * @param billIdList
     */
    public void removeMaterialOutstockBill(List<Integer> billIdList) {
        MaterialOutstockBillQuery materialOutstockBillQuery=new MaterialOutstockBillQuery();
        //检查物料出库单状态是否为待审核。
        materialOutstockBillQuery=new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillStateNotEqualTo(1);
        if(materialOutstockBillDAO.countByExample(materialOutstockBillQuery) > 0)
        {
            throw new BadRequestException(MATERIAL_BILL_STATE_WRONG);
        }
        //删除物料出库单
        materialOutstockBillQuery=new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillIdIn(billIdList);
        materialOutstockBillDAO.deleteByExample(materialOutstockBillQuery);
        //删除关联
        MaterialOutstockBillMaterialQuery materialOutstockBillMaterialQuery=new MaterialOutstockBillMaterialQuery();
        materialOutstockBillMaterialQuery.or().andBillIdIn(billIdList);
        materialOutstockBillMaterialDAO.deleteByExample(materialOutstockBillMaterialQuery);
        //添加日志
        addLog(LogType.MATERIAL_OUTSTOCK,Operate.REMOVE,billIdList);
    }

    /**
     * 添加物料出库单
     * @param bill_no
     * @param to_principal
     * @param warehouse_principal
     * @param related_bill
     * @param remark
     * @param state
     * @param materials
     * @return
     */
    public MaterialOutstockBill addMaterialOutstockBill(String bill_no, Integer to_principal, Integer warehouse_principal,
                                                        Integer related_bill, String remark, Integer state, String materials, Integer material_source) {
        MaterialOutstockBillQuery materialOutstockBillQuery=new MaterialOutstockBillQuery();
        Admin loginAdmin= RequestUtils.getLoginAdminFromCache();
        MaterialOutstockBill materialOutstockBill=new MaterialOutstockBill();
        materialOutstockBill.setBillNo(bill_no);
        materialOutstockBill.setToPrincipal(to_principal);
        materialOutstockBill.setWarehousePrincipal(warehouse_principal);
        materialOutstockBill.setBillTime(new Date());
        materialOutstockBill.setMaterialWhereabouts(material_source);
        materialOutstockBill.setRelatedBill(related_bill);
        materialOutstockBill.setBillState(state);
        materialOutstockBill.setRemark(remark);
        materialOutstockBill.setAuditAt(null);
        materialOutstockBill.setAuditBy(null);
        materialOutstockBill.setFinishAt(null);
        materialOutstockBill.setFinishBy(null);
        materialOutstockBill.setCreateAt(new Date());
        materialOutstockBill.setCreateBy(loginAdmin.getAdminId());
        materialOutstockBill.setUpdateAt(new Date());
        materialOutstockBill.setUpdateBy(loginAdmin.getAdminId());
        materialOutstockBillDAO.insertSelective(materialOutstockBill);
        List<MaterialOutstockBillMaterial> materialOutstockBillMaterials = ParamUtils.jsonToList(materials, MaterialOutstockBillMaterial.class);
        for(MaterialOutstockBillMaterial materialOutstockBillMaterial: materialOutstockBillMaterials) {
            materialOutstockBillMaterial.setBillId(materialOutstockBill.getBillId());
            materialOutstockBillMaterial.setMaterialId(materialOutstockBillMaterial.getMaterialId());
            materialOutstockBillMaterial.setPrincipal(materialOutstockBillMaterial.getPrincipal());
            materialOutstockBillMaterial.setPlace(materialOutstockBillMaterial.getPlace());
            materialOutstockBillMaterial.setRemark(materialOutstockBillMaterial.getRemark());
            materialOutstockBillMaterialDAO.insert(materialOutstockBillMaterial);
        }
        addLog(LogType.MATERIAL_OUTSTOCK,Operate.ADD,materialOutstockBill.getBillId());
        return getMaterialOutstockBillById(materialOutstockBill.getBillId());
    }

    /**
     * 更新物料出库单
     * @param bill_no
     * @param to_principal
     * @param warehouse_principal
     * @param related_bill
     * @param bill_state
     * @param remark
     * @return
     */
    public MaterialOutstockBill updateMaterialOutstockBill(Integer bill_id, String bill_no, Integer to_principal, Integer warehouse_principal,
                                                           Integer related_bill,Integer bill_state, String remark, Integer selected_source) {
         Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
         MaterialOutstockBillQuery materialOutstockBillQuery = new MaterialOutstockBillQuery();
         MaterialOutstockBillQuery.Criteria criteria = materialOutstockBillQuery.or();
         criteria.andBillIdEqualTo(bill_id);
         List<MaterialOutstockBill> results = materialOutstockBillDAO.selectByExample(materialOutstockBillQuery);
         for(MaterialOutstockBill item: results)
         {
             int state = item.getBillState();
             // int source = item.getMaterialWhereabouts();
             if (state == 3) {
                 throw new BadRequestException(MATERIAL_BILL_STATE_NOT_UPDATE);
             }
             if (state == 2) {
                 throw new BadRequestException(MATERIAL_BILL_STATE_NOT_UNAUDIT);
             }
         }
         materialOutstockBillQuery=new MaterialOutstockBillQuery();
         MaterialOutstockBill materialOutstockBill=new MaterialOutstockBill();
         materialOutstockBill.setBillId(bill_id);
         materialOutstockBill.setBillNo(bill_no);
         materialOutstockBill.setToPrincipal(to_principal);
         materialOutstockBill.setWarehousePrincipal(warehouse_principal);
         materialOutstockBill.setRelatedBill(related_bill);
         materialOutstockBill.setRemark(remark);
         materialOutstockBill.setMaterialWhereabouts(selected_source);
         //  完成时间和审核时间为空
         if(bill_state==1) {
             materialOutstockBill.setAuditAt(null);
             materialOutstockBill.setAuditBy(null);
             materialOutstockBill.setFinishAt(null);
             materialOutstockBill.setFinishBy(null);
         }
         else if(bill_state==2) {
             materialOutstockBill.setAuditAt(new java.util.Date());
             materialOutstockBill.setAuditBy(loginAdmin.getAdminId());
             materialOutstockBill.setFinishAt(null);
             materialOutstockBill.setFinishBy(null);
         }
         else {
             materialOutstockBill.setFinishAt(new java.util.Date());
             materialOutstockBill.setFinishBy(loginAdmin.getAdminId());
         }
         materialOutstockBill.setUpdateAt(new java.util.Date());
         materialOutstockBill.setUpdateBy(loginAdmin.getAdminId());
         materialOutstockBillDAO.updateByPrimaryKeySelective(materialOutstockBill);
         MaterialOutstockBillMaterial materialOutstockBillMaterial;
         //    日志记录
         addLog(LogType.MATERIAL_OUTSTOCK,Operate.UPDATE,materialOutstockBill.getBillId());
         return getMaterialOutstockBillById(materialOutstockBill.getBillId());
    }

    /**
     * 按照id获取物料出库单
     * @param billId
     * @return
     */
    public MaterialOutstockBill getMaterialOutstockBillById(int billId) {
        MaterialOutstockBill materialOutstockBill=materialOutstockBillDAO.selectByPrimaryKey(billId);
        return  materialOutstockBill;
    }

    /**
     * 搜索
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @param materialOutstockState
     * @return
     */
    public PageMode<MaterialOutstockBill> pageMaterialOutstockBill(Integer current, Integer limit, String sortColumn,
                                                                   String sort, String searchKey, Integer materialOutstockState) {
        MaterialOutstockBillQuery materialOutstockBillQuery=new MaterialOutstockBillQuery();
        materialOutstockBillQuery.setOffset((current-1)*limit);
        materialOutstockBillQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            materialOutstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }
        //查询订单编号
        MaterialOutstockBillQuery.Criteria criteria=materialOutstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andBillNoLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(materialOutstockState) && !materialOutstockState.equals(-1)) {
            criteria.andBillStateEqualTo(materialOutstockState);
        }
        //查询备注（remark）
        criteria=materialOutstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andRemarkLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(materialOutstockState) && !materialOutstockState.equals(-1)) {
            criteria.andBillStateEqualTo(materialOutstockState);
        }
        List<MaterialOutstockBill> result=materialOutstockBillDAO.selectByExample(materialOutstockBillQuery);
        return new PageMode<MaterialOutstockBill>(result,materialOutstockBillDAO.countByExample(materialOutstockBillQuery));
    }

    /**
     * 获取可选物料列表
     * @param
     * @return
     */
    public List<Material> getMaterials() {
        MaterialQuery materialQuery = new MaterialQuery();
        List<Material> result = materialDAO.selectByExample(materialQuery);
        return result;
    }

    /**
     * 获取可选负责人
     * @param
     * @return
     */
    public List<Admin> getAdmins() {
        AdminQuery adminQuery = new AdminQuery();
        List<Admin> result = adminDAO.selectByExample(adminQuery);
        return result;
    }

    /**
     * 获取可选仓库列表
     * @param
     * @return
     */
    public List<Warehouse> getWarehouses() {
        WarehouseQuery warehouseQuery = new WarehouseQuery();
        List<Warehouse> result = warehouseDAO.selectByExample(warehouseQuery);
        return result;
    }

    /**
     * 根据物料出库单id返回材料列表
     * @param billId
     * @return
     */
    public List<MaterialOutstockBillMaterial> getMaterial(Integer billId) {
        MaterialOutstockBillMaterialQuery materialOutstockBillMaterialQuery = new MaterialOutstockBillMaterialQuery();
        MaterialOutstockBillMaterialQuery.Criteria criteria = materialOutstockBillMaterialQuery.or();
        criteria.andBillIdEqualTo(billId);
        List<MaterialOutstockBillMaterial> results = materialOutstockBillMaterialDAO.selectByExample(materialOutstockBillMaterialQuery);
        for(MaterialOutstockBillMaterial item: results)
        {
            String materialName = materialDAO.selectByPrimaryKey(item.getMaterialId()).getMaterialName();
            String warehouseName = warehouseDAO.selectByPrimaryKey(item.getWarehouse()).getWarehouseName();
            String principalName = adminDAO.selectByPrimaryKey(item.getPrincipal()).getTrueName();
            item.setMaterialName(materialName);
            item.setWarehouseName(warehouseName);
            item.setPrincipalName(principalName);
        }
        return results;
    }

    private void checkBillState(List<Integer> idList, int state) {
        MaterialOutstockBillQuery materialOutstockBillQuery = new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillIdIn(idList)
                .andBillStateNotEqualTo(state);
        if (materialOutstockBillDAO.countByExample(materialOutstockBillQuery) > 0) {
            if (state == 1) {
                throw new BadRequestException(MATERIAL_BILL_STATE_NOT_AUDIT);
            }
            if (state == 2) {
                throw new BadRequestException(MATERIAL_BILL_STATE_NOT_UNAUDIT);
            }
            if (state == 3) {
                throw new BadRequestException(MATERIAL_BILL_STATE_NOT_FINISH);
            }
        }
    }


    /**
     * 审核订单
     *
     * @param idList 订单编号
     */
    public void audit(List<Integer> idList) {
        checkBillState(idList, 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        MaterialOutstockBill materialOutstockBill = new MaterialOutstockBill();
        materialOutstockBill.setBillState(2);
        materialOutstockBill.setAuditBy(loginAdmin.getAdminId());
        materialOutstockBill.setAuditAt(new Date());

        MaterialOutstockBillQuery materialOutstockBillQuery = new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillIdIn(idList);
        System.out.println("materialOutstockBillQuery");

        materialOutstockBillDAO.updateByExampleSelective(materialOutstockBill, materialOutstockBillQuery);
        // 添加日志
        addLog(LogType.ORDER, Operate.AUDIT, idList);
    }

    /**
     * 反审核订单
     *
     * @param idList 订单编号
     */
    public void unaudit(List<Integer> idList) {
        checkBillState(idList, 2);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        MaterialOutstockBill materialOutstockBill = new MaterialOutstockBill();
        materialOutstockBill.setBillState(1);
        materialOutstockBill.setAuditBy(loginAdmin.getAdminId());
        materialOutstockBill.setAuditAt(new Date());

        MaterialOutstockBillQuery materialOutstockBillQuery = new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillIdIn(idList);
        materialOutstockBillDAO.updateByExampleSelective(materialOutstockBill, materialOutstockBillQuery);
        // 添加日志
        addLog(LogType.ORDER, Operate.UNAUDIT, idList);
    }

    /**
     * 完成订单
     *
     * @param idList 订单编号
     */
    public void finish(List<Integer> idList) {
        checkBillState(idList, 2);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        MaterialOutstockBill materialOutstockBill = new MaterialOutstockBill();
        materialOutstockBill.setBillState(3);
        materialOutstockBill.setFinishBy(loginAdmin.getAdminId());
        materialOutstockBill.setFinishAt(new Date());

        MaterialOutstockBillQuery materialOutstockBillQuery = new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillIdIn(idList);
        materialOutstockBillDAO.updateByExampleSelective(materialOutstockBill, materialOutstockBillQuery);
        // 添加日志
        addLog(LogType.ORDER, Operate.FINISH, idList);
    }
    private static final String MATERIAL_BILL_STATE_WRONG="物料出库单状态不符合要求";
    private static final String MATERIAL_WHEREABOUS_MODIFY_ERROR="领料单生成的出库单不可修改";
    private static final String MATERIAL_BILL_STATE_NOT_UPDATE="已完成物料出库单不允许修改";
    private static final String MATERIAL_BILL_STATE_NOT_AUDIT="存在未审核物料出库单";
    private static final String MATERIAL_BILL_STATE_NOT_UNAUDIT="已审核物料出库单不允许修改";
    private static final String MATERIAL_BILL_STATE_NOT_FINISH="已完成物料出库单不允许修改";
}
