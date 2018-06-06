package com.springmvc.dto;

public class MaterialLack {
    /**
     * 物料ID
     * 表字段 : material.material_id
     */
    private Integer materialId;

    /**
     * 物料编号
     * 表字段 : material.material_no
     */
    private String materialNo;

    /**
     * 物料名称
     * 表字段 : material.material_name
     */
    private String materialName;

    /**
     * 计量单位
     * 表字段 : material.unit
     */
    private String unit;

    /**
     *总库存
     */
    private Integer totalStock;
    /**
     * 出库可用库存
     */
    private Integer useOutstock;

    /**
     * 领料可用库存
     */
    private Integer useDraw;

    public Integer getTotalStock(){return totalStock; }
    public void setTotalStock(Integer totalStock){this.totalStock = totalStock;}
    public Integer getUseOutstock(){return useOutstock; }
    public void setUseOutstock(Integer useOutstock){this.useOutstock = useOutstock;}
    public Integer getUseDraw(){return useDraw; }
    public void setUseDraw(Integer useDraw){this.useDraw = useDraw;}
    /**
     * 获取 物料ID
     * 表字段 : material.material_id
     *
     * @return Integer
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * 设置 物料ID
     * 表字段 : material.material_id
     *
     * @param materialId
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    /**
     * 获取 物料编号
     * 表字段 : material.material_no
     *
     * @return String
     */
    public String getMaterialNo() {
        return materialNo;
    }

    /**
     * 设置 物料编号
     * 表字段 : material.material_no
     *
     * @param materialNo
     */
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo == null ? null : materialNo.trim();
    }

    /**
     * 获取 物料名称
     * 表字段 : material.material_name
     *
     * @return String
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * 设置 物料名称
     * 表字段 : material.material_name
     *
     * @param materialName
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
    }

    /**
     * 获取 计量单位
     * 表字段 : material.unit
     *
     * @return String
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置 计量单位
     * 表字段 : material.unit
     *
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }


}
