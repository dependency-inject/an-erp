package com.springmvc.service;

import com.springmvc.dao.ProductDAO;
import com.springmvc.dao.ProductInstockBillDAO;
import com.springmvc.dao.ProductInstockBillProductDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.ProductInstockBill;
import com.springmvc.dto.ProductInstockBillProduct;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.ProductInstockBillProductQuery;
import com.springmvc.pojo.ProductInstockBillQuery;
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
    private ProductDAO productDAO;

    @Resource
    private ProductInstockBillDAO productInstockBillDAO;

    @Resource
    private ProductInstockBillProductDAO productInstockBillProductDAO;
    private static final String PRODUCT_BILL_NOT_EXISTED="入库单不存在";
    private static final String PRODUCT_BILL_STATE_WRONG="入库单状态不符合要求";//只有待审核可修改和可删除
    private static final String PRODUCT_SOURCE_MODIFY_ERROR="退货入库的入库单无法修改";

    //增加入库单
    public ProductInstockBill addProductInsockBill(String bill_no, Integer from_principal, Integer warehouse_principal,
                                                   Integer product_source, Integer related_bill, Integer bill_state, String remark, String productIdList)
    {
        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
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
        addLog(LogType.PRODUCT_INSTOCK,Operate.ADD,productInstockBill.getBillId());
        return getProductInstockBillById(productInstockBill.getBillId());
    }



    //修改
    public ProductInstockBill updateProductInsockBill(Integer bill_id,String bill_no, Integer from_principal, Integer warehouse_principal,
                                                  Integer product_source, Integer related_bill, Integer bill_state, String remark, List<Integer> productIdList)
    {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        ProductInstockBillQuery productInstockBillQuery=new ProductInstockBillQuery();
        productInstockBillQuery.or().andBillStateEqualTo(1);
        if(productInstockBillDAO.countByExample(productInstockBillQuery)<=0)
        {
            throw new BadRequestException(PRODUCT_BILL_STATE_WRONG);
        }
        productInstockBillQuery=new ProductInstockBillQuery();
        productInstockBillQuery.or().andProductSourceEqualTo(1);
        if(productInstockBillDAO.countByExample(productInstockBillQuery)>=1)
        {
            throw new BadRequestException(PRODUCT_SOURCE_MODIFY_ERROR);
        }
        ProductInstockBill productInstockBill=new ProductInstockBill();
        productInstockBill.setBillId(bill_id);
        productInstockBill.setBillNo(bill_no);
        productInstockBill.setFromPrincipal(from_principal);
        productInstockBill.setWarehousePrincipal(warehouse_principal);
        productInstockBill.setRelatedBill(related_bill);
        productInstockBill.setBillState(bill_state);
        productInstockBill.setRemark(remark);
        if(bill_state==1)
        {
            productInstockBill.setAuditAt(null);
            productInstockBill.setAuditBy(null);
            productInstockBill.setFinishAt(null);
            productInstockBill.setFinishBy(null);
        }
        else if(bill_state==2)
        {
            productInstockBill.setAuditAt(new java.util.Date());
            productInstockBill.setAuditBy(loginAdmin.getAdminId());
            productInstockBill.setFinishAt(null);
            productInstockBill.setFinishBy(null);
        }
        else {
                productInstockBill.setFinishAt(new java.util.Date());
                productInstockBill.setFinishBy(loginAdmin.getAdminId());
        }
        productInstockBill.setUpdateAt(new java.util.Date());
        productInstockBill.setUpdateBy(loginAdmin.getAdminId());
        ProductInstockBillProduct productInstockBillProduct= new ProductInstockBillProduct();
        ProductInstockBillProductQuery productInstockBillProductQuery=new ProductInstockBillProductQuery();
        productInstockBillProductQuery.or().andBillIdEqualTo(productInstockBill.getBillId());
        productInstockBillProductDAO.deleteByExample(productInstockBillProductQuery);
        for(Integer productId : productIdList)
        {
            productInstockBillProduct=new ProductInstockBillProduct();
            productInstockBillProduct.setBillId(productInstockBill.getBillId());
            productInstockBillProduct.setProductId(productId);
            productInstockBillProductDAO.insertSelective(productInstockBillProduct);
        }
        addLog(LogType.PRODUCT_INSTOCK,Operate.UPDATE,productInstockBill.getBillId());
        return getProductInstockBillById(productInstockBill.getBillId());
    }

    /*
     按照ID搜索产品入库单。
     */
    public ProductInstockBill getProductInstockBillById(int billId)
    {
        ProductInstockBill productInstockBill=productInstockBillDAO.selectByPrimaryKey(billId);
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
        return new PageMode<ProductInstockBill>(result,productInstockBillDAO.countByExample(materialInstockBillQuery));
    }


    //删除
    public void removeProductBill(List<Integer> billIdList)
    {
        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
        productInstockBillQuery.or().andBillStateEqualTo(1);
        /*if(productInstockBillProductDAO.countByExample(productInstockBillQuery)<=0)
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
}
