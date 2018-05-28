package com.springmvc.dao;

import com.springmvc.dto.Admin;
import com.springmvc.dto.Material;
import com.springmvc.dto.MaterialStockRecord;
import com.springmvc.pojo.AdminQuery;
import com.springmvc.pojo.MaterialQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MaterialDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(MaterialQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(MaterialQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param materialId
     */
    int deleteByPrimaryKey(Integer materialId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Material record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Material record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Material> selectByExample(MaterialQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param materialId
     */
    Material selectByPrimaryKey(Integer materialId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Material record, @Param("example") MaterialQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Material record, @Param("example") MaterialQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Material record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Material record);

    /**
     *   根据指定的条件查询符合条件的数据库记录（物料库存）
     *
     * @param example 查询条件
     * @return 一页记录
     */
    List<MaterialStockRecord> selectWithStockByExample(MaterialQuery example);
}