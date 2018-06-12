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
import java.math.BigDecimal;
import java.util.*;

@Service("OrderService")
@Transactional
public class OrderService extends BaseService {

    @Resource
    private OrderBillDAO orderBillDAO;

    @Resource
    private OrderBillProductDAO orderBillProductDAO;

    @Resource
    private ProductDAO productDAO;

    @Resource
    private AdminDAO adminDAO;

    @Resource
    private ClientDAO clientDAO;

    @Resource
    private ProductMaterialDAO productMaterialDAO;

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private ProductOutstockBillDAO productOutstockBillDAO;

    @Resource
    private DrawMaterialBillDAO drawMaterialBillDAO;

    /**
     * 获取订单表的所有信息
     *
     * @return 返回列表
     */
    public List<OrderBill> getList(Integer state, Boolean onlyNotOutstock, Boolean onlyNotDraw) {
        OrderBillQuery orderBillQuery = new OrderBillQuery();
        OrderBillQuery.Criteria criteria = orderBillQuery.or();
        if (!ParamUtils.isNull(state) && !state.equals(-1)) {
            criteria.andBillStateEqualTo(state);
        }
        if (!ParamUtils.isNull(onlyNotOutstock) && onlyNotOutstock) {
            Set<Integer> billIdSet = new HashSet<Integer>();
            List<ProductOutstockBill> billList = productOutstockBillDAO.selectByExample(new ProductOutstockBillQuery());
            for (ProductOutstockBill bill: billList) {
                if (bill.getProductWhereabouts().equals(1) && !ParamUtils.isNull(bill.getRelatedBill())) {
                    billIdSet.add(bill.getRelatedBill());
                }
            }
            if (billIdSet.size() > 0) {
                criteria.andBillIdNotIn(new ArrayList<Integer>(billIdSet));
            }
        }
        if (!ParamUtils.isNull(onlyNotDraw) && onlyNotDraw) {
            Set<Integer> billIdSet = new HashSet<Integer>();
            List<DrawMaterialBill> billList = drawMaterialBillDAO.selectByExample(new DrawMaterialBillQuery());
            for (DrawMaterialBill bill: billList) {
                if (bill.getDrawReason().equals(1) && !ParamUtils.isNull(bill.getRelatedBill())) {
                    billIdSet.add(bill.getRelatedBill());
                }
            }
            if (billIdSet.size() > 0) {
                criteria.andBillIdNotIn(new ArrayList<Integer>(billIdSet));
            }
        }
        return orderBillDAO.selectByExample(orderBillQuery);
    }

    /**
     * 获取统计信息
     *
     * @return
     */
    public StatisticsMode getStatistics() {
        List<StatisticsData> preWeek = new ArrayList<StatisticsData>();
        for (int i = -6; i <= 0; ++i) {
            OrderBillQuery orderBillQuery = new OrderBillQuery();
            Date beginTime = ParamUtils.getCertainDate(i);
            Date endTime = ParamUtils.getCertainDate(i + 1);
            orderBillQuery.or()
                    .andBillTimeGreaterThanOrEqualTo(beginTime)
                    .andBillTimeLessThan(endTime);
            preWeek.add(new StatisticsData(ParamUtils.dateConvert(beginTime, "yyyy-MM-dd"),
                    orderBillDAO.countByExample(orderBillQuery)));
        }

        List<StatisticsData> history = new ArrayList<StatisticsData>();
        for (int i = 1; i <= 5; ++i) {
            OrderBillQuery orderBillQuery = new OrderBillQuery();
            orderBillQuery.or().andBillStateEqualTo(i);
            history.add(new StatisticsData("" + i, orderBillDAO.countByExample(orderBillQuery)));
        }

        return new StatisticsMode(preWeek, history);
    }

