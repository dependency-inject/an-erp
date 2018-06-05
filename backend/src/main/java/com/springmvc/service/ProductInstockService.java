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

@Service("ProductInstockService")
@Transactional
public class ProductInstockService extends BaseService {

    @Resource
    private AdminDAO adminDAO;

    @Resource
    private WarehouseDAO warehouseDAO;

    @Resource
    private ProductDAO productDAO;

    @Resource
    private ProductInstockBillDAO productInstockBillDAO;

    @Resource
    private ProductInstockBillProductDAO productInstockBillProductDAO;

    private static final String PRODUCT_BILL_NOT_EXISTED="出库单不存在";
    private static final String PRODUCT_BILL_STATE_WRONG="出库单状态不符合要求";//只有待审核可修改和可删除
    private static final String PRODUCT_SOURCE_MODIFY_ERROR="退货出库的出库单无法修改";
    private static final String BILL_STATE_NOT_UNAUDIT = "单据不是待审核状态";
    private static final String BILL_STATE_NOT_AUDIT = "单据不是已审核状态";
    private static final String EXISE_OUTSTOCK_CANNOT_UNAUDIT = "单据已存在对应出库单，不能反审核";

    /**
     * 添加货品入库单
     *
     * 将主表信息保存：product_instock_bill
     * 将关联的从表信息保存：product_instock_bill_product
     * 添加日志信息：LogType.PRODUCT_INSTOCK, Operate.ADD
     *
     * @param fromPrincipal
     * @param productSource
     * @param remark
     * @param productList
     * @return
     */
    public ProductInstockBill addProductInsockBill(Integer fromPrincipal, Integer productSource,
                                                   String remark, List<ProductInstockBillProduct> productList)  {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductInstockBill productInstockBill = new ProductInstockBill();
        productInstockBill.setBillNo("PI" + ParamUtils.dateConvert(new Date(), "yyMMddHHmmssSSS"));
        productInstockBill.setFromPrincipal(fromPrincipal);
        productInstockBill.setWarehousePrincipal(loginAdmin.getAdminId());
        productInstockBill.setBillTime(new Date());
        productInstockBill.setProductSource(productSource);
        productInstockBill.setBillState(1);
        productInstockBill.setRemark(remark);
        productInstockBill.setCreateAt(new Date());
        productInstockBill.setCreateBy(loginAdmin.getAdminId());
        productInstockBill.setUpdateAt(new Date());
        productInstockBill.setUpdateBy(loginAdmin.getAdminId());
        productInstockBillDAO.insertSelective(productInstockBill);

        for (ProductInstockBillProduct product: productList) {
            product.setBillId(productInstockBill.getBillId());
            productInstockBillProductDAO.insert(product);
        }
        addLog(LogType.PRODUCT_INSTOCK, Operate.ADD, productInstockBill.getBillId());
        return getProductInstockBillById(productInstockBill.getBillId());
    }


