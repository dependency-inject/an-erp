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

@Service("ProductOutstockService")
@Transactional
public class ProductOutstockService extends BaseService {

    @Resource
    private AdminDAO adminDAO;

    @Resource
    private WarehouseDAO warehouseDAO;

    @Resource
    private ProductDAO productDAO;

    @Resource
    private ProductOutstockBillDAO productOutstockBillDAO;

    @Resource
    private ProductOutstockBillProductDAO productOutstockBillProductDAO;

    @Resource
    private OrderBillDAO orderBillDAO;

    private static final String ORDER_BILL_NOT_EXIST = "相关订单不存在";
    private static final String ORDER_BILL_NOT_PRODUCE = "相关订单不是生产中状态";
    private static final String BILL_STATE_NOT_UNAUDIT = "单据不是待审核状态";
    private static final String BILL_STATE_NOT_AUDIT = "单据不是已审核状态";
    private static final String STOCK_QUANTITY_NOT_ENOUGH = "货品库存数量不足";

    /**
     * 添加货品出库单
     *
     * 将主表信息保存：product_outstock_bill
     * 将关联的从表信息保存：product_outstock_bill_product
     * 添加日志信息：LogType.PRODUCT_OUTSTOCK, Operate.ADD
     * 若为发货出库，检查相应订单状态
     *
     * @param toPrincipal
     * @param productWhereabouts
     * @param relatedBill
     * @param remark
     * @param productList
     * @return
     */
    public ProductOutstockBill addProductOutsockBill(Integer toPrincipal, Integer productWhereabouts, Integer relatedBill,
                                                     String remark, List<ProductOutstockBillProduct> productList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductOutstockBill productOutstockBill = new ProductOutstockBill();
        productOutstockBill.setBillNo("PO" + ParamUtils.dateConvert(new Date(), "yyMMddHHmmssSSS"));
        productOutstockBill.setToPrincipal(toPrincipal);
        productOutstockBill.setWarehousePrincipal(loginAdmin.getAdminId());
        productOutstockBill.setBillTime(new Date());
        productOutstockBill.setProductWhereabouts(productWhereabouts);
        if (productWhereabouts.equals(1)) {
            productOutstockBill.setRelatedBill(relatedBill);
        }
        productOutstockBill.setBillState(1);
        productOutstockBill.setRemark(remark);
        productOutstockBill.setCreateAt(new Date());
        productOutstockBill.setCreateBy(loginAdmin.getAdminId());
        productOutstockBill.setUpdateAt(new Date());
        productOutstockBill.setUpdateBy(loginAdmin.getAdminId());
        productOutstockBillDAO.insertSelective(productOutstockBill);

        // 检查库存数量
        checkStockQuantity(productList);
        // 将关联的从表信息保存
        for (ProductOutstockBillProduct product: productList) {
            product.setBillId(productOutstockBill.getBillId());
            productOutstockBillProductDAO.insert(product);
        }

        addLog(LogType.PRODUCT_OUTSTOCK,Operate.ADD,productOutstockBill.getBillId());

        // 若为发货出库，检查相应订单状态
        if (productOutstockBill.getProductWhereabouts().equals(1)) {
            OrderBill orderBill = orderBillDAO.selectByPrimaryKey(productOutstockBill.getRelatedBill());
            if (orderBill == null) {
                throw new BadRequestException(ORDER_BILL_NOT_EXIST);
            }
            if (!orderBill.getBillState().equals(3)) {
                throw new BadRequestException(ORDER_BILL_NOT_PRODUCE);
            }
        }

        return getProductOutstockBillById(productOutstockBill.getBillId());
    }


