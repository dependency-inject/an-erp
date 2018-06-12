package com.springmvc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

@Service("ProductionReturnService")
@Transactional
public class ProductionReturnService extends BaseService {

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

    @Resource
    private OrderBillDAO orderBillDAO;

    /**
     * 获取统计信息
     *
     * @return
     */
    public StatisticsMode getStatistics() {
        List<StatisticsData> preWeek = new ArrayList<StatisticsData>();
        for (int i = -6; i <= 0; ++i) {
            ReturnMaterialBillQuery returnMaterialBillQuery = new ReturnMaterialBillQuery();
            Date beginTime = ParamUtils.getCertainDate(i);
            Date endTime = ParamUtils.getCertainDate(i + 1);
            returnMaterialBillQuery.or()
                    .andReturnReasonEqualTo(1)
                    .andBillTimeGreaterThanOrEqualTo(beginTime)
                    .andBillTimeLessThan(endTime);
            preWeek.add(new StatisticsData(ParamUtils.dateConvert(beginTime, "yyyy-MM-dd"),
                    returnMaterialBillDAO.countByExample(returnMaterialBillQuery)));
        }

        List<StatisticsData> history = new ArrayList<StatisticsData>();
        for (int i = 1; i <= 3; ++i) {
            ReturnMaterialBillQuery returnMaterialBillQuery = new ReturnMaterialBillQuery();
            returnMaterialBillQuery.or()
                    .andReturnReasonEqualTo(1)
                    .andBillStateEqualTo(i);
            history.add(new StatisticsData("" + i, returnMaterialBillDAO.countByExample(returnMaterialBillQuery)));
        }

        return new StatisticsMode(preWeek, history);
    }

    /**
     * 查询退料单信息（单个）,只取生产退料对应的数据
     *
     * @param billId 订单编号
     * @return
     */
    public ReturnMaterialBill getBillById(Integer billId) {
        ReturnMaterialBill returnMaterialBill = returnMaterialBillDAO.selectByPrimaryKey(billId);
        if(returnMaterialBill.getReturnReason() != 1) {
            return null;
        }
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

        String relatedBillNo = orderBillDAO.selectByPrimaryKey(returnMaterialBill.getRelatedBill()).getBillNo();
        returnMaterialBill.setRelatedBillNo(relatedBillNo);

        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery = new ReturnMaterialBillMaterialQuery();
        ReturnMaterialBillMaterialQuery.Criteria criteria = returnMaterialBillMaterialQuery.or();
        criteria.andBillIdEqualTo(returnMaterialBill.getBillId());
        List<ReturnMaterialBillMaterial> returnMaterialBillMaterials = returnMaterialBillMaterialDAO.selectByExample(returnMaterialBillMaterialQuery);
        for (ReturnMaterialBillMaterial item : returnMaterialBillMaterials) {
            Material material = materialDAO.selectByPrimaryKey(item.getMaterialId());
            if (material != null) {
                item.setMaterialNo(material.getMaterialNo());
                item.setMaterialName(material.getMaterialName());
            }
        }
        returnMaterialBill.setMaterialList(returnMaterialBillMaterials);
        return returnMaterialBill;
    }

