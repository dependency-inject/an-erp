package com.springmvc.service;

import com.springmvc.dao.AdminDAO;
import com.springmvc.dao.OrderBillDAO;
import com.springmvc.dao.OrderBillProductDAO;
import com.springmvc.dao.ProductDAO;
import com.springmvc.dto.*;
import com.springmvc.pojo.AdminQuery;
import com.springmvc.pojo.OrderBillProductQuery;
import com.springmvc.pojo.OrderBillQuery;
import com.springmvc.pojo.ProductQuery;
import com.springmvc.utils.ParamUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("OrderService")
@Transactional
public class OrderService extends BaseService {

    @Resource
    private OrderBillDAO orderBillDao;

    @Resource
    private AdminDAO adminDAO;

    @Resource
    private OrderBillProductDAO orderBillProductDAO;

    @Resource
    private ProductDAO productDAO;

    /**
     * 查询订单信息（单个）
     * @param billId 订单编号
     * @return
     */
    public OrderBill getOrderById(Integer billId) {
        OrderBill orderBill = orderBillDao.selectByPrimaryKey(billId);
        String name = adminDAO.selectByPrimaryKey(orderBill.getSalesman()).getTrueName();
        orderBill.setSalesName(name);
        Integer i = orderBill.getBillState();
        if(i < 5) {
            if(i > 1) {
                String auditName = adminDAO.selectByPrimaryKey(orderBill.getAuditBy()).getTrueName();
                orderBill.setAuditName(auditName);
            }
            if(i > 2) {
                String produceName = adminDAO.selectByPrimaryKey(orderBill.getProduceBy()).getTrueName();
                orderBill.setProduceName(produceName);
            }
            if(i > 3) {
                String deliverName = adminDAO.selectByPrimaryKey(orderBill.getDeliveryBy()).getTrueName();
                orderBill.setDeliverName(deliverName);
            }
        }
        return orderBill;
    }



    /**
     * 查询订单信息（分页）
     *
     * 将主表信息取出：（同时包含总记录数）
     * 搜索字段：编号、销售人、客户、金额、状态
     * 筛选字段：账号状态
     * 过滤不显示的信息：password
     *
     * 通过查找Admin获得销售人真实姓名
     *
     * @param current 当前位置
     * @param limit 一次读取长度
     * @param sortColumn 按哪一列排序
     * @param sort  排序方式 升序 降序
     * @param searchKey 关键字查找
     * @param state 订单状态
     * @return
     */
    public PageMode<OrderBill> pageOrder(Integer current, Integer limit, String sortColumn, String  sort, String searchKey, Integer state) {
        OrderBillQuery orderBillQuery = new OrderBillQuery();
        orderBillQuery.setOffset((current-1) * limit);
        orderBillQuery.setLimit(limit);
        //若未指定则默认按照时间排序
        if(ParamUtils.isNull(sortColumn)) {
            sortColumn = "billTime";
            sort = "desc";
        }
        orderBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        OrderBillQuery.Criteria criteria = orderBillQuery.or();
        Boolean checkState = !ParamUtils.isNull(state) && !state.equals(-1);
        if (!ParamUtils.isNull(searchKey)) {
            //编号查找
            criteria.andBillNoLike("%" + searchKey + "%");
            if (checkState) {
                criteria.andBillStateEqualTo(state);
            }
            //客户姓名
            criteria = orderBillQuery.or();
            criteria.andContactLike("%" + searchKey + "%");
            if (checkState) {
                criteria.andBillStateEqualTo(state);
            }


            //查找销售人姓名 先根据姓名查找销售人id list
            AdminQuery adminQuery = new AdminQuery();
            AdminQuery.Criteria criteria1 = adminQuery.or();
            criteria1.andTrueNameLike("%" + searchKey + "%");
            List<Admin> adminList = adminDAO.selectByExample(adminQuery);
            List<Integer> adminId = new ArrayList<Integer>();
            for (Admin admin: adminList) {
                adminId.add(admin.getAdminId());
            }
            if (!adminId.isEmpty()) {
                criteria = orderBillQuery.or();
                criteria.andSalesmanIn(adminId);
                if (checkState) {
                    criteria.andBillStateEqualTo(state);
                }
            }
        } else {
            if (checkState) {
                criteria.andBillStateEqualTo(state);
            }
        }

        List<OrderBill> result = orderBillDao.selectByExample(orderBillQuery);

        for(OrderBill order: result) {
            String name = adminDAO.selectByPrimaryKey(order.getSalesman()).getTrueName();
            order.setSalesName(name);
        }
        //找到所有符合条件的记录的个数 把之前的limit限制去掉
        orderBillQuery.setLimit(0);
        return new PageMode<OrderBill>(result, orderBillDao.countByExample(orderBillQuery));
    }

