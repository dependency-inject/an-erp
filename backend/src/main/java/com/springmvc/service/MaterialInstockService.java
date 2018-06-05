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
import java.util.Collections;
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

    @Resource
    private ReturnMaterialBillDAO returnMaterialBillDAO;

    /**
     * 添加物料入库单
     *
     * 将主表信息保存：material_instock_bill
     * 将关联的从表信息保存：material_instock_bill_product
     * 添加日志信息：LogType.MATERIAL_INSTOCK, Operate.ADD
     * 若为退料入库，为相应退料单设置仓库负责人信息
     *
     * @param fromPrincipal
     * @param materialSource
     * @param relatedBill
     * @param remark
     * @param materialList
     * @return
     */
    public MaterialInstockBill addMaterialInstockBill(Integer fromPrincipal, Integer materialSource, Integer relatedBill,
                                                      String remark, List<MaterialInstockBillMaterial> materialList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        MaterialInstockBill materialInstockBill = new MaterialInstockBill();
        materialInstockBill.setBillNo("MI" + ParamUtils.dateConvert(new Date(), "yyMMddHHmmssSSS"));
        materialInstockBill.setFromPrincipal(fromPrincipal);
        materialInstockBill.setWarehousePrincipal(loginAdmin.getAdminId());
        materialInstockBill.setBillTime(new Date());
        materialInstockBill.setMaterialSource(materialSource);
        materialInstockBill.setRelatedBill(relatedBill);
        materialInstockBill.setBillState(1);
        materialInstockBill.setRemark(remark);
        materialInstockBill.setCreateAt(new Date());
        materialInstockBill.setCreateBy(loginAdmin.getAdminId());
        materialInstockBill.setUpdateAt(new Date());
        materialInstockBill.setUpdateBy(loginAdmin.getAdminId());
        materialInstockBillDAO.insertSelective(materialInstockBill);

        for (MaterialInstockBillMaterial material: materialList) {
            material.setBillId(materialInstockBill.getBillId());
            materialInstockBillMaterialDAO.insert(material);
        }

        addLog(LogType.MATERIAL_INSTOCK, Operate.ADD, materialInstockBill.getBillId());

        // 若为退料入库，为相应退料单设置仓库负责人信息
        if (materialInstockBill.getMaterialSource().equals(1)) {
            ReturnMaterialBill returnMaterialBill = returnMaterialBillDAO.selectByPrimaryKey(materialInstockBill.getRelatedBill());
            if (returnMaterialBill == null) {
                throw new BadRequestException(RETURN_MATERIAL_BILL_NOT_EXIST);
            }
            if (!returnMaterialBill.getBillState().equals(2)) {
                throw new BadRequestException(RETURN_MATERIAL_BILL_NOT_AUDIT);
            }
            returnMaterialBill = new ReturnMaterialBill();
            returnMaterialBill.setBillId(materialInstockBill.getRelatedBill());
            returnMaterialBill.setWarehousePrincipal(materialInstockBill.getWarehousePrincipal());
            returnMaterialBillDAO.updateByPrimaryKeySelective(returnMaterialBill);
        }

        return getMaterialInstockBillById(materialInstockBill.getBillId());
    }

    /**
     * 更新物料入库单
     *
     * 进行必要的检查：是否为待审核状态
     * 更新主表信息：material_instock_bill
     * 更新关联的从表信息：material_instock_bill_material
     * 添加日志信息：LogType.MATERIAL_INSTOCK, Operate.UPDATE
     *
     * @param billId
     * @param fromPrincipal
     * @param materialSource
     * @param remark
     * @param materialList
     * @return
     */
    public MaterialInstockBill updateMateialInstockBill(Integer billId, Integer fromPrincipal, Integer materialSource, Integer relatedBill,
                                                        String remark, List<MaterialInstockBillMaterial> materialList) {

        checkBillState(Collections.singletonList(billId), 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        MaterialInstockBill materialInstockBill = new MaterialInstockBill();
        materialInstockBill.setBillId(billId);
        materialInstockBill.setFromPrincipal(fromPrincipal);
        materialInstockBill.setMaterialSource(materialSource);
        materialInstockBill.setRelatedBill(relatedBill);
        materialInstockBill.setRemark(remark);
        materialInstockBill.setUpdateAt(new Date());
        materialInstockBill.setUpdateBy(loginAdmin.getAdminId());
        materialInstockBillDAO.updateByPrimaryKeySelective(materialInstockBill);

        MaterialInstockBillMaterialQuery materialInstockBillMaterialQuery = new MaterialInstockBillMaterialQuery();
        materialInstockBillMaterialQuery.or().andBillIdEqualTo(materialInstockBill.getBillId());
        materialInstockBillMaterialDAO.deleteByExample(materialInstockBillMaterialQuery);
        for (MaterialInstockBillMaterial material : materialList){
            material.setBillId(materialInstockBill.getBillId());
            materialInstockBillMaterialDAO.insert(material);
        }
        addLog(LogType.MATERIAL_INSTOCK, Operate.UPDATE, materialInstockBill.getBillId());
        return getMaterialInstockBillById(materialInstockBill.getBillId());
    }

    /**
     * 查询物料入库单信息（单个）
     *
     * @param billId 单据编号
     * @return
     */
    public MaterialInstockBill getMaterialInstockBillById(int billId) {
        MaterialInstockBill materialInstockBill = materialInstockBillDAO.selectByPrimaryKey(billId);

        String fromPrincipalName = adminDAO.selectByPrimaryKey(materialInstockBill.getFromPrincipal()).getTrueName();
        materialInstockBill.setFromPrincipalName(fromPrincipalName);
        String warehousePrincipalName = adminDAO.selectByPrimaryKey(materialInstockBill.getWarehousePrincipal()).getTrueName();
        materialInstockBill.setWarehousePrincipalName(warehousePrincipalName);

        Integer i = materialInstockBill.getBillState();
        if (i > 1) {
            String auditName = adminDAO.selectByPrimaryKey(materialInstockBill.getAuditBy()).getTrueName();
            materialInstockBill.setAuditName(auditName);
        }
        if (i > 2) {
            String finishName = adminDAO.selectByPrimaryKey(materialInstockBill.getFinishBy()).getTrueName();
            materialInstockBill.setFinishName(finishName);
        }

        if (materialInstockBill.getMaterialSource().equals(1)) {
            String relatedBillNo = returnMaterialBillDAO.selectByPrimaryKey(materialInstockBill.getRelatedBill()).getBillNo();
            materialInstockBill.setRelatedBillNo(relatedBillNo);
        }

        MaterialInstockBillMaterialQuery materialOutstockBillMaterialQuery = new MaterialInstockBillMaterialQuery();
        MaterialInstockBillMaterialQuery.Criteria criteria = materialOutstockBillMaterialQuery.or();
        criteria.andBillIdEqualTo(materialInstockBill.getBillId());
        List<MaterialInstockBillMaterial> result = materialInstockBillMaterialDAO.selectByExample(materialOutstockBillMaterialQuery);
        for(MaterialInstockBillMaterial item: result) {
            Material material = materialDAO.selectByPrimaryKey(item.getMaterialId());
            if (material != null) {
                item.setMaterialNo(material.getMaterialNo());
                item.setMaterialName(material.getMaterialName());
            }
            Admin principal = adminDAO.selectByPrimaryKey(item.getPrincipal());
            if (principal != null) {
                item.setPrincipalName(principal.getTrueName());
            }
            Warehouse warehouse = warehouseDAO.selectByPrimaryKey(item.getWarehouse());
            if (warehouse != null) {
                item.setWarehouseName(warehouse.getWarehouseName());
            }
        }
        materialInstockBill.setMaterialList(result);
        return materialInstockBill;
    }

    /**
     * 查询物料入库单信息（分页）
     *
     * 将主表信息取出：（同时包含总记录数）
     * 搜索字段：编号、交料人、仓库负责人
     * 筛选字段：状态
     *
     * @param current 当前位置
     * @param limit 一次读取长度
     * @param sortColumn 按哪一列排序
     * @param sort  排序方式 升序 降序
     * @param searchKey 关键字查找
     * @param state 单据状态
     * @return
     */
    public PageMode<MaterialInstockBill> pageMaterialInstockBill(Integer current, Integer limit, String sortColumn, String sort,
                                                             String searchKey, Integer state, Date beginTime, Date endTime) {
        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.setOffset((current-1)*limit);
        materialInstockBillQuery.setLimit(limit);
        // 若未指定则默认按照时间排序
        if(ParamUtils.isNull(sortColumn)) {
            sortColumn = "billTime";
            sort = "desc";
        }
        materialInstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        // 查询编号
        MaterialInstockBillQuery.Criteria criteria = materialInstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andBillNoLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(state) && !state.equals(-1)) {
            criteria.andBillStateEqualTo(state);
        }
        if (!ParamUtils.isNull(beginTime)) {
            criteria.andBillTimeGreaterThanOrEqualTo(beginTime);
        }
        if (!ParamUtils.isNull(endTime)) {
            criteria.andBillTimeLessThanOrEqualTo(endTime);
        }
        // 查询领料人
        List<Integer> adminIdList = searchAdminByTrueName(searchKey);
        criteria = materialInstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            if (adminIdList.size() == 0) {
                criteria.andFromPrincipalEqualTo(0);
            } else {
                criteria.andFromPrincipalIn(adminIdList);
            }
        }
        if (!ParamUtils.isNull(state) && !state.equals(-1)) {
            criteria.andBillStateEqualTo(state);
        }
        if (!ParamUtils.isNull(beginTime)) {
            criteria.andBillTimeGreaterThanOrEqualTo(beginTime);
        }
        if (!ParamUtils.isNull(endTime)) {
            criteria.andBillTimeLessThanOrEqualTo(endTime);
        }
        // 查询仓库负责人
        criteria = materialInstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            if (adminIdList.size() == 0) {
                criteria.andWarehousePrincipalEqualTo(0);
            } else {
                criteria.andWarehousePrincipalIn(adminIdList);
            }
        }
        if (!ParamUtils.isNull(state) && !state.equals(-1)) {
            criteria.andBillStateEqualTo(state);
        }
        if (!ParamUtils.isNull(beginTime)) {
            criteria.andBillTimeGreaterThanOrEqualTo(beginTime);
        }
        if (!ParamUtils.isNull(endTime)) {
            criteria.andBillTimeLessThanOrEqualTo(endTime);
        }

        List<MaterialInstockBill> result = materialInstockBillDAO.selectByExample(materialInstockBillQuery);

        for(MaterialInstockBill bill: result) {
            String fromPrincipalName = adminDAO.selectByPrimaryKey(bill.getFromPrincipal()).getTrueName();
            bill.setFromPrincipalName(fromPrincipalName);
            String warehousePrincipalName = adminDAO.selectByPrimaryKey(bill.getWarehousePrincipal()).getTrueName();
            bill.setWarehousePrincipalName(warehousePrincipalName);
        }
        return new PageMode<MaterialInstockBill>(result, materialInstockBillDAO.countByExample(materialInstockBillQuery));
    }

    /**
     * 删除物料入库单
     *
     * 进行必要的检查：是否为待审核状态
     * 删除主表信息：material_instock_bill
     * 删除关联的从表信息：material_instock_bill_product
     * 添加日志信息：LogType.MATERIAL_INSTOCK, Operate.REMOVE
     *
     * @param billIdList
     */
    public void removeMaterialInstockBill(List<Integer> billIdList) {
        checkBillState(billIdList, 1);

        //删除入库单
        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andBillIdIn(billIdList);
        materialInstockBillDAO.deleteByExample(materialInstockBillQuery);
        //删除关联
        MaterialInstockBillMaterialQuery materialInstockBillMaterialQuery = new MaterialInstockBillMaterialQuery();
        materialInstockBillMaterialQuery.or().andBillIdIn(billIdList);
        materialInstockBillMaterialDAO.deleteByExample(materialInstockBillMaterialQuery);
        addLog(LogType.MATERIAL_INSTOCK, Operate.REMOVE,billIdList);
    }

    /**
     * 审核物料入库单
     *
     * @param idList 单据编号
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
        materialInstockBillDAO.updateByExampleSelective(materialInstockBill, materialInstockBillQuery);
        // 添加日志
        addLog(LogType.MATERIAL_INSTOCK, Operate.AUDIT, idList);
    }

    /**
     * 反审核物料入库单
     *
     * @param idList 单据编号
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
        addLog(LogType.MATERIAL_INSTOCK, Operate.UNAUDIT, idList);
    }

    /**
     * 完成物料入库单
     * 若为退料入库，将相关退料单转为已完成状态
     *
     * @param billId 单据编号
     */
    public void finish(Integer billId) {
        checkBillState(Collections.singletonList(billId), 2);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        MaterialInstockBill materialInstockBill = new MaterialInstockBill();
        materialInstockBill.setBillId(billId);
        materialInstockBill.setBillState(3);
        materialInstockBill.setFinishBy(loginAdmin.getAdminId());
        materialInstockBill.setFinishAt(new Date());
        materialInstockBillDAO.updateByPrimaryKeySelective(materialInstockBill);

        // 若为退料入库，将相关退料单转为已完成状态
        materialInstockBill = materialInstockBillDAO.selectByPrimaryKey(materialInstockBill.getBillId());
        if (materialInstockBill != null && materialInstockBill.getMaterialSource().equals(1)
                && !ParamUtils.isNull(materialInstockBill.getRelatedBill())) {
            ReturnMaterialBill returnMaterialBill = new ReturnMaterialBill();
            returnMaterialBill.setBillId(materialInstockBill.getRelatedBill());
            returnMaterialBill.setBillState(3);
            returnMaterialBill.setFinishBy(loginAdmin.getAdminId());
            returnMaterialBill.setFinishAt(new Date());
            returnMaterialBillDAO.updateByPrimaryKeySelective(returnMaterialBill);
        }

        // 添加日志
        addLog(LogType.MATERIAL_INSTOCK, Operate.FINISH, billId);
    }

    private void checkBillState(List<Integer> idList, int state) {
        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andBillIdIn(idList)
                .andBillStateNotEqualTo(state);
        if (materialInstockBillDAO.countByExample(materialInstockBillQuery) > 0) {
            if (state == 1 ) {
                throw new BadRequestException(BILL_STATE_NOT_UNAUDIT);
            }
            if (state == 2) {
                throw new BadRequestException(BILL_STATE_NOT_AUDIT);
            }
        }
    }

    private List<Integer> searchAdminByTrueName(String searchKey) {
        AdminQuery adminQuery = new AdminQuery();
        AdminQuery.Criteria criteria = adminQuery.or();
        criteria.andTrueNameLike("%" + searchKey + "%");
        List<Admin> adminList = adminDAO.selectByExample(adminQuery);
        List<Integer> adminIdList = new ArrayList<Integer>();
        for (Admin admin : adminList) {
            adminIdList.add(admin.getAdminId());
        }
        return adminIdList;
    }

    private static final String RETURN_MATERIAL_BILL_NOT_EXIST = "相关退料单不存在";
    private static final String RETURN_MATERIAL_BILL_NOT_AUDIT = "相关退料单不是已审核状态";
    private static final String BILL_STATE_NOT_UNAUDIT = "单据不是待审核状态";
    private static final String BILL_STATE_NOT_AUDIT = "单据不是已审核状态";
}
