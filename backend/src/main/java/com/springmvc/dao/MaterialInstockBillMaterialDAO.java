package com.springmvc.dao;

import com.springmvc.dto.MaterialInstockBillMaterial;
import com.springmvc.pojo.MaterialInstockBillMaterialQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MaterialInstockBillMaterialDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(MaterialInstockBillMaterialQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(MaterialInstockBillMaterialQuery example);

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
    int insert(MaterialInstockBillMaterial record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(MaterialInstockBillMaterial record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<MaterialInstockBillMaterial> selectByExample(MaterialInstockBillMaterialQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param billMaterialId
     */
    MaterialInstockBillMaterial selectByPrimaryKey(Integer billMaterialId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") MaterialInstockBillMaterial record, @Param("example") MaterialInstockBillMaterialQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") MaterialInstockBillMaterial record, @Param("example") MaterialInstockBillMaterialQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(MaterialInstockBillMaterial record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(MaterialInstockBillMaterial record);
}