    /**
     * 查询订单信息（单个）
     * @param billId 订单编号
     * @return
     */
    public OrderBill getOrderById(Integer billId) {
        OrderBill orderBill = orderBillDAO.selectByPrimaryKey(billId);
        String salesName = adminDAO.selectByPrimaryKey(orderBill.getSalesman()).getTrueName();
        orderBill.setSalesName(salesName);
        String clientName = clientDAO.selectByPrimaryKey(orderBill.getClientId()).getClientName();
        orderBill.setClientName(clientName);

        Integer i = orderBill.getBillState();
        if(i < 5) {
            if (i > 1) {
                String auditName = adminDAO.selectByPrimaryKey(orderBill.getAuditBy()).getTrueName();
                orderBill.setAuditName(auditName);
            }
            if (i > 2) {
                String produceName = adminDAO.selectByPrimaryKey(orderBill.getProduceBy()).getTrueName();
                orderBill.setProduceName(produceName);
            }
            if (i > 3) {
                String deliverName = adminDAO.selectByPrimaryKey(orderBill.getDeliveryBy()).getTrueName();
                orderBill.setDeliveryName(deliverName);
            }
        }

        OrderBillProductQuery orderBillProductQuery = new OrderBillProductQuery();
        OrderBillProductQuery.Criteria criteria = orderBillProductQuery.or();
        criteria.andBillIdEqualTo(orderBill.getBillId());
        List<OrderBillProduct> result = orderBillProductDAO.selectByExample(orderBillProductQuery);
        for (OrderBillProduct item: result) {
            Product product = productDAO.selectByPrimaryKey(item.getProductId());
            if (product != null) {
                item.setProductNo(product.getProductNo());
                item.setProductName(product.getProductName());
            }
        }
        orderBill.setProductList(result);
        return orderBill;
    }


