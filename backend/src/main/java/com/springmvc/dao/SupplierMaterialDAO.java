package com.springmvc.dao;

import com.springmvc.dto.SupplierMaterial;
import com.springmvc.pojo.SupplierMaterialQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SupplierMaterialDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(SupplierMaterialQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(SupplierMaterialQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param supplierMaterialId
     */
    int deleteByPrimaryKey(Integer supplierMaterialId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(SupplierMaterial record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(SupplierMaterial record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<SupplierMaterial> selectByExample(SupplierMaterialQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param supplierMaterialId
     */
    SupplierMaterial selectByPrimaryKey(Integer supplierMaterialId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") SupplierMaterial record, @Param("example") SupplierMaterialQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") SupplierMaterial record, @Param("example") SupplierMaterialQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SupplierMaterial record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(SupplierMaterial record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<SupplierMaterial> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<SupplierMaterial> records);
}