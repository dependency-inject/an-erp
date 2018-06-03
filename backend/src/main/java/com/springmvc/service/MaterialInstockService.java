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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("MaterialInstockService")
@Transactional
public class MaterialInstockService extends BaseService {

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private WarehouseDAO warehouseDAO;

    @Resource
    private AdminDAO adminDAO;

    @Resource
    private MaterialInstockBillDAO materialInstockBillDAO;

    @Resource
    private MaterialInstockBillMaterialDAO materialInstockBillMaterialDAO;

    /**
     * 删除物料入库单
     *
     * @param billIdList
     */
    public void removeMaterialBill(List<Integer> billIdList) {
        MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
//        materialInstockBillQuery.or().andBillStateNotEqualTo(1);
//        if(materialInstockBillDAO.countByExample(materialInstockBillQuery) > 0)
//        {
//            throw new BadRequestException(MATERIAL_BILL_STATE_WRONG);
//        }
        for(int billID : billIdList) {
            MaterialInstockBill materialInstockBill=materialInstockBillDAO.selectByPrimaryKey(billID);
            if(materialInstockBill.getBillState()!=1)
                throw new BadRequestException(MATERIAL_BILL_STATE_NOT_UPDATE);
        }
        //删除物料入库单
        materialInstockBillQuery=new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andBillIdIn(billIdList);
        materialInstockBillDAO.deleteByExample(materialInstockBillQuery);
        //删除关联
        MaterialInstockBillMaterialQuery materialInstockBillMaterialQuery=new MaterialInstockBillMaterialQuery();
        materialInstockBillMaterialQuery.or().andBillIdIn(billIdList);
        materialInstockBillMaterialDAO.deleteByExample(materialInstockBillMaterialQuery);
        //添加日志
        addLog(LogType.MATERIAL_INSTOCK,Operate.REMOVE,billIdList);
    }

    /**
     * 采购入库。
     * @param bill_no
     * @param from_principal
     * @param warehouse_principal
     * @param related_bill
     * @param remark
     * @return
     */

    public MaterialInstockBill addMaterialInstockBillByPurchase(String bill_no, Integer from_principal, Integer warehouse_principal,
                                              Integer related_bill, String remark, Integer state, String materials, Integer material_source) {
    MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
    Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
    MaterialInstockBill materialInstockBill=new MaterialInstockBill();
    materialInstockBill.setBillNo(bill_no);
    materialInstockBill.setFromPrincipal(from_principal);
    materialInstockBill.setWarehousePrincipal(warehouse_principal);
    materialInstockBill.setBillTime(new Date());
    materialInstockBill.setMaterialSource(material_source);
    materialInstockBill.setRelatedBill(related_bill);
    materialInstockBill.setBillState(state);
    materialInstockBill.setRemark(remark);
    // 完成时间和审核时间为空
    materialInstockBill.setAuditAt(null);
    materialInstockBill.setAuditBy(null);
    materialInstockBill.setFinishAt(null);
    materialInstockBill.setFinishBy(null);
    materialInstockBill.setCreateAt(new java.util.Date());
    materialInstockBill.setCreateBy(loginAdmin.getAdminId());
    materialInstockBill.setUpdateAt(new java.util.Date());
    materialInstockBill.setUpdateBy(loginAdmin.getAdminId());
    materialInstockBillDAO.insertSelective(materialInstockBill);
    List<MaterialInstockBillMaterial> materialInstockBillMaterials = ParamUtils.jsonToList(materials, MaterialInstockBillMaterial.class);
    for(MaterialInstockBillMaterial materialInstockBillMaterial: materialInstockBillMaterials)
    {
        materialInstockBillMaterial.setBillId(materialInstockBill.getBillId());
        materialInstockBillMaterial.setMaterialId(materialInstockBillMaterial.getMaterialId());
        materialInstockBillMaterial.setPrincipal(materialInstockBillMaterial.getPrincipal());
        materialInstockBillMaterial.setPlace(materialInstockBillMaterial.getPlace());
        materialInstockBillMaterial.setRemark(materialInstockBillMaterial.getRemark());
        materialInstockBillMaterialDAO.insertSelective(materialInstockBillMaterial);
    }
    // 日志记录
    addLog(LogType.MATERIAL_INSTOCK,Operate.ADD,materialInstockBill.getBillId());
    return getMaterialInstockBillById(materialInstockBill.getBillId());
}

