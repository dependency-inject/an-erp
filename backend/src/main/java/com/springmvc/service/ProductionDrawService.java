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
@Service("ProductionDrawService")
@Transactional
public class ProductionDrawService extends BaseService {

    @Resource
    private AdminDAO adminDAO;

    @Resource
    private DrawMaterialBillDAO drawMaterialBillDAO;

    @Resource
    private DrawMaterialBillMaterialDAO drawMaterialBillMaterialDAO;

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private OrderBillProductDAO orderBillProductDAO;

    @Resource
    private MaterialOutstockBillDAO materialOutstockBillDAO;

    @Resource
    private ProductMaterialDAO productMaterialDAO;

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
            DrawMaterialBillQuery drawMaterialBillQuery = new DrawMaterialBillQuery();
            Date beginTime = ParamUtils.getCertainDate(i);
            Date endTime = ParamUtils.getCertainDate(i + 1);
            drawMaterialBillQuery.or()
                    .andDrawReasonEqualTo(1)
                    .andBillTimeGreaterThanOrEqualTo(beginTime)
                    .andBillTimeLessThan(endTime);
            preWeek.add(new StatisticsData(ParamUtils.dateConvert(beginTime, "yyyy-MM-dd"),
                    drawMaterialBillDAO.countByExample(drawMaterialBillQuery)));
        }

        List<StatisticsData> history = new ArrayList<StatisticsData>();
        for (int i = 1; i <= 3; ++i) {
            DrawMaterialBillQuery drawMaterialBillQuery = new DrawMaterialBillQuery();
            drawMaterialBillQuery.or()
                    .andDrawReasonEqualTo(1)
                    .andBillStateEqualTo(i);
            history.add(new StatisticsData("" + i, drawMaterialBillDAO.countByExample(drawMaterialBillQuery)));
        }

        return new StatisticsMode(preWeek, history);
    }

    /**
     * 查询领料单信息（单个）,只取生产领料对应的数据
     *
     * @param billId 订单编号
     * @return
     */
    public DrawMaterialBill getBillById(Integer billId) {
        DrawMaterialBill drawMaterialBill = drawMaterialBillDAO.selectByPrimaryKey(billId);
        if (drawMaterialBill.getDrawReason() != 1) {
            return null;
        }
        String toPrincipalName = adminDAO.selectByPrimaryKey(drawMaterialBill.getToPrincipal()).getTrueName();
        drawMaterialBill.setToPrincipalName(toPrincipalName);

        Integer i = drawMaterialBill.getBillState();
        if (i > 1) {
            String auditName = adminDAO.selectByPrimaryKey(drawMaterialBill.getAuditBy()).getTrueName();
            drawMaterialBill.setAuditName(auditName);
        }
        if (i > 2) {
            String warehousePrincipalName = adminDAO.selectByPrimaryKey(drawMaterialBill.getWarehousePrincipal()).getTrueName();
            drawMaterialBill.setWarehousePrincipalName(warehousePrincipalName);
            String finishName = adminDAO.selectByPrimaryKey(drawMaterialBill.getFinishBy()).getTrueName();
            drawMaterialBill.setFinishName(finishName);
        }

        String relatedBillNo = orderBillDAO.selectByPrimaryKey(drawMaterialBill.getRelatedBill()).getBillNo();
        drawMaterialBill.setRelatedBillNo(relatedBillNo);

        DrawMaterialBillMaterialQuery drawMaterialBillMaterialQuery = new DrawMaterialBillMaterialQuery();
        DrawMaterialBillMaterialQuery.Criteria criteria = drawMaterialBillMaterialQuery.or();
        criteria.andBillIdEqualTo(drawMaterialBill.getBillId());
        List<DrawMaterialBillMaterial> result = drawMaterialBillMaterialDAO.selectByExample(drawMaterialBillMaterialQuery);
        for (DrawMaterialBillMaterial item: result) {
            Material material = materialDAO.selectByPrimaryKey(item.getMaterialId());
            if (material != null) {
                item.setMaterialNo(material.getMaterialNo());
                item.setMaterialName(material.getMaterialName());
            }
        }
        drawMaterialBill.setMaterialList(result);
        return drawMaterialBill;
    }

    /**
     * 查询领料单信息（分页）,只取生产领料对应的数据
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
    public PageMode<DrawMaterialBill> pageBill(Integer current, Integer limit, String sortColumn, String  sort,
                                               String searchKey, Integer state, Date beginTime, Date endTime) {
        DrawMaterialBillQuery drawMaterialBillQuery = new DrawMaterialBillQuery();
        drawMaterialBillQuery.setOffset((current-1) * limit);
        drawMaterialBillQuery.setLimit(limit);
        // 若未指定则默认按照时间排序
        if(ParamUtils.isNull(sortColumn)) {
            sortColumn = "billTime";
            sort = "desc";
        }
        drawMaterialBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        // 搜索编号
        DrawMaterialBillQuery.Criteria criteria = drawMaterialBillQuery.or().andDrawReasonEqualTo(1);
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
        criteria = drawMaterialBillQuery.or().andDrawReasonEqualTo(1);
        if (!ParamUtils.isNull(searchKey)) {
            List<Integer> adminIdList = searchAdminByTrueName(searchKey);
            if (adminIdList.size() == 0) {
                criteria.andToPrincipalEqualTo(0);
            } else {
                criteria.andToPrincipalIn(adminIdList);
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

        List<DrawMaterialBill> result = drawMaterialBillDAO.selectByExample(drawMaterialBillQuery);

        for(DrawMaterialBill bill: result) {
            String toPrincipalName = adminDAO.selectByPrimaryKey(bill.getToPrincipal()).getTrueName();
            bill.setToPrincipalName(toPrincipalName);
            if (bill.getWarehousePrincipal() == null) {
                continue;
            }
            Admin warehousePrincipal = adminDAO.selectByPrimaryKey(bill.getWarehousePrincipal());
            if (warehousePrincipal != null) {
                bill.setWarehousePrincipalName(warehousePrincipal.getTrueName());
            }
        }
        return new PageMode<DrawMaterialBill>(result, drawMaterialBillDAO.countByExample(drawMaterialBillQuery));
    }

    /**
     * 审核领料单
     *
     * @param idList 领料单编号
     */
    public void auditBill(List<Integer> idList) {
        checkBillState(idList, 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        DrawMaterialBill drawMaterialBill = new DrawMaterialBill();
        drawMaterialBill.setBillState(2);
        drawMaterialBill.setAuditBy(loginAdmin.getAdminId());
        drawMaterialBill.setAuditAt(new Date());

        DrawMaterialBillQuery drawMaterialBillQuery = new DrawMaterialBillQuery();
        drawMaterialBillQuery.or().andBillIdIn(idList);
        drawMaterialBillDAO.updateByExampleSelective(drawMaterialBill, drawMaterialBillQuery);
        // 添加日志
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.AUDIT, idList);
    }

    /**
     * 反审核领料单
     *
     * 检查领料单是否已经有对应出库单
     *
     * @param idList 领料单编号
     */
    public void unauditBill(List<Integer> idList) {
        checkBillState(idList, 2);
        MaterialOutstockBillQuery materialOutstockBillQuery = new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andRelatedBillIn(idList);
        if (materialOutstockBillDAO.countByExample(materialOutstockBillQuery) > 0) {
            throw new BadRequestException(EXISE_OUTSTOCK_CANNOT_UNAUDIT);
        }

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        DrawMaterialBill drawMaterialBill = new DrawMaterialBill();
        drawMaterialBill.setBillState(1);
        drawMaterialBill.setAuditBy(loginAdmin.getAdminId());
        drawMaterialBill.setAuditAt(new Date());

        DrawMaterialBillQuery drawMaterialBillQuery = new DrawMaterialBillQuery();
        drawMaterialBillQuery.or().andBillIdIn(idList);
        drawMaterialBillDAO.updateByExampleSelective(drawMaterialBill, drawMaterialBillQuery);
        // 添加日志
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.UNAUDIT, idList);
    }

    /**
     * 添加领料单
     *
     * 将主表信息保存：draw_material_bill
     * 将关联的从表信息保存：draw_material_bill_material
     * 添加日志信息：LogType.DRAW_MATERIAL_BILL, Operate.ADD
     *
     * @param remark 备注
     * @param materialList 物料信息
     */
    public DrawMaterialBill addBill(Integer relatedBill, String remark, List<DrawMaterialBillMaterial> materialList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        DrawMaterialBill drawMaterialBill = new DrawMaterialBill();
        drawMaterialBill.setBillNo("PDM" + ParamUtils.dateConvert(new Date(), "yyMMddHHmmssSSS"));
        drawMaterialBill.setToPrincipal(loginAdmin.getAdminId());
        drawMaterialBill.setBillTime(new Date());
        drawMaterialBill.setDrawReason(1);
        drawMaterialBill.setRelatedBill(relatedBill);
        drawMaterialBill.setBillState(1);
        drawMaterialBill.setRemark(remark);
        drawMaterialBill.setCreateAt(new Date());
        drawMaterialBill.setCreateBy(loginAdmin.getAdminId());
        drawMaterialBill.setUpdateAt(new Date());
        drawMaterialBill.setUpdateBy(loginAdmin.getAdminId());
        drawMaterialBillDAO.insertSelective(drawMaterialBill);

        for (DrawMaterialBillMaterial material: materialList) {
            material.setBillId(drawMaterialBill.getBillId());
            drawMaterialBillMaterialDAO.insert(material);
        }
        // 添加日志
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.ADD, drawMaterialBill.getBillId());

        // 检查相应订单状态
        OrderBill orderBill = orderBillDAO.selectByPrimaryKey(drawMaterialBill.getRelatedBill());
        if (orderBill == null) {
            throw new BadRequestException(ORDER_BILL_NOT_EXIST);
        }
        if (!orderBill.getBillState().equals(3)) {
            throw new BadRequestException(ORDER_BILL_NOT_PRODUCE);
        }

        return getBillById(drawMaterialBill.getBillId());
    }

    /**
     * 更新领料单
     *
     * 进行必要的检查：是否为待审核状态
     * 更新主表信息：draw_material_bill
     * 更新关联的从表信息：draw_material_bill_material
     * 添加日志信息：LogType.DRAW_MATERIAL_BILL, Operate.UPDATE
     *
     * @param remark 备注
     * @param materialList 物料信息
     */
    public DrawMaterialBill updateBill(Integer billId, Integer relatedBill, String remark, List<DrawMaterialBillMaterial> materialList) {
        checkBillState(Collections.singletonList(billId), 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        DrawMaterialBill drawMaterialBill = new DrawMaterialBill();
        drawMaterialBill.setBillId(billId);
//        drawMaterialBill.setRelatedBill(relatedBill);
        drawMaterialBill.setRemark(remark);
        drawMaterialBill.setUpdateAt(new Date());
        drawMaterialBill.setUpdateBy(loginAdmin.getAdminId());
        drawMaterialBillDAO.updateByPrimaryKeySelective(drawMaterialBill);

        // 先删除原来所有draw_material_bill_material
        DrawMaterialBillMaterialQuery drawMaterialBillMaterialQuery = new DrawMaterialBillMaterialQuery();
        drawMaterialBillMaterialQuery.or().andBillIdEqualTo(drawMaterialBill.getBillId());
        drawMaterialBillMaterialDAO.deleteByExample(drawMaterialBillMaterialQuery);
        // 再新增现有关联draw_material_bill_material
        for (DrawMaterialBillMaterial material: materialList) {
            material.setBillId(drawMaterialBill.getBillId());
            drawMaterialBillMaterialDAO.insert(material);
        }
        // 添加日志
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.UPDATE, drawMaterialBill.getBillId());
        return getBillById(drawMaterialBill.getBillId());
    }

    /**
     * 删除领料单
     *
     * 进行必要的检查：是否为待审核状态
     * 删除主表信息：draw_material_bill
     * 删除关联的从表信息：draw_material_bill_material
     * 添加日志信息：LogType.DRAW_MATERIAL_BILL, Operate.UPDATE
     *
     * @param idList
     */
    public void removeBill(List<Integer> idList) {
        checkBillState(idList, 1);

        // 删除 draw_material_bill
        DrawMaterialBillQuery drawMaterialBillQuery = new DrawMaterialBillQuery();
        drawMaterialBillQuery.or().andBillIdIn(idList);
        drawMaterialBillDAO.deleteByExample(drawMaterialBillQuery);
        // 删除关联 draw_material_bill_material
        DrawMaterialBillMaterialQuery drawMaterialBillMaterialQuery = new DrawMaterialBillMaterialQuery();
        drawMaterialBillMaterialQuery.or().andBillIdIn(idList);
        drawMaterialBillMaterialDAO.deleteByExample(drawMaterialBillMaterialQuery);
        // 添加日志
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.REMOVE, idList);
    }

    private void checkBillState(List<Integer> idList, int state) {
        DrawMaterialBillQuery drawMaterialBillQuery = new DrawMaterialBillQuery();
        drawMaterialBillQuery.or().andBillIdIn(idList)
                .andBillStateNotEqualTo(state);
        if (drawMaterialBillDAO.countByExample(drawMaterialBillQuery) > 0) {
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

    /**
     * 订单物料分解
     *
     * 进行必要的检查：是否为生产中状态
     *
     * @param relatedBillId
     * @return
     */
    public List<DrawMaterialBillMaterial> getAllMaterial(int relatedBillId) {
        OrderBillProductQuery orderBillProductQuery = new OrderBillProductQuery();
        OrderBillProductQuery.Criteria criteria = orderBillProductQuery.or();
        criteria.andBillIdEqualTo(relatedBillId);
        List<OrderBillProduct> productList = orderBillProductDAO.selectByExample(orderBillProductQuery);
        Map<Integer, Integer> materialMap = new HashMap<Integer, Integer>();
        for (OrderBillProduct orderBillProduct : productList) {
            ProductMaterialQuery productMaterialQuery = new ProductMaterialQuery();
            productMaterialQuery.or().andProductIdEqualTo(orderBillProduct.getProductId());
            List<ProductMaterial> productMaterialList = productMaterialDAO.selectByExample(productMaterialQuery);

            for (ProductMaterial productMaterial: productMaterialList) {
                if (!materialMap.containsKey(productMaterial.getMaterialId())) {
                    materialMap.put(productMaterial.getMaterialId(), 0);
                }
                materialMap.put(productMaterial.getMaterialId(), materialMap.get(productMaterial.getMaterialId()) + productMaterial.getQuantity() * orderBillProduct.getQuantity());
            }
        }

        List<DrawMaterialBillMaterial> result = new ArrayList<DrawMaterialBillMaterial>();
        for (Integer materialId : materialMap.keySet()) {
            if (materialMap.get(materialId) <= 0) {
                continue;
            }
            Material material = materialDAO.selectByPrimaryKey(materialId);
            if (material != null) {
                DrawMaterialBillMaterial drawMaterialBillMaterial = new DrawMaterialBillMaterial();
                drawMaterialBillMaterial.setMaterialId(material.getMaterialId());
                drawMaterialBillMaterial.setMaterialNo(material.getMaterialNo());
                drawMaterialBillMaterial.setMaterialName(material.getMaterialName());
                drawMaterialBillMaterial.setQuantity(materialMap.get(materialId));
                result.add(drawMaterialBillMaterial);
            }
        }
        return result;
    }

    private static final String ORDER_BILL_NOT_EXIST = "相关订单不存在";
    private static final String ORDER_BILL_NOT_PRODUCE = "相关订单不是生产中状态";
    private static final String BILL_STATE_NOT_UNAUDIT = "单据不是待审核状态";
    private static final String BILL_STATE_NOT_AUDIT = "单据不是已审核状态";
    private static final String EXISE_OUTSTOCK_CANNOT_UNAUDIT = "单据已存在对应出库单，不能反审核";
}
