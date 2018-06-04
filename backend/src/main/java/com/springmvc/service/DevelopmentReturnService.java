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
import java.util.*;

@Service("DevelopmentReturnService")
@Transactional
public class DevelopmentReturnService extends BaseService {
    @Resource
    private AdminDAO adminDAO;
    @Resource
    private ReturnMaterialBillDAO returnMaterialBillDAO;
    @Resource
    private ReturnMaterialBillMaterialDAO returnMaterialBillMaterialDAO;
    @Resource
    private MaterialDAO materialDAO;
    @Resource
    private MaterialInstockBillDAO materialInstockBillDAO;
    public ReturnMaterialBill getBillById(Integer billId) {
        ReturnMaterialBill returnMaterialBill=returnMaterialBillDAO.selectByPrimaryKey(billId);
        if(returnMaterialBill.getReturnReason()!=2)
            return null;
        String fromPrincipalName = adminDAO.selectByPrimaryKey(returnMaterialBill.getFromPrincipal()).getTrueName();
        returnMaterialBill.setFromPrincipalName(fromPrincipalName);
        Integer i = returnMaterialBill.getBillState();
        if (i > 1) {
            String auditName = adminDAO.selectByPrimaryKey(returnMaterialBill.getAuditBy()).getTrueName();
            returnMaterialBill.setAuditAtName(auditName);
        }
        if (i > 2) {
            String warehousePrincipalName = adminDAO.selectByPrimaryKey(returnMaterialBill.getWarehousePrincipal()).getTrueName();
            returnMaterialBill.setWarehousePrincipalName(warehousePrincipalName);
            String finishName = adminDAO.selectByPrimaryKey(returnMaterialBill.getFinishBy()).getTrueName();
            returnMaterialBill.setFinishByName(finishName);
        }
        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery=new ReturnMaterialBillMaterialQuery();
        ReturnMaterialBillMaterialQuery.Criteria criteria = returnMaterialBillMaterialQuery.or();
        criteria.andBillIdEqualTo(billId);
        List<ReturnMaterialBillMaterial> returnMaterialBillMaterials=returnMaterialBillMaterialDAO.selectByExample(returnMaterialBillMaterialQuery);
        for (ReturnMaterialBillMaterial item : returnMaterialBillMaterials) {
            Material material = materialDAO.selectByPrimaryKey(item.getMaterialId());
            if (material != null) {
                item.setMaterialName(material.getMaterialName());
            }
        }
        returnMaterialBill.setMaterialList(returnMaterialBillMaterials);
        return returnMaterialBill;
    }
    public List<ReturnMaterialBillMaterial> getMaterials(int billid){
        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery=new ReturnMaterialBillMaterialQuery();
        ReturnMaterialBillMaterialQuery.Criteria criteria = returnMaterialBillMaterialQuery.or();
        criteria.andBillIdEqualTo(billid);
        List<ReturnMaterialBillMaterial> returnMaterialBillMaterials=returnMaterialBillMaterialDAO.selectByExample(returnMaterialBillMaterialQuery);
        for (ReturnMaterialBillMaterial item : returnMaterialBillMaterials) {
            Material material = materialDAO.selectByPrimaryKey(item.getMaterialId());
            if (material != null) {
                item.setMaterialName(material.getMaterialName());
            }
        }
        return returnMaterialBillMaterials;
    }