    /**
    * 更新物料入库单
    *
    * @param bill_no
    * @param from_principal
    * @param warehouse_principal
    * @param related_bill
    * @param bill_state
    * @param remark
    * @return
    */

    public MaterialInstockBill updateMateialInstockBill(Integer bill_id, String bill_no, Integer from_principal, Integer warehouse_principal,
                                                        Integer related_bill, Integer bill_state, String remark, Integer selected_source) {

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        MaterialInstockBillQuery.Criteria criteria = materialInstockBillQuery.or();
        criteria.andBillIdEqualTo(bill_id);
        List<MaterialInstockBill> results = materialInstockBillDAO.selectByExample(materialInstockBillQuery);
        for(MaterialInstockBill item: results)
        {
            int state = item.getBillState();
            int source = item.getMaterialSource();
            if (state == 3) {
                throw new BadRequestException(MATERIAL_BILL_STATE_NOT_UPDATE);
            }
            if (state == 2) {
                throw new BadRequestException(MATERIAL_BILL_STATE_NOT_UNAUDIT);
            }
            if (source == 1) {
                throw new BadRequestException(MATERIAL_SOURCE_MODIFY_ERROR);
            }
        }
        MaterialInstockBill materialInstockBill=new MaterialInstockBill();
        materialInstockBill.setBillId(bill_id);
        materialInstockBill.setBillNo(bill_no);
        materialInstockBill.setFromPrincipal(from_principal);
        materialInstockBill.setWarehousePrincipal(warehouse_principal);
        materialInstockBill.setRelatedBill(related_bill);
        materialInstockBill.setBillState(bill_state);
        materialInstockBill.setRemark(remark);
        materialInstockBill.setMaterialSource(selected_source);
        // 完成时间和审核时间为空
        if(bill_state==1) {
            materialInstockBill.setAuditAt(null);
            materialInstockBill.setAuditBy(null);
            materialInstockBill.setFinishAt(null);
            materialInstockBill.setFinishBy(null);
        }
        else if(bill_state==2) {
            materialInstockBill.setAuditAt(new java.util.Date());
            materialInstockBill.setAuditBy(loginAdmin.getAdminId());
            materialInstockBill.setFinishAt(null);
            materialInstockBill.setFinishBy(null);
        }
        else {
            materialInstockBill.setFinishAt(new java.util.Date());
            materialInstockBill.setFinishBy(loginAdmin.getAdminId());
        }
        materialInstockBill.setUpdateAt(new java.util.Date());
        materialInstockBill.setUpdateBy(loginAdmin.getAdminId());
        materialInstockBillDAO.updateByPrimaryKeySelective(materialInstockBill);
        // 仓库位置
        MaterialInstockBillMaterial materialInstockBillMaterial=new MaterialInstockBillMaterial();
        // 日志记录
        addLog(LogType.MATERIAL_INSTOCK,Operate.UPDATE,materialInstockBill.getBillId());
        return getMaterialInstockBillById(materialInstockBill.getBillId());
    }

    /**
    * 按照ID搜索物料入库单。
    * @param billId
    * @return
    */
    public MaterialInstockBill getMaterialInstockBillById(int billId) {
        MaterialInstockBill materialInstockBill=materialInstockBillDAO.selectByPrimaryKey(billId);
        return materialInstockBill;
    }

