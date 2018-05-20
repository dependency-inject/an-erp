package com.springmvc.dao;

import com.springmvc.dto.MaterialOutstockBillMaterial;
import com.springmvc.pojo.MaterialOutstockBillMaterialQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MaterialOutstockBillMaterialDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(MaterialOutstockBillMaterialQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(MaterialOutstockBillMaterialQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param billMaterialId
     */
    int deleteByPrimaryKey(Integer billMaterialId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(MaterialOutstockBillMaterial record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(MaterialOutstockBillMaterial record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<MaterialOutstockBillMaterial> selectByExample(MaterialOutstockBillMaterialQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param billMaterialId
     */
    MaterialOutstockBillMaterial selectByPrimaryKey(Integer billMaterialId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") MaterialOutstockBillMaterial record, @Param("example") MaterialOutstockBillMaterialQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") MaterialOutstockBillMaterial record, @Param("example") MaterialOutstockBillMaterialQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(MaterialOutstockBillMaterial record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(MaterialOutstockBillMaterial record);
}