    /**
     * 查询退料单信息（分页）,只取生产退料对应的数据
     *
     * 将主表信息取出：（同时包含总记录数）
     * 搜索字段：编号、退料人
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

        // 搜索编号
        ReturnMaterialBillQuery.Criteria criteria = returnMaterialBillQuery.or().andReturnReasonEqualTo(1);
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
        // 搜索退料人
        criteria = returnMaterialBillQuery.or().andReturnReasonEqualTo(1);
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
            String fromPrincipalName = adminDAO.selectByPrimaryKey(bill.getFromPrincipal()).getTrueName();
            bill.setFromPrincipalName(fromPrincipalName);
            if (bill.getWarehousePrincipal() == null) {
                continue;
            }
            Admin warehousePrincipal = adminDAO.selectByPrimaryKey(bill.getWarehousePrincipal());
            if (warehousePrincipal != null) {
                bill.setWarehousePrincipalName(warehousePrincipal.getTrueName());
            }
        }
        return new PageMode<ReturnMaterialBill>(result, returnMaterialBillDAO.countByExample(returnMaterialBillQuery));
    }

    /**
     * 审核退料单
     *
     * @param idList 退料单编号
     */
    public void auditBill(List<Integer> idList) {
        checkBillState(idList,1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ReturnMaterialBill returnMaterialBill = new ReturnMaterialBill();
        returnMaterialBill.setBillState(2);
        returnMaterialBill.setAuditAt(new Date());
        returnMaterialBill.setAuditBy(loginAdmin.getAdminId());

        ReturnMaterialBillQuery returnMaterialBillQuery=new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillIdIn(idList);
        returnMaterialBillDAO.updateByExampleSelective(returnMaterialBill, returnMaterialBillQuery);
        // 添加日志
        addLog(LogType.RETURN_MATERIAL_BILL, Operate.AUDIT, idList);
    }

    /**
     * 反审核退料单
     *
     * 检查退料单是否已经有对应入库单
     *
     * @param idList 领料单编号
     */
    public void unauditBill(List<Integer> idList) {
        checkBillState(idList, 2);
        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andRelatedBillIn(idList);
        if (materialInstockBillDAO.countByExample(materialInstockBillQuery) > 0) {
            throw new BadRequestException(EXISE_INSTOCK_CANNOT_UNAUDIT);
        }

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ReturnMaterialBill returnMaterialBill = new ReturnMaterialBill();
        returnMaterialBill.setBillState(1);
        returnMaterialBill.setAuditBy(loginAdmin.getAdminId());
        returnMaterialBill.setAuditAt(new Date());

        ReturnMaterialBillQuery returnMaterialBillQuery = new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillIdIn(idList);
        returnMaterialBillDAO.updateByExampleSelective(returnMaterialBill, returnMaterialBillQuery);
        // 添加日志
        addLog(LogType.RETURN_MATERIAL_BILL, Operate.UNAUDIT, idList);
    }

    /**
     * 添加退料单
     *
     * 将主表信息保存：return_material_bill
     * 将关联的从表信息保存：return_material_bill_material
     * 添加日志信息：LogType.RETURN_MATERIAL_BILL, Operate.ADD
     *
     * @param remark       备注
     * @param materialList 物料信息
     */
    public ReturnMaterialBill addBill(Integer relatedBill, String remark, List<ReturnMaterialBillMaterial> materialList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ReturnMaterialBill returnMaterialBill=new ReturnMaterialBill();
        returnMaterialBill.setBillNo("PRM" + ParamUtils.dateConvert(new Date(), "yyMMddHHmmssSSS"));
        returnMaterialBill.setFromPrincipal(loginAdmin.getAdminId());
        returnMaterialBill.setBillTime(new Date());
        returnMaterialBill.setReturnReason(1);
        returnMaterialBill.setRelatedBill(relatedBill);
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

        // 检查相应订单状态
        OrderBill orderBill = orderBillDAO.selectByPrimaryKey(returnMaterialBill.getRelatedBill());
        if (orderBill == null) {
            throw new BadRequestException(ORDER_BILL_NOT_EXIST);
        }
        if (!orderBill.getBillState().equals(3)) {
            throw new BadRequestException(ORDER_BILL_NOT_PRODUCE);
        }

        return getBillById(returnMaterialBill.getBillId());
    }

    /**
     * 更新退料单
     *
     * 进行必要的检查：是否为待审核状态
     * 更新主表信息：return_material_bill
     * 更新关联的从表信息：return_material_bill_material
     * 添加日志信息：LogType.RETURN_MATERIAL_BILL, Operate.UPDATE
     *
     * @param remark       备注
     * @param materialList 物料信息
     */
    public ReturnMaterialBill updateBill(Integer billId, Integer relatedBill, String remark, List<ReturnMaterialBillMaterial> materialList) {
        checkBillState(Collections.singletonList(billId), 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ReturnMaterialBill returnMaterialBill = new ReturnMaterialBill();
        returnMaterialBill.setBillId(billId);
//        returnMaterialBill.setRelatedBill(relatedBill);
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
     *
     * 进行必要的检查：是否为待审核状态
     * 删除主表信息：return_material_bill
     * 删除关联的从表信息：return_material_bill_material
     * 添加日志信息：LogType.RETURN_MATERIAL_BILL, Operate.UPDATE
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

    private static final String ORDER_BILL_NOT_EXIST = "相关订单不存在";
    private static final String ORDER_BILL_NOT_PRODUCE = "相关订单不是生产中状态";
    private static final String BILL_STATE_NOT_UNAUDIT = "单据不是待审核状态";
    private static final String BILL_STATE_NOT_AUDIT = "单据不是已审核状态";
    private static final String EXISE_INSTOCK_CANNOT_UNAUDIT = "单据已存在对应入库单，不能反审核";
}