    /**
    * 搜索
    * @param current
    * @param limit
    * @param sortColumn
    * @param sort
    * @param searchKey
    * @param materialInstockState
    * @return
    */
    public PageMode<MaterialInstockBill> pageMaterialInstockBill(Integer current, Integer limit, String sortColumn, String sort,
                                                             String searchKey, Integer materialInstockState) {
        MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
        materialInstockBillQuery.setOffset((current-1)*limit);
        materialInstockBillQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            materialInstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }
        //查询订单编号
        MaterialInstockBillQuery.Criteria criteria=materialInstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andBillNoLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(materialInstockState) && !materialInstockState.equals(-1)) {
            criteria.andBillStateEqualTo(materialInstockState);
        }
        //查询备注（remark）
        criteria=materialInstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andRemarkLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(materialInstockState) && !materialInstockState.equals(-1)) {
            criteria.andBillStateEqualTo(materialInstockState);
        }
        //返回warehouse
        List<MaterialInstockBill> result=materialInstockBillDAO.selectByExample(materialInstockBillQuery);
        return new PageMode<MaterialInstockBill>(result,materialInstockBillDAO.countByExample(materialInstockBillQuery));
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
    public List<MaterialInstockBillMaterial> getMaterial(Integer billId) {
        MaterialInstockBillMaterialQuery materialInstockBillMaterialQuery = new MaterialInstockBillMaterialQuery();
        MaterialInstockBillMaterialQuery.Criteria criteria = materialInstockBillMaterialQuery.or();
        criteria.andBillIdEqualTo(billId);
        List<MaterialInstockBillMaterial> results = materialInstockBillMaterialDAO.selectByExample(materialInstockBillMaterialQuery);
        for(MaterialInstockBillMaterial item: results) {
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
        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andBillIdIn(idList)
                .andBillStateNotEqualTo(state);
        if (materialInstockBillDAO.countByExample(materialInstockBillQuery) > 0) {
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
        MaterialInstockBill materialInstockBill = new MaterialInstockBill();
        materialInstockBill.setBillState(2);
        materialInstockBill.setAuditBy(loginAdmin.getAdminId());
        materialInstockBill.setAuditAt(new Date());

        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andBillIdIn(idList);
        System.out.println("materialOutstockBillQuery");

        materialInstockBillDAO.updateByExampleSelective(materialInstockBill, materialInstockBillQuery);
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

        MaterialInstockBill materialInstockBill = new MaterialInstockBill();
        materialInstockBill.setBillState(1);
        materialInstockBill.setAuditBy(loginAdmin.getAdminId());
        materialInstockBill.setAuditAt(new Date());

        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andBillIdIn(idList);
        materialInstockBillDAO.updateByExampleSelective(materialInstockBill, materialInstockBillQuery);
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

        MaterialInstockBill materialInstockBill = new MaterialInstockBill();
        materialInstockBill.setBillState(3);
        materialInstockBill.setFinishBy(loginAdmin.getAdminId());
        materialInstockBill.setFinishAt(new Date());

        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andBillIdIn(idList);
        materialInstockBillDAO.updateByExampleSelective(materialInstockBill, materialInstockBillQuery);
        // 添加日志
        addLog(LogType.ORDER, Operate.FINISH, idList);
    }

    private static final String MATERIAL_BILL_NOT_EXISTED="物料入库单不存在";
    private static final String MATERIAL_BILL_STATE_WRONG="物料入库单状态不符合要求";
    private static final String MATERIAL_BILL_STATE_NOT_UPDATE="已完成物料入库单不允许修改";
    private static final String MATERIAL_SOURCE_MODIFY_ERROR="退料入库的入库单无法修改";
    private static final String MATERIAL_BILL_STATE_NOT_AUDIT="存在未审核物料入库单";
    private static final String MATERIAL_BILL_STATE_NOT_UNAUDIT="已审核物料出库单不允许修改";
    private static final String MATERIAL_BILL_STATE_NOT_FINISH="已完成物料出库单不允许修改";
}
