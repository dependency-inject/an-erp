package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.*;
import com.springmvc.service.OrderService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService orderService;

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<OrderBill> getList(Integer state, Boolean onlyNotOutstock) {
        return orderService.getList(state, onlyNotOutstock);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<OrderBill> search(@RequestParam Integer current, @RequestParam Integer limit,
                                      String sortColumn, String sort, String searchKey, Integer state,
                                      Long beginTime, Long endTime) {
        return orderService.pageOrder(current, limit, sortColumn, sort, searchKey, state,
                ParamUtils.toDate(beginTime), ParamUtils.toDate(endTime));
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public OrderBill getById(@RequestParam Integer billId) {
        return orderService.getOrderById(billId);
    }

    @RequestMapping(value = "audit", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ORDER_AUDIT)
    public String audit(@RequestParam String idList) {
        orderService.audit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/unaudit", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ORDER_AUDIT)
    public String unaudit(@RequestParam String idList) {
        orderService.unaudit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/produce", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ORDER_PRODUCE)
    public String produce(@RequestParam Integer billId) {
        orderService.produce(billId);
        return "success";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ORDER_CANCEL)
    public String cancel(@RequestParam Integer billId) {
        orderService.cancel(billId);
        return "success";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ORDER_ADD)
    public OrderBill add(@RequestParam Integer clientId, @RequestParam String contact, @RequestParam String contactPhone,
                         @RequestParam BigDecimal billAmount, String remark, @RequestParam String productList) {
        return orderService.add(clientId, contact, contactPhone, billAmount,
                remark, ParamUtils.jsonToList(productList, OrderBillProduct.class));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ORDER_ADD)
    public OrderBill update(@RequestParam Integer billId, @RequestParam String contact, @RequestParam String contactPhone,
                            @RequestParam BigDecimal billAmount, String remark, @RequestParam String productList) {
        return orderService.update(billId, contact, contactPhone, billAmount,
                remark, ParamUtils.jsonToList(productList, OrderBillProduct.class));
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ORDER_REMOVE)
    public String remove(@RequestParam String idList) {
        orderService.remove(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/getMaterialRequired", method = RequestMethod.POST)
    @ResponseBody
    public List<OrderBillMaterial> getMaterialRequired(@RequestParam Integer billId) {
        return orderService.getMaterialRequired(billId);
    }
}