    /**
     * 更新货品出库单
     *
     * 进行必要的检查：是否为待审核状态
     * 更新主表信息：product_outstock_bill
     * 更新关联的从表信息：product_outstock_bill_product
     * 添加日志信息：LogType.PRODUCT_OUTSTOCK, Operate.UPDATE
     *
     * @param billId
     * @param toPrincipal
     * @param productWhereabouts
     * @param remark
     * @param productList
     * @return
     */
    public ProductOutstockBill updateProductOutsockBill(Integer billId, Integer toPrincipal, Integer productWhereabouts, Integer relatedBill,
                                                        String remark, List<ProductOutstockBillProduct> productList) {
        checkBillState(Collections.singletonList(billId), 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductOutstockBill productOutstockBill=new ProductOutstockBill();
        productOutstockBill.setBillId(billId);
        productOutstockBill.setToPrincipal(toPrincipal);
//        productOutstockBill.setProductWhereabouts(productWhereabouts);
//        productOutstockBill.setRelatedBill(relatedBill);
        productOutstockBill.setRemark(remark);
        productOutstockBill.setUpdateAt(new Date());
        productOutstockBill.setUpdateBy(loginAdmin.getAdminId());
        productOutstockBillDAO.updateByPrimaryKeySelective(productOutstockBill);

        // 先删除关联的从表信息
        ProductOutstockBillProductQuery productOutstockBillProductQuery = new ProductOutstockBillProductQuery();
        productOutstockBillProductQuery.or().andBillIdEqualTo(productOutstockBill.getBillId());
        productOutstockBillProductDAO.deleteByExample(productOutstockBillProductQuery);
        // 检查库存数量
        checkStockQuantity(productList);
        // 再将关联的从表信息保存
        for (ProductOutstockBillProduct product : productList){
            product.setBillId(productOutstockBill.getBillId());
            productOutstockBillProductDAO.insert(product);
        }
        addLog(LogType.PRODUCT_OUTSTOCK, Operate.UPDATE, productOutstockBill.getBillId());
        return getProductOutstockBillById(productOutstockBill.getBillId());
    }

    /**
     * 查询货品出库单信息（单个）
     *
     * @param billId 订单编号
     * @return
     */
    public ProductOutstockBill getProductOutstockBillById(int billId) {
        ProductOutstockBill productOutstockBill=productOutstockBillDAO.selectByPrimaryKey(billId);

        String toPrincipalName = adminDAO.selectByPrimaryKey(productOutstockBill.getToPrincipal()).getTrueName();
        productOutstockBill.setToPrincipalName(toPrincipalName);
        String warehousePrincipalName = adminDAO.selectByPrimaryKey(productOutstockBill.getWarehousePrincipal()).getTrueName();
        productOutstockBill.setWarehousePrincipalName(warehousePrincipalName);

        Integer i = productOutstockBill.getBillState();
        if (i > 1) {
            String auditName = adminDAO.selectByPrimaryKey(productOutstockBill.getAuditBy()).getTrueName();
            productOutstockBill.setAuditName(auditName);
        }
        if (i > 2) {
            String finishName = adminDAO.selectByPrimaryKey(productOutstockBill.getFinishBy()).getTrueName();
            productOutstockBill.setFinishName(finishName);
        }

        if (productOutstockBill.getProductWhereabouts().equals(1)) {
            String relatedBillNo = orderBillDAO.selectByPrimaryKey(productOutstockBill.getRelatedBill()).getBillNo();
            productOutstockBill.setRelatedBillNo(relatedBillNo);
        }

        ProductOutstockBillProductQuery productOutstockBillProductQuery = new ProductOutstockBillProductQuery();
        ProductOutstockBillProductQuery.Criteria criteria = productOutstockBillProductQuery.or();
        criteria.andBillIdEqualTo(productOutstockBill.getBillId());
        List<ProductOutstockBillProduct> result = productOutstockBillProductDAO.selectByExample(productOutstockBillProductQuery);
        for(ProductOutstockBillProduct item: result) {
            Product product = productDAO.selectByPrimaryKey(item.getProductId());
            if (product != null) {
                item.setProductNo(product.getProductNo());
                item.setProductName(product.getProductName());
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
        productOutstockBill.setProductList(result);
        return productOutstockBill;
    }


    /**
     * 查询货品入库单信息（分页）
     *
     * 将主表信息取出：（同时包含总记录数）
     * 搜索字段：编号、交货人、仓库负责人
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
    public PageMode<ProductOutstockBill> pageProductOutstockBill(Integer current, Integer limit, String sortColumn, String sort,
                                                                 String searchKey, Integer state, Date beginTime, Date endTime) {
        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
        productOutstockBillQuery.setOffset((current-1)*limit);
        productOutstockBillQuery.setLimit(limit);
        // 若未指定则默认按照时间排序
        if(ParamUtils.isNull(sortColumn)) {
            sortColumn = "billTime";
            sort = "desc";
        }
        productOutstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        // 查询编号
        ProductOutstockBillQuery.Criteria criteria = productOutstockBillQuery.or();
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
        // 查询领货人
        List<Integer> adminIdList = searchAdminByTrueName(searchKey);
        criteria = productOutstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
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
        // 查询仓库负责人
        criteria = productOutstockBillQuery.or();
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

        List<ProductOutstockBill> result=productOutstockBillDAO.selectByExample(productOutstockBillQuery);

        for(ProductOutstockBill bill: result) {
            String toPrincipalName = adminDAO.selectByPrimaryKey(bill.getToPrincipal()).getTrueName();
            bill.setToPrincipalName(toPrincipalName);
            String warehousePrincipalName = adminDAO.selectByPrimaryKey(bill.getWarehousePrincipal()).getTrueName();
            bill.setWarehousePrincipalName(warehousePrincipalName);
        }
        return new PageMode<ProductOutstockBill>(result,productOutstockBillDAO.countByExample(productOutstockBillQuery));
    }


    /**
     * 删除货品出库单
     *
     * 进行必要的检查：是否为待审核状态
     * 删除主表信息：product_outstock_bill
     * 删除关联的从表信息：product_outstock_bill_product
     * 添加日志信息：LogType.PRODUCT_OUTSTOCK, Operate.REMOVE
     *
     * @param billIdList
     */
    public void removeProductBill(List<Integer> billIdList) {
        checkBillState(billIdList, 1);

        //删除出库单
        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillIdIn(billIdList);
        productOutstockBillDAO.deleteByExample(productOutstockBillQuery);
        //删除关联
        ProductOutstockBillProductQuery productOutstockBillProductQuery =new ProductOutstockBillProductQuery();
        productOutstockBillProductQuery.or().andBillIdIn(billIdList);
        productOutstockBillProductDAO.deleteByExample(productOutstockBillProductQuery);
        addLog(LogType.PRODUCT_OUTSTOCK, Operate.REMOVE,billIdList);
    }

    /**
     * 审核货品出库单
     *
     * @param idList 货品入库单编号
     */
    public void audit(List<Integer> idList) {
        checkBillState(idList, 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductOutstockBill productOutstockBill = new ProductOutstockBill();
        productOutstockBill.setBillState(2);
        productOutstockBill.setAuditBy(loginAdmin.getAdminId());
        productOutstockBill.setAuditAt(new Date());

        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillIdIn(idList);
        productOutstockBillDAO.updateByExampleSelective(productOutstockBill, productOutstockBillQuery);
        // 添加日志
        addLog(LogType.PRODUCT_OUTSTOCK, Operate.AUDIT, idList);
    }

    /**
     * 反审核货品出库单
     *
     * @param idList 货品入库单编号
     */
    public void unaudit(List<Integer> idList) {
        checkBillState(idList, 2);

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductOutstockBill productOutstockBill = new ProductOutstockBill();
        productOutstockBill.setBillState(1);
        productOutstockBill.setAuditBy(loginAdmin.getAdminId());
        productOutstockBill.setAuditAt(new Date());

        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillIdIn(idList);
        productOutstockBillDAO.updateByExampleSelective(productOutstockBill, productOutstockBillQuery);
        // 添加日志
        addLog(LogType.PRODUCT_OUTSTOCK, Operate.UNAUDIT, idList);
    }

    /**
     * 完成货品出库单
     * 若为发货出库，将相关订单转为已发货状态
     *
     * @param billId 单据编号
     */
    public void finish(Integer billId) {
        checkBillState(Collections.singletonList(billId), 2);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductOutstockBill productOutstockBill = new ProductOutstockBill();
        productOutstockBill.setBillId(billId);
        productOutstockBill.setBillState(3);
        productOutstockBill.setFinishBy(loginAdmin.getAdminId());
        productOutstockBill.setFinishAt(new Date());
        productOutstockBillDAO.updateByPrimaryKeySelective(productOutstockBill);

        // 若为发货出库，将相关订单转为已发货状态
        productOutstockBill = productOutstockBillDAO.selectByPrimaryKey(productOutstockBill.getBillId());
        if (productOutstockBill != null && productOutstockBill.getProductWhereabouts().equals(1)
                && !ParamUtils.isNull(productOutstockBill.getRelatedBill())) {
            OrderBill orderBill = new OrderBill();
            orderBill.setBillId(productOutstockBill.getRelatedBill());
            orderBill.setBillState(4);
            orderBill.setDeliveryBy(loginAdmin.getAdminId());
            orderBill.setDeliveryAt(new Date());
            orderBillDAO.updateByPrimaryKeySelective(orderBill);
        }

        // 添加日志
        addLog(LogType.MATERIAL_OUTSTOCK, Operate.FINISH, billId);
    }

    private void checkStockQuantity(List<ProductOutstockBillProduct> productList) {
        Map<Integer, Integer> productMap = new HashMap<Integer, Integer>();
        for (ProductOutstockBillProduct product : productList) {
            if (!productMap.containsKey(product.getProductId())) {
                productMap.put(product.getProductId(), 0);
            }
            productMap.put(product.getProductId(), productMap.get(product.getProductId()) + product.getQuantity());
        }

        for (Integer productId : productMap.keySet()) {
            ProductQuery productQuery = new ProductQuery();
            productQuery.or().andProductIdEqualTo(productId);
            List<ProductStockRecord> productStockRecordList = productDAO.selectProductStockByExample(productQuery);
            if (productStockRecordList.size() == 0) {
                throw new BadRequestException(STOCK_QUANTITY_NOT_ENOUGH);
            }
            if (productStockRecordList.get(0).getLeftAmount() < productMap.get(productId)) {
                throw new BadRequestException(STOCK_QUANTITY_NOT_ENOUGH + "，编号：" + productStockRecordList.get(0).getProductNo());
            }
        }
    }

    private void checkBillState(List<Integer> idList, int state) {
        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillIdIn(idList)
                .andBillStateNotEqualTo(state);
        if (productOutstockBillDAO.countByExample(productOutstockBillQuery) > 0) {
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
}