    /**
     * 获得订单中货品信息
     *
     * @param billId 订单编号
     * @return
     */
    public List<OrderBillProduct> getProduct(Integer billId) {
        OrderBillProductQuery orderBillProductQuery = new OrderBillProductQuery();
        OrderBillProductQuery.Criteria criteria = orderBillProductQuery.or();
        criteria.andBillIdEqualTo(billId);
        List<OrderBillProduct> result = orderBillProductDAO.selectByExample(orderBillProductQuery);
        for (OrderBillProduct item: result) {
            String name = productDAO.selectByPrimaryKey(item.getProductId()).getProductName();
            item.setProductName(name);
        }
        return result;
    }

    /**
     * 审核订单
     * @param adminId 审核人编号
     * @param billId 订单编号
     */
    public String shenhe(Integer adminId, Integer billId) {
        OrderBill orderBill = orderBillDao.selectByPrimaryKey(billId);
        orderBill.setBillState(2);
        orderBill.setAuditBy(adminId);
        orderBill.setAuditAt(new Date());
        orderBill.setUpdateAt(new Date());
        orderBill.setUpdateBy(adminId);
        orderBillDao.updateByPrimaryKey(orderBill);
        return "success";
    }

    /**
     * 反审核订单
     * @param adminId 操作人
     * @param billId 订单编号
     */
    public String fanshenhe(Integer adminId, Integer billId) {
        OrderBill orderBill = orderBillDao.selectByPrimaryKey(billId);
        orderBill.setAuditAt(null);
        orderBill.setAuditBy(null);
        orderBill.setBillState(1);
        orderBill.setUpdateAt(new Date());
        orderBill.setUpdateBy(adminId);
        orderBillDao.updateByPrimaryKey(orderBill);
        return "success";
    }

    /**
     * 状态更改为生产中
     * @param adminId 操作人
     * @param billId 订单编号
     */
    public String produce(Integer adminId, Integer billId) {
        OrderBill orderBill = orderBillDao.selectByPrimaryKey(billId);
        orderBill.setBillState(3);
        orderBill.setProduceAt(new Date());
        orderBill.setProduceBy(adminId);
        orderBill.setUpdateBy(adminId);
        orderBill.setUpdateAt(new Date());
        orderBillDao.updateByPrimaryKey(orderBill);
        return "success";
    }


    /**
     * 状态改为已发货
     * @param adminId 操作人
     * @param billId 订单编号
     */
    public String deliver(Integer adminId, Integer billId) {
        OrderBill orderBill = orderBillDao.selectByPrimaryKey(billId);
        orderBill.setBillState(4);
        orderBill.setDeliveryAt(new Date());
        orderBill.setDeliveryBy(adminId);
        orderBill.setUpdateAt(new Date());
        orderBill.setUpdateBy(adminId);
        orderBillDao.updateByPrimaryKey(orderBill);
        return "success";
    }

    /**
     * 状态改为已取消
     * @param adminId 操作人
     * @param billId 订单编号
     */
    public String cancel(Integer adminId, Integer billId) {
        OrderBill orderBill = orderBillDao.selectByPrimaryKey(billId);
        orderBill.setBillState(5);
        orderBill.setUpdateBy(adminId);
        orderBill.setUpdateAt(new Date());
        orderBillDao.updateByPrimaryKey(orderBill);
        return "success";
    }

    /**
     * 获取所有可选的商品
     */
    public List<Product> getProducts() {
        ProductQuery productQuery = new ProductQuery();
        List<Product> result = productDAO.selectByExample(productQuery);
        return result;
    }


    /**
     * 添加订单
     * @param adminId 操作人
     * @param contact 联系人
     * @param contactPhone 联系电话
     * @param billAmount 金额
     * @param remark 备注
     * @param products 商品信息
     * @return 返回该订单编号
     */
    public Integer add(Integer adminId, String contact, String contactPhone, BigDecimal billAmount, String remark, String products) {
        OrderBill orderBill = new OrderBill();
        String no = UUID.randomUUID().toString();
        orderBill.setBillNo(no);
        orderBill.setBillState(1);
        orderBill.setSalesman(adminId);
        orderBill.setCreateAt(new Date());
        orderBill.setCreateBy(adminId);
        orderBill.setBillTime(new Date());
        orderBill.setContact(contact);
        orderBill.setContactPhone(contactPhone);
        orderBill.setBillAmount(billAmount);
        if (!ParamUtils.isNull(remark)) {
            orderBill.setRemark(remark);
        }
        orderBillDao.insert(orderBill);
        Integer billId = orderBill.getBillId();
        List<OrderBillProduct> orderBillProducts = ParamUtils.jsonToList(products, OrderBillProduct.class);
        for (OrderBillProduct orderBillProduct: orderBillProducts) {
            orderBillProduct.setBillId(billId);
            orderBillProductDAO.insert(orderBillProduct);
        }
        return billId;
    }


    /**
     * 删除订单
     * @param billId  订单编号
     * @return
     */
    public String remove(Integer billId) {
        orderBillDao.deleteByPrimaryKey(billId);
        OrderBillProductQuery orderBillProductQuery = new OrderBillProductQuery();
        OrderBillProductQuery.Criteria criteria = orderBillProductQuery.or();
        criteria.andBillIdEqualTo(billId);
        orderBillProductDAO.deleteByExample(orderBillProductQuery);
        return "success";
    }
}
