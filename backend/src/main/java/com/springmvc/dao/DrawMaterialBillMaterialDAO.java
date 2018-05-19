package com.springmvc.dao;

import com.springmvc.dto.DrawMaterialBillMaterial;
import com.springmvc.pojo.DrawMaterialBillMaterialQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DrawMaterialBillMaterialDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(DrawMaterialBillMaterialQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(DrawMaterialBillMaterialQuery example);

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
    int insert(DrawMaterialBillMaterial record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(DrawMaterialBillMaterial record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<DrawMaterialBillMaterial> selectByExample(DrawMaterialBillMaterialQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param billMaterialId
     */
    DrawMaterialBillMaterial selectByPrimaryKey(Integer billMaterialId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") DrawMaterialBillMaterial record, @Param("example") DrawMaterialBillMaterialQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") DrawMaterialBillMaterial record, @Param("example") DrawMaterialBillMaterialQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(DrawMaterialBillMaterial record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(DrawMaterialBillMaterial record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<DrawMaterialBillMaterial> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<DrawMaterialBillMaterial> records);
}