    /**
     * 更新货品入库单
     *
     * 进行必要的检查：是否为待审核状态
     * 更新主表信息：product_instock_bill
     * 更新关联的从表信息：product_instock_bill_product
     * 添加日志信息：LogType.PRODUCT_INSTOCK, Operate.UPDATE
     *
     * @param billId
     * @param fromPrincipal
     * @param productSource
     * @param remark
     * @param productList
     * @return
     */
    public ProductInstockBill updateProductInsockBill(Integer billId, Integer fromPrincipal, Integer productSource,
                                                      String remark, List<ProductInstockBillProduct> productList) {
        checkBillState(Collections.singletonList(billId), 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductInstockBill productInstockBill = new ProductInstockBill();
        productInstockBill.setBillId(billId);
        productInstockBill.setFromPrincipal(fromPrincipal);
        productInstockBill.setProductSource(productSource);
        productInstockBill.setRemark(remark);
        productInstockBill.setUpdateAt(new Date());
        productInstockBill.setUpdateBy(loginAdmin.getAdminId());
        productInstockBillDAO.updateByPrimaryKeySelective(productInstockBill);

        ProductInstockBillProductQuery productInstockBillProductQuery = new ProductInstockBillProductQuery();
        productInstockBillProductQuery.or().andBillIdEqualTo(productInstockBill.getBillId());
        productInstockBillProductDAO.deleteByExample(productInstockBillProductQuery);
        for (ProductInstockBillProduct product : productList){
            product.setBillId(productInstockBill.getBillId());
            productInstockBillProductDAO.insert(product);
        }
        addLog(LogType.PRODUCT_INSTOCK, Operate.UPDATE, productInstockBill.getBillId());
        return getProductInstockBillById(productInstockBill.getBillId());
    }

    /**
     * 查询货品入库单信息（单个）
     *
     * @param billId 订单编号
     * @return
     */
    public ProductInstockBill getProductInstockBillById(int billId) {
        ProductInstockBill productInstockBill = productInstockBillDAO.selectByPrimaryKey(billId);

        String toPrincipalName = adminDAO.selectByPrimaryKey(productInstockBill.getFromPrincipal()).getTrueName();
        productInstockBill.setFromPrincipalName(toPrincipalName);
        String warehousePrincipalName = adminDAO.selectByPrimaryKey(productInstockBill.getWarehousePrincipal()).getTrueName();
        productInstockBill.setWarehousePrincipalName(warehousePrincipalName);

        Integer i = productInstockBill.getBillState();
        if (i > 1) {
            String auditName = adminDAO.selectByPrimaryKey(productInstockBill.getAuditBy()).getTrueName();
            productInstockBill.setAuditName(auditName);
        }
        if (i > 2) {
            String finishName = adminDAO.selectByPrimaryKey(productInstockBill.getFinishBy()).getTrueName();
            productInstockBill.setFinishName(finishName);
        }

        ProductInstockBillProductQuery productInstockBillProductQuery = new ProductInstockBillProductQuery();
        ProductInstockBillProductQuery.Criteria criteria = productInstockBillProductQuery.or();
        criteria.andBillIdEqualTo(productInstockBill.getBillId());
        List<ProductInstockBillProduct> result = productInstockBillProductDAO.selectByExample(productInstockBillProductQuery);
        for(ProductInstockBillProduct item: result) {
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
        productInstockBill.setProductList(result);
        return productInstockBill;
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
    public PageMode<ProductInstockBill> pageProductInstockBill(Integer current, Integer limit, String sortColumn, String sort,
                                                               String searchKey, Integer state, Date beginTime, Date endTime) {
        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
        productInstockBillQuery.setOffset((current-1)*limit);
        productInstockBillQuery.setLimit(limit);
        // 若未指定则默认按照时间排序
        if(ParamUtils.isNull(sortColumn)) {
            sortColumn = "billTime";
            sort = "desc";
        }
        productInstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        // 查询编号
        ProductInstockBillQuery.Criteria criteria = productInstockBillQuery.or();
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
        // 查询交货人
        List<Integer> adminIdList = searchAdminByTrueName(searchKey);
        criteria = productInstockBillQuery.or();
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
        criteria = productInstockBillQuery.or();
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

        List<ProductInstockBill> result = productInstockBillDAO.selectByExample(productInstockBillQuery);

        for(ProductInstockBill bill: result) {
            String fromPrincipalName = adminDAO.selectByPrimaryKey(bill.getFromPrincipal()).getTrueName();
            bill.setFromPrincipalName(fromPrincipalName);
            String warehousePrincipalName = adminDAO.selectByPrimaryKey(bill.getWarehousePrincipal()).getTrueName();
            bill.setWarehousePrincipalName(warehousePrincipalName);
        }
        return new PageMode<ProductInstockBill>(result, productInstockBillDAO.countByExample(productInstockBillQuery));
    }


    /**
     * 删除货品入库单
     *
     * 进行必要的检查：是否为待审核状态
     * 删除主表信息：product_instock_bill
     * 删除关联的从表信息：product_instock_bill_product
     * 添加日志信息：LogType.PRODUCT_INSTOCK, Operate.REMOVE
     *
     * @param billIdList
     */
    public void removeProductBill(List<Integer> billIdList) {
        checkBillState(billIdList, 1);

        //删除入库单
        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
        productInstockBillQuery.or().andBillIdIn(billIdList);
        productInstockBillDAO.deleteByExample(productInstockBillQuery);
        //删除关联
        ProductInstockBillProductQuery productInstockBillProductQuery = new ProductInstockBillProductQuery();
        productInstockBillProductQuery.or().andBillIdIn(billIdList);
        productInstockBillProductDAO.deleteByExample(productInstockBillProductQuery);
        // 添加日志
        addLog(LogType.PRODUCT_INSTOCK, Operate.REMOVE, billIdList);
    }

    /**
     * 审核货品入库单
     *
     * @param idList 货品入库单编号
     */
    public void audit(List<Integer> idList) {
        checkBillState(idList, 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductInstockBill productInstockBill = new ProductInstockBill();
        productInstockBill.setBillState(2);
        productInstockBill.setAuditBy(loginAdmin.getAdminId());
        productInstockBill.setAuditAt(new Date());

        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
        productInstockBillQuery.or().andBillIdIn(idList);
        productInstockBillDAO.updateByExampleSelective(productInstockBill, productInstockBillQuery);
        // 添加日志
        addLog(LogType.PRODUCT_INSTOCK, Operate.AUDIT, idList);
    }

    /**
     * 反审核货品入库单
     *
     * @param idList 货品入库单编号
     */
    public void unaudit(List<Integer> idList) {
        checkBillState(idList, 2);

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductInstockBill productInstockBill = new ProductInstockBill();
        productInstockBill.setBillState(1);
        productInstockBill.setAuditBy(loginAdmin.getAdminId());
        productInstockBill.setAuditAt(new Date());

        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
        productInstockBillQuery.or().andBillIdIn(idList);
        productInstockBillDAO.updateByExampleSelective(productInstockBill, productInstockBillQuery);
        // 添加日志
        addLog(LogType.PRODUCT_INSTOCK, Operate.UNAUDIT, idList);
    }

    private void checkBillState(List<Integer> idList, int state) {
        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
        productInstockBillQuery.or().andBillIdIn(idList)
                .andBillStateNotEqualTo(state);
        if (productInstockBillDAO.countByExample(productInstockBillQuery) > 0) {
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
