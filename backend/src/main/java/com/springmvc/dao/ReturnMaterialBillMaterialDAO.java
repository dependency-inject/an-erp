package com.springmvc.dao;

import com.springmvc.pojo.ReturnMaterialBillMaterial;
import com.springmvc.pojo.ReturnMaterialBillMaterialQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReturnMaterialBillMaterialDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(ReturnMaterialBillMaterialQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(ReturnMaterialBillMaterialQuery example);

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
    int insert(ReturnMaterialBillMaterial record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(ReturnMaterialBillMaterial record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<ReturnMaterialBillMaterial> selectByExample(ReturnMaterialBillMaterialQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param billMaterialId
     */
    ReturnMaterialBillMaterial selectByPrimaryKey(Integer billMaterialId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ReturnMaterialBillMaterial record, @Param("example") ReturnMaterialBillMaterialQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ReturnMaterialBillMaterial record, @Param("example") ReturnMaterialBillMaterialQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ReturnMaterialBillMaterial record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(ReturnMaterialBillMaterial record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<ReturnMaterialBillMaterial> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<ReturnMaterialBillMaterial> records);
}