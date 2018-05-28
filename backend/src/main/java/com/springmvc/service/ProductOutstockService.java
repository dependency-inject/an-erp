package com.springmvc.service;

import com.springmvc.dao.*;
import com.springmvc.dto.Admin;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.ProductOutstockBill;
import com.springmvc.dto.ProductOutstockBillProduct;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.ProductOutstockBillProductQuery;
import com.springmvc.pojo.ProductOutstockBillQuery;
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
    private ProductDAO productDAO;

    @Resource
    private ProductOutstockBillDAO productOutstockBillDAO;

    @Resource
    private ProductOutstockBillProductDAO productOutstockBillProductDAO;
    private static final String PRODUCT_BILL_NOT_EXISTED="入库单不存在";
    private static final String PRODUCT_BILL_STATE_WRONG="入库单状态不符合要求";//只有待审核可修改和可删除
    private static final String PRODUCT_SOURCE_MODIFY_ERROR="退货入库的入库单无法修改";

    //增加入库单
    public ProductOutstockBill addProductOutsockBill(String bill_no, Integer from_principal, Integer warehouse_principal,
                                                     Integer product_source, Integer related_bill, Integer bill_state, String remark, String productIdList)
    {
        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
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
        addLog(LogType.PRODUCT_INSTOCK,Operate.ADD,productOutstockBill.getBillId());
        return getProductOutstockBillById(productOutstockBill.getBillId());
    }



    //修改
    public ProductOutstockBill updateProductOutsockBill(Integer bill_id,String bill_no, Integer from_principal, Integer warehouse_principal,
                                                        Integer product_source, Integer related_bill, Integer bill_state, String remark, List<Integer> productIdList)
    {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ProductOutstockBillQuery productOutstockBillQuery=new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillStateEqualTo(1);
        if(productOutstockBillDAO.countByExample(productOutstockBillQuery)<=0)
        {
            throw new BadRequestException(PRODUCT_BILL_STATE_WRONG);
        }
        productOutstockBillQuery=new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andProductWhereaboutsEqualTo(1);
        if(productOutstockBillDAO.countByExample(productOutstockBillQuery)>=1)
        {
            throw new BadRequestException(PRODUCT_SOURCE_MODIFY_ERROR);
        }
        ProductOutstockBill productOutstockBill=new ProductOutstockBill();
        productOutstockBill.setBillId(bill_id);
        productOutstockBill.setBillNo(bill_no);
        productOutstockBill.setToPrincipal(from_principal);
        productOutstockBill.setWarehousePrincipal(warehouse_principal);
        productOutstockBill.setRelatedBill(related_bill);
        productOutstockBill.setBillState(bill_state);
        productOutstockBill.setRemark(remark);
        if(bill_state==1)
        {
            productOutstockBill.setAuditAt(null);
            productOutstockBill.setAuditBy(null);
            productOutstockBill.setFinishAt(null);
            productOutstockBill.setFinishBy(null);
        }
        else if(bill_state==2)
        {
            productOutstockBill.setAuditAt(new java.util.Date());
            productOutstockBill.setAuditBy(loginAdmin.getAdminId());
            productOutstockBill.setFinishAt(null);
            productOutstockBill.setFinishBy(null);
        }
        else {
            productOutstockBill.setFinishAt(new java.util.Date());
            productOutstockBill.setFinishBy(loginAdmin.getAdminId());
        }
        productOutstockBill.setUpdateAt(new java.util.Date());
        productOutstockBill.setUpdateBy(loginAdmin.getAdminId());
        ProductOutstockBillProduct productOutstockBillProduct= new ProductOutstockBillProduct();
        ProductOutstockBillProductQuery productOutstockBillProductQuery=new ProductOutstockBillProductQuery();
        productOutstockBillProductQuery.or().andBillIdEqualTo(productOutstockBill.getBillId());
        productOutstockBillProductDAO.deleteByExample(productOutstockBillProductQuery);
        for(Integer productId : productIdList)
        {
            productOutstockBillProduct=new ProductOutstockBillProduct();
            productOutstockBillProduct.setBillId(productOutstockBill.getBillId());
            productOutstockBillProduct.setProductId(productId);
            productOutstockBillProductDAO.insertSelective(productOutstockBillProduct);
        }
        addLog(LogType.PRODUCT_INSTOCK,Operate.UPDATE,productOutstockBill.getBillId());
        return getProductOutstockBillById(productOutstockBill.getBillId());
    }

    /*
     按照ID搜索产品入库单。
     */
    public ProductOutstockBill getProductOutstockBillById(int billId)
    {
        ProductOutstockBill productOutstockBill=productOutstockBillDAO.selectByPrimaryKey(billId);
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
        //删除入库单
        productOutstockBillQuery=new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andBillIdIn(billIdList);
        productOutstockBillDAO.deleteByExample(productOutstockBillQuery);
        //删除关联
        ProductOutstockBillProductQuery productOutstockBillProductQuery =new ProductOutstockBillProductQuery();
        productOutstockBillProductQuery.or().andBillIdIn(billIdList);
        productOutstockBillProductDAO.deleteByExample(productOutstockBillProductQuery);
        addLog(LogType.PRODUCT_INSTOCK,Operate.REMOVE,billIdList);
    }
}