    /**
     * 查询订单信息（分页）
     *
     * 将主表信息取出：（同时包含总记录数）
     * 搜索字段：编号、销售人员、客户姓名
     * 筛选字段：订单状态
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
    public PageMode<OrderBill> pageOrder(Integer current, Integer limit, String sortColumn, String  sort,
                                         String searchKey, Integer state, Date beginTime, Date endTime) {
        OrderBillQuery orderBillQuery = new OrderBillQuery();
        orderBillQuery.setOffset((current-1) * limit);
        orderBillQuery.setLimit(limit);
        // 若未指定则默认按照时间排序
        if(ParamUtils.isNull(sortColumn)) {
            sortColumn = "billTime";
            sort = "desc";
        }
        orderBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);

        // 搜索编号
        OrderBillQuery.Criteria criteria = orderBillQuery.or();
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
        // 搜索销售人员
        criteria = orderBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            List<Integer> adminIdList = searchAdminByTrueName(searchKey);
            if (adminIdList.size() == 0) {
                criteria.andSalesmanEqualTo(0);
            } else {
                criteria.andSalesmanIn(adminIdList);
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
        // 搜索客户姓名
        criteria = orderBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            List<Integer> clientIdList = searchClientByClientName(searchKey);
            if (clientIdList.size() == 0) {
                criteria.andClientIdEqualTo(0);
            } else {
                criteria.andClientIdIn(clientIdList);
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

        List<OrderBill> result = orderBillDAO.selectByExample(orderBillQuery);

        for(OrderBill order: result) {
            String salesName = adminDAO.selectByPrimaryKey(order.getSalesman()).getTrueName();
            order.setSalesName(salesName);
            String clientName = clientDAO.selectByPrimaryKey(order.getClientId()).getClientName();
            order.setClientName(clientName);
        }
        return new PageMode<OrderBill>(result, orderBillDAO.countByExample(orderBillQuery));
    }

    /**
     * 审核订单
     *
     * @param idList 订单编号
     */
    public void audit(List<Integer> idList) {
        checkBillState(idList, 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        OrderBill orderBill = new OrderBill();
        orderBill.setBillState(2);
        orderBill.setAuditBy(loginAdmin.getAdminId());
        orderBill.setAuditAt(new Date());

        OrderBillQuery orderBillQuery = new OrderBillQuery();
        orderBillQuery.or().andBillIdIn(idList);
        orderBillDAO.updateByExampleSelective(orderBill, orderBillQuery);
        // 添加日志
        addLog(LogType.ORDER, Operate.AUDIT, idList);
    }

    /**
     * 反审核订单
     *
     * @param idList 订单编号
     */
    public void unaudit(List<Integer> idList) {
        checkBillState(idList, 2);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        OrderBill orderBill = new OrderBill();
        orderBill.setBillState(1);
        orderBill.setAuditBy(loginAdmin.getAdminId());
        orderBill.setAuditAt(new Date());

        OrderBillQuery orderBillQuery = new OrderBillQuery();
        orderBillQuery.or().andBillIdIn(idList);
        orderBillDAO.updateByExampleSelective(orderBill, orderBillQuery);
        // 添加日志
        addLog(LogType.ORDER, Operate.UNAUDIT, idList);
    }

    /**
     * 状态更改为生产中
     *
     * @param billId 订单编号
     */
    public void produce(Integer billId) {
        checkBillState(Collections.singletonList(billId), 2);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        OrderBill orderBill = new OrderBill();
        orderBill.setBillId(billId);
        orderBill.setBillState(3);
        orderBill.setProduceAt(new Date());
        orderBill.setProduceBy(loginAdmin.getAdminId());
        orderBillDAO.updateByPrimaryKeySelective(orderBill);
        // 添加日志
        addLog(LogType.ORDER, Operate.PRODUCE, orderBill.getBillId());
    }

    /**
     * 状态改为已取消
     *
     * @param billId 订单编号
     */
    public void cancel(Integer billId) {
        checkBillState(Collections.singletonList(billId), 3);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        OrderBill orderBill = new OrderBill();
        orderBill.setBillId(billId);
        orderBill.setBillState(5);
        // TODO: 增加取消操作的日志信息
        orderBillDAO.updateByPrimaryKeySelective(orderBill);
        // 添加日志
        addLog(LogType.ORDER, Operate.CANCEL, orderBill.getBillId());
    }


    /**
     * 添加订单
     *
     * 将主表信息保存：order_bill
     * 将关联的从表信息保存：order_bill_product
     * 添加日志信息：LogType.ORDER_BILL, Operate.ADD
     *
     * @param clientId 客户
     * @param contact 联系人
     * @param contactPhone 联系电话
     * @param billAmount 金额
     * @param remark 备注
     * @param productList 商品信息
     * @return 返回该订单编号
     */
    public OrderBill add(Integer clientId, String contact, String contactPhone,
                         BigDecimal billAmount, String remark, List<OrderBillProduct> productList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        OrderBill orderBill = new OrderBill();
        orderBill.setBillNo("OR" + ParamUtils.dateConvert(new Date(), "yyMMddHHmmssSSS"));
        orderBill.setSalesman(loginAdmin.getAdminId());
        orderBill.setClientId(clientId);
        orderBill.setContact(contact);
        orderBill.setContactPhone(contactPhone);
        orderBill.setBillTime(new Date());
        orderBill.setBillAmount(billAmount);
        orderBill.setBillState(1);
        orderBill.setRemark(remark);
        orderBill.setCreateAt(new Date());
        orderBill.setCreateBy(loginAdmin.getAdminId());
        orderBill.setUpdateAt(new Date());
        orderBill.setUpdateBy(loginAdmin.getAdminId());
        orderBillDAO.insertSelective(orderBill);

        for (OrderBillProduct orderBillProduct: productList) {
            orderBillProduct.setBillId(orderBill.getBillId());
            orderBillProductDAO.insertSelective(orderBillProduct);
        }
        // 添加日志
        addLog(LogType.ORDER, Operate.ADD, orderBill.getBillId());
        return getOrderById(orderBill.getBillId());
    }


    /**
     * 更新订单
     *
     * 进行必要的检查：是否为待审核状态
     * 更新主表信息：order_bill
     * 更新关联的从表信息：order_bill_product
     * 添加日志信息：LogType.ORDER_BILL, Operate.UPDATE
     *
     * @param billId
     * @param contact 联系人
     * @param contactPhone 联系电话
     * @param billAmount 金额
     * @param remark 备注
     * @param productList 商品信息
     * @return 返回该订单编号
     */
    public OrderBill update(Integer billId, String contact, String contactPhone,
                            BigDecimal billAmount, String remark, List<OrderBillProduct> productList) {
        checkBillState(Collections.singletonList(billId), 1);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        OrderBill orderBill = new OrderBill();
        orderBill.setBillId(billId);
        orderBill.setContact(contact);
        orderBill.setContactPhone(contactPhone);
        orderBill.setBillAmount(billAmount);
        orderBill.setRemark(remark);
        orderBill.setUpdateAt(new Date());
        orderBill.setUpdateBy(loginAdmin.getAdminId());
        orderBillDAO.updateByPrimaryKeySelective(orderBill);

        // 先删除原来所有order_bill_product
        OrderBillProductQuery orderBillProductQuery = new OrderBillProductQuery();
        orderBillProductQuery.or().andBillIdEqualTo(orderBill.getBillId());
        orderBillProductDAO.deleteByExample(orderBillProductQuery);
        // 再新增现有关联order_bill_product
        for (OrderBillProduct orderBillProduct: productList) {
            orderBillProduct.setBillId(orderBill.getBillId());
            orderBillProductDAO.insertSelective(orderBillProduct);
        }
        // 添加日志
        addLog(LogType.ORDER, Operate.UPDATE, orderBill.getBillId());
        return getOrderById(orderBill.getBillId());
    }


    /**
     * 删除订单
     *
     * 进行必要的检查：是否为待审核状态
     * 删除主表信息：order_bill
     * 删除关联的从表信息：order_bill_product
     * 添加日志信息：LogType.ORDER_BILL, Operate.REMOVE
     *
     * @param idList
     * @return
     */
    public void remove(List<Integer> idList) {
        checkBillState(idList, 1);

        // 删除 order_bill
        OrderBillQuery orderBillQuery = new OrderBillQuery();
        orderBillQuery.or().andBillIdIn(idList);
        orderBillDAO.deleteByExample(orderBillQuery);
        // 删除关联 order_bill_product
        OrderBillProductQuery orderBillProductQuery = new OrderBillProductQuery();
        orderBillProductQuery.or().andBillIdIn(idList);
        orderBillProductDAO.deleteByExample(orderBillProductQuery);
        // 添加日志
        addLog(LogType.ORDER, Operate.REMOVE, idList);
    }

    /**
     * 订单物料分解
     *
     * 进行必要的检查：是否为生产中或已发货状态
     *
     * @param billId
     * @return
     */
    public List<OrderBillMaterial> getMaterialRequired(Integer billId) {
        OrderBillQuery orderBillQuery = new OrderBillQuery();
        orderBillQuery.or().andBillIdEqualTo(billId)
                .andBillStateNotEqualTo(3)
                .andBillStateNotEqualTo(4);
        if (orderBillDAO.countByExample(orderBillQuery) > 0) {
            throw new BadRequestException(BILL_STATE_NOT_PRODUCE_OR_DELIVERY);
        }

        OrderBillProductQuery orderBillProductQuery = new OrderBillProductQuery();
        OrderBillProductQuery.Criteria criteria = orderBillProductQuery.or();
        criteria.andBillIdEqualTo(billId);
        List<OrderBillProduct> productList = orderBillProductDAO.selectByExample(orderBillProductQuery);

        List<OrderBillMaterial> result = new ArrayList<OrderBillMaterial>();
        for (OrderBillProduct orderBillProduct: productList) {
            Product product = productDAO.selectByPrimaryKey(orderBillProduct.getProductId());
            if (product == null) {
                continue;
            }
            ProductMaterialQuery productMaterialQuery = new ProductMaterialQuery();
            productMaterialQuery.or().andProductIdEqualTo(orderBillProduct.getProductId());
            List<ProductMaterial> productMaterialList = productMaterialDAO.selectByExample(productMaterialQuery);

            for (ProductMaterial productMaterial: productMaterialList) {
                Material material = materialDAO.selectByPrimaryKey(productMaterial.getMaterialId());
                if (material != null) {
                    OrderBillMaterial orderBillMaterial = new OrderBillMaterial();
                    orderBillMaterial.setMaterialNo(material.getMaterialNo());
                    orderBillMaterial.setMaterialName(material.getMaterialName());
                    orderBillMaterial.setQuantity(productMaterial.getQuantity() * orderBillProduct.getQuantity());
                    orderBillMaterial.setProductNo(product.getProductNo());
                    orderBillMaterial.setProductName(product.getProductName());
                    orderBillMaterial.setMaterialProperty(productMaterial.getMaterialProperty());
                    orderBillMaterial.setProductMaterialRemark(productMaterial.getRemark());
                    result.add(orderBillMaterial);
                }
            }
        }
        return result;
    }

    private void checkBillState(List<Integer> idList, int state) {
        OrderBillQuery orderBillQuery = new OrderBillQuery();
        orderBillQuery.or().andBillIdIn(idList)
                .andBillStateNotEqualTo(state);
        if (orderBillDAO.countByExample(orderBillQuery) > 0) {
            if (state == 1) {
                throw new BadRequestException(BILL_STATE_NOT_UNAUDIT);
            }
            if (state == 2) {
                throw new BadRequestException(BILL_STATE_NOT_AUDIT);
            }
            if (state == 3) {
                throw new BadRequestException(BILL_STATE_NOT_PRODUCE);
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

    private List<Integer> searchClientByClientName(String searchKey) {
        ClientQuery clientQuery = new ClientQuery();
        ClientQuery.Criteria criteria = clientQuery.or();
        criteria.andClientNameLike("%" + searchKey + "%");
        List<Client> clientList = clientDAO.selectByExample(clientQuery);
        List<Integer> clientIdList = new ArrayList<Integer>();
        for (Client client : clientList) {
            clientIdList.add(client.getClientId());
        }
        return clientIdList;
    }

    private static final String BILL_STATE_NOT_UNAUDIT = "单据不是待审核状态";
    private static final String BILL_STATE_NOT_AUDIT = "单据不是已审核状态";
    private static final String BILL_STATE_NOT_PRODUCE = "单据不是生产中状态";
    private static final String BILL_STATE_NOT_PRODUCE_OR_DELIVERY = "单据不是生产中或已发货状态";
}