    /**
     * 查询领料单信息（分页）,只取研发领料对应的数据
     *
     * 将主表信息取出：（同时包含总记录数）
     * 搜索字段：编号、领料人
     * 筛选字段：状态
     *
     * @param current 当前位置
     * @param limit 一次读取长度
     * @param sortColumn 按哪一列排序
     * @param sort  排序方式 升序 降序
     * @param searchKey 关键字查找
     * @param state 订单状态
     * @return
     */
    public PageMode<ReturnMaterialBill> pageBill(Integer current, Integer limit, String sortColumn, String  sort,
                                                 String searchKey, Integer state, Date beginTime, Date endTime) {
        ReturnMaterialBillQuery returnMaterialBillQuery = new ReturnMaterialBillQuery();
        returnMaterialBillQuery.setOffset((current-1) * limit);
        returnMaterialBillQuery.setLimit(limit);
        // 若未指定则默认按照时间排序
        if(ParamUtils.isNull(sortColumn)) {
            sortColumn = "billTime";
            sort = "desc";
        }
        returnMaterialBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        ReturnMaterialBillQuery.Criteria criteria = returnMaterialBillQuery.or().andReturnReasonEqualTo(2);
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
        // 搜索领料人
        criteria = returnMaterialBillQuery.or().andReturnReasonEqualTo(2);
        if (!ParamUtils.isNull(searchKey)) {
            List<Integer> adminIdList = searchAdminByTrueName(searchKey);
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

        List<ReturnMaterialBill> result = returnMaterialBillDAO.selectByExample(returnMaterialBillQuery);

        for(ReturnMaterialBill bill: result) {
            String name = adminDAO.selectByPrimaryKey(bill.getFromPrincipal()).getTrueName();
            bill.setFromPrincipalName(name);
        }
        return new PageMode<ReturnMaterialBill>(result, returnMaterialBillDAO.countByExample(returnMaterialBillQuery));
    }

    /**
     * 审核退料单
     *
     * @param idList 领料单编号
     */
    public void auditBill(List<Integer> idList) {
        checkBillState(idList,1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ReturnMaterialBill returnMaterialBill=new ReturnMaterialBill();
        returnMaterialBill.setBillState(2);
        returnMaterialBill.setAuditAt(new Date());
        returnMaterialBill.setAuditBy(loginAdmin.getAdminId());
        ReturnMaterialBillQuery returnMaterialBillQuery=new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillIdIn(idList);
        returnMaterialBillDAO.updateByExampleSelective(returnMaterialBill,returnMaterialBillQuery);
        // 添加日志
        addLog(LogType.RETURN_MATERIAL_BILL, Operate.AUDIT, idList);
    }

    /**
     * 反审核领料单
     * <p>
     * 检查领料单是否已经有对应出库单
     *
     * @param idList 领料单编号
     */
    public void unauditBill(List<Integer> idList) {
        checkBillState(idList, 2);
        MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andRelatedBillIn(idList);
        if(materialInstockBillDAO.countByExample(materialInstockBillQuery)>0) {
            throw new BadRequestException(EXISE_INSTOCK_CANNOT_UNAUDIT);
        }

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ReturnMaterialBill returnMaterialBill=new ReturnMaterialBill();
        returnMaterialBill.setBillState(1);
        returnMaterialBill.setAuditBy(loginAdmin.getAdminId());
        returnMaterialBill.setAuditAt(new Date());
        ReturnMaterialBillQuery returnMaterialBillQuery=new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillIdIn(idList);
        returnMaterialBillDAO.updateByExampleSelective(returnMaterialBill,returnMaterialBillQuery);
        // 添加日志
        addLog(LogType.RETURN_MATERIAL_BILL, Operate.UNAUDIT, idList);
    }

    /**
     * 获取所有可选的物料
     */
    public List<Material> getMaterialList() {
        return materialDAO.selectByExample(new MaterialQuery());
    }

    /**
     * 添加领料单
     * <p>
     * 将主表信息保存：return_material_bill
     * 将关联的从表信息保存：return_material_bill_material
     * 添加日志信息：LogType.return_MATERIAL_BILL, Operate.ADD
     *
     * @param remark       备注
     * @param materialList 物料信息
     */
    public ReturnMaterialBill addBill(String remark, List<ReturnMaterialBillMaterial> materialList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ReturnMaterialBill returnMaterialBill=new ReturnMaterialBill();
        returnMaterialBill.setBillNo("DDM" + ParamUtils.dateConvert(new Date(), "yyMMddHHmmssSSS"));
        returnMaterialBill.setFromPrincipal(loginAdmin.getAdminId());
        returnMaterialBill.setBillTime(new Date());
        returnMaterialBill.setReturnReason(2);
        returnMaterialBill.setBillState(1);
        returnMaterialBill.setRemark(remark);
        returnMaterialBill.setCreateAt(new Date());
        returnMaterialBill.setCreateBy(loginAdmin.getAdminId());
        returnMaterialBill.setUpdateAt(new Date());
        returnMaterialBill.setUpdateBy(loginAdmin.getAdminId());
        returnMaterialBillDAO.insertSelective(returnMaterialBill);

        for (ReturnMaterialBillMaterial material : materialList) {
            material.setBillId(returnMaterialBill.getBillId());
            returnMaterialBillMaterialDAO.insert(material);
        }
        // 添加日志
        addLog(LogType.RETURN_MATERIAL_BILL, Operate.ADD, returnMaterialBill.getBillId());
        return getBillById(returnMaterialBill.getBillId());
    }

    /**
     * 更新领料单
     * <p>
     * 进行必要的检查：是否为待审核状态
     * 更新主表信息：return_material_bill
     * 更新关联的从表信息：return_material_bill_material
     * 添加日志信息：LogType.return_MATERIAL_BILL, Operate.UPDATE
     *
     * @param remark       备注
     * @param materialList 物料信息
     */
    public ReturnMaterialBill updateBill(Integer billId, String remark, List<ReturnMaterialBillMaterial> materialList) {
        checkBillState(Collections.singletonList(billId), 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ReturnMaterialBill returnMaterialBill = new ReturnMaterialBill();
        returnMaterialBill.setBillId(billId);
        returnMaterialBill.setRemark(remark);
        returnMaterialBill.setUpdateAt(new Date());
        returnMaterialBill.setUpdateBy(loginAdmin.getAdminId());
        returnMaterialBillDAO.updateByPrimaryKeySelective(returnMaterialBill);

        // 先删除原来所有return_material_bill_material
        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery = new ReturnMaterialBillMaterialQuery();
        returnMaterialBillMaterialQuery.or().andBillIdEqualTo(returnMaterialBill.getBillId());
        returnMaterialBillMaterialDAO.deleteByExample(returnMaterialBillMaterialQuery);
        // 再新增现有关联return_material_bill_material
        for (ReturnMaterialBillMaterial material : materialList) {
            material.setBillId(returnMaterialBill.getBillId());
            returnMaterialBillMaterialDAO.insert(material);
        }
        // 添加日志
        addLog(LogType.RETURN_MATERIAL_BILL, Operate.UPDATE, returnMaterialBill.getBillId());
        return getBillById(returnMaterialBill.getBillId());
    }

    /**
     * 删除领料单
     * <p>
     * 进行必要的检查：是否为待审核状态
     * 删除主表信息：return_material_bill
     * 删除关联的从表信息：return_material_bill_material
     * 添加日志信息：LogType.return_MATERIAL_BILL, Operate.UPDATE
     *
     * @param idList
     */
    public void removeBill(List<Integer> idList) {
        checkBillState(idList, 1);

        // 删除 return_material_bill
        ReturnMaterialBillQuery returnMaterialBillQuery = new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillIdIn(idList);
        returnMaterialBillDAO.deleteByExample(returnMaterialBillQuery);
        // 删除关联 return_material_bill_material
        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery = new ReturnMaterialBillMaterialQuery();
        returnMaterialBillMaterialQuery.or().andBillIdIn(idList);
        returnMaterialBillMaterialDAO.deleteByExample(returnMaterialBillMaterialQuery);
        // 添加日志
        addLog(LogType.RETURN_MATERIAL_BILL, Operate.REMOVE, idList);
    }

    private void checkBillState(List<Integer> idList, int state) {
        ReturnMaterialBillQuery returnMaterialBillQuery = new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillIdIn(idList)
                .andBillStateNotEqualTo(state);
        if (returnMaterialBillDAO.countByExample(returnMaterialBillQuery) > 0) {
            if (state == 1) {
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

    private static final String BILL_STATE_NOT_UNAUDIT = "单据不是待审核状态";
    private static final String BILL_STATE_NOT_AUDIT = "单据不是已审核状态";
    private static final String EXISE_INSTOCK_CANNOT_UNAUDIT = "单据已存在对应入库单，不能反审核";
}