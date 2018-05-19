package com.springmvc.dao;

import com.springmvc.dto.MaterialCategory;
import com.springmvc.pojo.MaterialCategoryQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MaterialCategoryDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(MaterialCategoryQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(MaterialCategoryQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param categoryId
     */
    int deleteByPrimaryKey(Integer categoryId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(MaterialCategory record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(MaterialCategory record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<MaterialCategory> selectByExample(MaterialCategoryQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param categoryId
     */
    MaterialCategory selectByPrimaryKey(Integer categoryId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") MaterialCategory record, @Param("example") MaterialCategoryQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") MaterialCategory record, @Param("example") MaterialCategoryQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(MaterialCategory record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(MaterialCategory record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<MaterialCategory> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<MaterialCategory> records);
}