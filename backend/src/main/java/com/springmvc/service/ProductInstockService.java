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

    //增加入库单
    public ProductInstockBill addProductInsockBill(String bill_no, Integer from_principal, Integer warehouse_principal,
                                                   Integer product_source, Integer related_bill, Integer bill_state, String remark, List<ProductInstockBillProduct> productIdList)
    {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ProductInstockBill productInstockBill = new ProductInstockBill();
        productInstockBill.setBillNo(bill_no);
        productInstockBill.setFromPrincipal(from_principal);
        productInstockBill.setWarehousePrincipal(warehouse_principal);
        productInstockBill.setBillTime(new Date());
        productInstockBill.setProductSource(product_source);
        productInstockBill.setRelatedBill(related_bill);
        productInstockBill.setBillState(bill_state);
        productInstockBill.setRemark(remark);
        productInstockBill.setCreateAt(null);
        productInstockBill.setCreateBy(null);
        productInstockBill.setUpdateAt(null);
        productInstockBill.setUpdateBy(null);
        productInstockBill.setCreateAt(new java.util.Date());
        productInstockBill.setCreateBy(loginAdmin.getAdminId());
        productInstockBill.setUpdateAt(new java.util.Date());
        productInstockBill.setUpdateBy(loginAdmin.getAdminId());
        productInstockBillDAO.insertSelective(productInstockBill);

        for (ProductInstockBillProduct product: productIdList) {
            product.setBillId(productInstockBill.getBillId());
            productInstockBillProductDAO.insert(product);
        }
        addLog(LogType.PRODUCT_INSTOCK,Operate.ADD,productInstockBill.getBillId());
        return getProductInstockBillById(productInstockBill.getBillId());
    }



    //修改
    public ProductInstockBill updateProductInsockBill(Integer bill_id,String bill_no, Integer from_principal, Integer warehouse_principal,
                                                      Integer product_source, Integer related_bill, Integer bill_state, String remark,  List<ProductInstockBillProduct> productIdList)
    {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ProductInstockBill productInstockBill=new ProductInstockBill();
        productInstockBill.setBillId(bill_id);
        productInstockBill.setBillNo(bill_no);
        productInstockBill.setFromPrincipal(from_principal);
        productInstockBill.setWarehousePrincipal(warehouse_principal);
        productInstockBill.setRelatedBill(related_bill);
        productInstockBill.setBillState(bill_state);
        productInstockBill.setRemark(remark);
        productInstockBill.setUpdateAt(new Date());
        productInstockBill.setUpdateBy(loginAdmin.getAdminId());
        productInstockBillDAO.updateByPrimaryKeySelective(productInstockBill);
        ProductInstockBillProductQuery productInstockBillProductQuery = new ProductInstockBillProductQuery();
        productInstockBillProductQuery.or().andBillIdEqualTo(productInstockBill.getBillId());
        productInstockBillProductDAO.deleteByExample(productInstockBillProductQuery);
        for (ProductInstockBillProduct product : productIdList){
            product.setBillId(productInstockBill.getBillId());
            productInstockBillProductDAO.insert(product);
        }
        addLog(LogType.PRODUCT_OUTSTOCK, Operate.UPDATE, productInstockBill.getBillId());
        return getProductInstockBillById(productInstockBill.getBillId());
    }

    /*
     按照ID搜索产品入库单。
     */
    public ProductInstockBill getProductInstockBillById(int billId)
    {
        ProductInstockBill productInstockBill=productInstockBillDAO.selectByPrimaryKey(billId);
        String toPrincipalName = adminDAO.selectByPrimaryKey(productInstockBill.getFromPrincipal()).getTrueName();
        productInstockBill.setFromPrincipalName(toPrincipalName);
        String warehousePrincipalName = adminDAO.selectByPrimaryKey(productInstockBill.getWarehousePrincipal()).getTrueName();
        productInstockBill.setWarehousePrincipalName(warehousePrincipalName);
        ProductInstockBillProductQuery productInstockBillProductQuery = new ProductInstockBillProductQuery();
        ProductInstockBillProductQuery.Criteria criteria = productInstockBillProductQuery.or();
        criteria.andBillIdEqualTo(productInstockBill.getBillId());
        List<ProductInstockBillProduct> result = productInstockBillProductDAO.selectByExample(productInstockBillProductQuery);
        for(ProductInstockBillProduct item: result) {
            Product product = productDAO.selectByPrimaryKey(item.getProductId());
            if (product != null){
                item.setProductNo(product.getProductNo());
                item.setProductName(product.getProductName());
                String name = adminDAO.selectByPrimaryKey(item.getPrincipal()).getTrueName();
                item.setFromPrincipalName(name);
                String warehouseName = warehouseDAO.selectByPrimaryKey(item.getWarehouse()).getWarehouseName();
                item.setWarehouseName(warehouseName);
            }
        }
        productInstockBill.setProductIdList(result);
        System.out.println(productInstockBill.getProductIdList());
        return productInstockBill;
    }



    //查询
    public PageMode<ProductInstockBill> pageProductInstockBill(Integer current, Integer limit, String sortColumn, String sort,
                                                               String searchKey, Integer productInstockState)
    {
        ProductInstockBillQuery materialInstockBillQuery=new ProductInstockBillQuery();
        materialInstockBillQuery.setOffset((current-1)*limit);
        materialInstockBillQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            materialInstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }
        //查询订单编号
        ProductInstockBillQuery.Criteria criteria=materialInstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andBillNoLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(productInstockState) && !productInstockState.equals(-1)) {
            criteria.andBillStateEqualTo(productInstockState);
        }

        //查询备注（remark）
        criteria=materialInstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andRemarkLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(productInstockState) && !productInstockState.equals(-1)) {
            criteria.andBillStateEqualTo(productInstockState);
        }

        //返回warehouse
        List<ProductInstockBill> result=productInstockBillDAO.selectByExample(materialInstockBillQuery);

        for(ProductInstockBill bill: result) {
            String name = adminDAO.selectByPrimaryKey(bill.getFromPrincipal()).getTrueName();
            bill.setFromPrincipalName(name);
            String warehousePrincipalName = adminDAO.selectByPrimaryKey(bill.getWarehousePrincipal()).getTrueName();
            bill.setWarehousePrincipalName(warehousePrincipalName);
        }
        return new PageMode<ProductInstockBill>(result,productInstockBillDAO.countByExample(materialInstockBillQuery));
    }


    //删除
    public void removeProductBill(List<Integer> billIdList)
    {
        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
        productInstockBillQuery.or().andBillStateEqualTo(1);
    /*if(productInstockBillProductDAO.countByExample()<=0)
    {
        throw new BadRequestException(PRODUCT_BILL_STATE_WRONG);
    }*/
        //删除入库单
        productInstockBillQuery=new ProductInstockBillQuery();
        productInstockBillQuery.or().andBillIdIn(billIdList);
        productInstockBillDAO.deleteByExample(productInstockBillQuery);
        //删除关联
        ProductInstockBillProductQuery productInstockBillProductQuery =new ProductInstockBillProductQuery();
        productInstockBillProductQuery.or().andBillIdIn(billIdList);
        productInstockBillProductDAO.deleteByExample(productInstockBillProductQuery);
        addLog(LogType.PRODUCT_INSTOCK,Operate.REMOVE,billIdList);
    }


    public List<Product> getProductIdList() {
        return productDAO.selectByExample(new ProductQuery());
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


    public List<Warehouse> getWarehouses() {
        WarehouseQuery warehouseQuery = new WarehouseQuery();
        List<Warehouse> result = warehouseDAO.selectByExample(warehouseQuery);
        return result;
    }

    public List<Admin> getAdmins() {
        AdminQuery adminQuery = new AdminQuery();
        List<Admin> result = adminDAO.selectByExample(adminQuery);
        return result;
    }

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
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.AUDIT, idList);
    }

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
        addLog(LogType.DRAW_MATERIAL_BILL, Operate.UNAUDIT, idList);
    }


}
