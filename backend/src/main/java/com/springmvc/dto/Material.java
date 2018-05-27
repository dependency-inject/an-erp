package com.springmvc.dto;

import com.springmvc.pojo.MaterialEntity;

public class Material extends MaterialEntity {

    /**
     *  真实库存
     */
    private Integer realStock;

    /**
     *  得到真实库存
     * @return
     */
    public Integer getRealStock() {
        return realStock;
    }

    /**
     *  设置真实库存
     * @param realStock
     */
    public void setRealStock(Integer realStock) {
        this.realStock = realStock;
    }
}