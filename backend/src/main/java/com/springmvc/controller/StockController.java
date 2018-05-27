package com.springmvc.controller;

import com.springmvc.dto.MaterialStockRecord;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.Product;
import com.springmvc.dto.ProductStockRecord;
import com.springmvc.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Resource
    StockService stockService;

    /**
     * 统计货品仓储
     *
     * @param current    当前页面
     * @param limit      每页条目
     * @param sortColumn 排序依据
     * @param sort       排序方式
     * @param searchKey  搜索关键字
     * @return 一页结果
     */
    @RequestMapping(value = "/product-search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<ProductStockRecord> productSearch(@RequestParam Integer current, @RequestParam Integer limit,
                                                      String sortColumn, String sort, String searchKey) {
        return stockService.pageProduct(current, limit, sortColumn, sort, searchKey);
    }

    /**
     * 统计物料仓储
     *
     * @param current    当前页面
     * @param limit      每页条目
     * @param sortColumn 排序依据
     * @param sort       排序方式
     * @param searchKey  搜索关键字
     * @return 一页结果
     */
    @RequestMapping(value = "/material-search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<MaterialStockRecord> materialSearch(@RequestParam Integer current, @RequestParam Integer limit,
                                                        String sortColumn, String sort, String searchKey) {
        return stockService.pageMaterial(current, limit, sortColumn, sort, searchKey);
    }
}
