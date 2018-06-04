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
import java.util.Date;
import java.util.List;

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
    private static final String PRODUCT_BILL_NOT_EXISTED="出库单不存在";
    private static final String PRODUCT_BILL_STATE_WRONG="出库单状态不符合要求";//只有待审核可修改和可删除
    private static final String PRODUCT_SOURCE_MODIFY_ERROR="退货出库的出库单无法修改";
    private static final String BILL_STATE_NOT_UNAUDIT = "单据不是待审核状态";
    private static final String BILL_STATE_NOT_AUDIT = "单据不是已审核状态";
    private static final String EXISE_OUTSTOCK_CANNOT_UNAUDIT = "单据已存在对应出库单，不能反审核";

    //增加入库单
    public ProductOutstockBill addProductOutsockBill(String bill_no, Integer from_principal, Integer warehouse_principal,
                                                     Integer product_source, Integer related_bill, Integer bill_state, String remark, List<ProductOutstockBillProduct> productIdList)
    {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ProductOutstockBill productOutstockBill = new ProductOutstockBill();
        productOutstockBill.setBillNo(bill_no);
        productOutstockBill.setToPrincipal(from_principal);
        productOutstockBill.setWarehousePrincipal(warehouse_principal);
        productOutstockBill.setBillTime(new Date());
        productOutstockBill.setProductWhereabouts(1);
        productOutstockBill.setRelatedBill(related_bill);
        productOutstockBill.setBillState(bill_state);
        productOutstockBill.setRemark(remark);
        productOutstockBill.setCreateAt(null);
        productOutstockBill.setCreateBy(null);
        productOutstockBill.setUpdateAt(null);
        productOutstockBill.setUpdateBy(null);
        productOutstockBill.setCreateAt(new java.util.Date());
        productOutstockBill.setCreateBy(loginAdmin.getAdminId());
        productOutstockBill.setUpdateAt(new java.util.Date());
        productOutstockBill.setUpdateBy(loginAdmin.getAdminId());
        productOutstockBillDAO.insertSelective(productOutstockBill);

        for (ProductOutstockBillProduct product: productIdList) {
            product.setBillId(productOutstockBill.getBillId());
            productOutstockBillProductDAO.insert(product);
        }
        addLog(LogType.PRODUCT_INSTOCK,Operate.ADD,productOutstockBill.getBillId());
        return getProductOutstockBillById(productOutstockBill.getBillId());
    }



    //修改
    public ProductOutstockBill updateProductOutsockBill(Integer bill_id,String bill_no, Integer from_principal, Integer warehouse_principal,
                                                        Integer product_source, Integer related_bill, Integer bill_state, String remark,  List<ProductOutstockBillProduct> productIdList)
    {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ProductOutstockBill productOutstockBill=new ProductOutstockBill();
        productOutstockBill.setBillId(bill_id);
        productOutstockBill.setBillNo(bill_no);
        productOutstockBill.setToPrincipal(from_principal);
        productOutstockBill.setWarehousePrincipal(warehouse_principal);
        productOutstockBill.setRelatedBill(related_bill);
        productOutstockBill.setBillState(bill_state);
        productOutstockBill.setRemark(remark);
        productOutstockBill.setUpdateAt(new Date());
        productOutstockBill.setUpdateBy(loginAdmin.getAdminId());
        productOutstockBillDAO.updateByPrimaryKeySelective(productOutstockBill);
        ProductOutstockBillProductQuery productOutstockBillProductQuery = new ProductOutstockBillProductQuery();
        productOutstockBillProductQuery.or().andBillIdEqualTo(productOutstockBill.getBillId());
        productOutstockBillProductDAO.deleteByExample(productOutstockBillProductQuery);
        for (ProductOutstockBillProduct product : productIdList){
            product.setBillId(productOutstockBill.getBillId());
            productOutstockBillProductDAO.insert(product);
        }
        addLog(LogType.PRODUCT_OUTSTOCK, Operate.UPDATE, productOutstockBill.getBillId());
        return getProductOutstockBillById(productOutstockBill.getBillId());
    }

    /*
     按照ID搜索产品出库单。
     */
    public ProductOutstockBill getProductOutstockBillById(int billId)
    {
        ProductOutstockBill productOutstockBill=productOutstockBillDAO.selectByPrimaryKey(billId);
        String toPrincipalName = adminDAO.selectByPrimaryKey(productOutstockBill.getToPrincipal()).getTrueName();
        productOutstockBill.setToPrincipalName(toPrincipalName);
        String warehousePrincipalName = adminDAO.selectByPrimaryKey(productOutstockBill.getWarehousePrincipal()).getTrueName();
        productOutstockBill.setWarehousePrincipalName(warehousePrincipalName);
        ProductOutstockBillProductQuery productOutstockBillProductQuery = new ProductOutstockBillProductQuery();
        ProductOutstockBillProductQuery.Criteria criteria = productOutstockBillProductQuery.or();
        criteria.andBillIdEqualTo(productOutstockBill.getBillId());
        List<ProductOutstockBillProduct> result = productOutstockBillProductDAO.selectByExample(productOutstockBillProductQuery);
        for(ProductOutstockBillProduct item: result) {
            Product product = productDAO.selectByPrimaryKey(item.getProductId());
            if (product != null) {
                item.setProductNo(product.getProductNo());
                item.setProductName(product.getProductName());
                String name = adminDAO.selectByPrimaryKey(item.getPrincipal()).getTrueName();
                item.setToPrincipalName(name);
                String warehouseName = warehouseDAO.selectByPrimaryKey(item.getWarehouse()).getWarehouseName();
                item.setWarehouseName(warehouseName);
            }
        }
        productOutstockBill.setProductIdList(result);
        return productOutstockBill;
    }



    //查询
    public PageMode<ProductOutstockBill> pageProductOutstockBill(Integer current, Integer limit, String sortColumn, String sort,
                                                                 String searchKey, Integer productOutstockState)
    {
        ProductOutstockBillQuery materialOutstockBillQuery=new ProductOutstockBillQuery();
        materialOutstockBillQuery.setOffset((current-1)*limit);
        materialOutstockBillQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            materialOutstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }
        //查询订单编号
        ProductOutstockBillQuery.Criteria criteria=materialOutstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andBillNoLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(productOutstockState) && !productOutstockState.equals(-1)) {
            criteria.andBillStateEqualTo(productOutstockState);
        }

        //查询备注（remark）
        criteria=materialOutstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andRemarkLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(productOutstockState) && !productOutstockState.equals(-1)) {
            criteria.andBillStateEqualTo(productOutstockState);
        }

        //返回warehouse
        List<ProductOutstockBill> result=productOutstockBillDAO.selectByExample(materialOutstockBillQuery);

        for(ProductOutstockBill bill: result) {
            String name = adminDAO.selectByPrimaryKey(bill.getToPrincipal()).getTrueName();
            bill.setToPrincipalName(name);
            String warehousePrincipalName = adminDAO.selectByPrimaryKey(bill.getWarehousePrincipal()).getTrueName();
            bill.setWarehousePrincipalName(warehousePrincipalName);
        }
        return new PageMode<ProductOutstockBill>(result,productOutstockBillDAO.countByExample(materialOutstockBillQuery));
    }


    //删除
    public void removeProductBill(List<Integer> billIdList)
    {
        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillStateEqualTo(1);
        /*if(productOutstockBillProductDAO.countByExample()<=0)
        {
            throw new BadRequestException(PRODUCT_BILL_STATE_WRONG);
        }*/
        //删除出库单
        productOutstockBillQuery=new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillIdIn(billIdList);
        productOutstockBillDAO.deleteByExample(productOutstockBillQuery);
        //删除关联
        ProductOutstockBillProductQuery productOutstockBillProductQuery =new ProductOutstockBillProductQuery();
        productOutstockBillProductQuery.or().andBillIdIn(billIdList);
        productOutstockBillProductDAO.deleteByExample(productOutstockBillProductQuery);
        addLog(LogType.PRODUCT_INSTOCK,Operate.REMOVE,billIdList);
    }

    /**
     * 获取所有可选的物料
     */
    public List<Product> getProductIdList() {
        return productDAO.selectByExample(new ProductQuery());
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
     * 审核
     *
     * @param idList
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
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.AUDIT, idList);
    }

    /**
     * 反审核领料单
     *
     * 检查领料单是否已经有对应出库单
     *
     * @param idList 领料单编号
     */
    public void unaudit(List<Integer> idList) {
        checkBillState(idList, 2);
    /*MaterialOutstockBillQuery materialOutstockBillQuery = new MaterialOutstockBillQuery();
    materialOutstockBillQuery.or().andRelatedBillIn(idList);
    if (materialOutstockBillDAO.countByExample(materialOutstockBillQuery) > 0) {
        throw new BadRequestException(EXISE_OUTSTOCK_CANNOT_UNAUDIT);
    }*/

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        ProductOutstockBill productOutstockBill = new ProductOutstockBill();
        productOutstockBill.setBillState(1);
        productOutstockBill.setAuditBy(loginAdmin.getAdminId());
        productOutstockBill.setAuditAt(new Date());

        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillIdIn(idList);
        productOutstockBillDAO.updateByExampleSelective(productOutstockBill, productOutstockBillQuery);
        // 添加日志
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.UNAUDIT, idList);
    }
}

