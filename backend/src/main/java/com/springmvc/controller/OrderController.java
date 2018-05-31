package com.springmvc.controller;

import com.springmvc.dto.OrderBill;
import com.springmvc.dto.OrderBillProduct;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.Product;
import com.springmvc.service.OrderService;
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

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<OrderBill> search(@RequestParam Integer current, @RequestParam Integer limit, String sortColumn, String sort, String searchKey, Integer state) {
        return orderService.pageOrder(current, limit, sortColumn, sort, searchKey, state);
    }

    @RequestMapping(value = "getById", method = RequestMethod.POST)
    @ResponseBody
    public OrderBill getById(@RequestParam Integer billId) {
        return orderService.getOrderById(billId);
    }

    @RequestMapping(value = "getProduct", method = RequestMethod.POST)
    @ResponseBody
    public List<OrderBillProduct> getProduct(@RequestParam Integer billId) { return orderService.getProduct(billId);}


    @RequestMapping(value = "shenhe", method = RequestMethod.POST)
    @ResponseBody
    public String shenhe(@RequestParam Integer adminId, @RequestParam Integer billId) {
        return orderService.shenhe(adminId, billId);
    }


    @RequestMapping(value = "fanshenhe", method = RequestMethod.POST)
    @ResponseBody
    public String fanshenhe(@RequestParam Integer adminId, @RequestParam Integer billId) {
        return orderService.fanshenhe(adminId, billId);
    }

    @RequestMapping(value = "produce", method = RequestMethod.POST)
    @ResponseBody
    public String produce(@RequestParam Integer adminId, @RequestParam Integer billId) {
        return orderService.produce(adminId, billId);
    }

    @RequestMapping(value = "deliver", method = RequestMethod.POST)
    @ResponseBody
    public String deliver(@RequestParam Integer adminId, @RequestParam Integer billId) {
        return orderService.deliver(adminId, billId);
    }

    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    @ResponseBody
    public String cancel(@RequestParam Integer adminId, @RequestParam Integer billId) {
        return orderService.cancel(adminId, billId);
    }

    @RequestMapping(value = "getProducts", method = RequestMethod.POST)
    @ResponseBody
    public List<Product> getProducts() {
        return orderService.getProducts();
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Integer add(@RequestParam Integer adminId, @RequestParam String contact, @RequestParam String contactPhone, @RequestParam BigDecimal billAmount,
                       String remark, @RequestParam String products) {
        return orderService.add(adminId, contact, contactPhone, billAmount, remark, products);
    }


    @RequestMapping(value = "remove", method = RequestMethod.POST)
    @ResponseBody
    public String remove(@RequestParam Integer billId) {
        return orderService.remove(billId);
    }

}
