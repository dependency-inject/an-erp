package com.springmvc.dao;

import com.springmvc.pojo.Warehouse;
import com.springmvc.pojo.WarehouseQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WarehouseDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(WarehouseQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(WarehouseQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param warehouseId
     */
    int deleteByPrimaryKey(Integer warehouseId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Warehouse record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Warehouse record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Warehouse> selectByExample(WarehouseQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param warehouseId
     */
    Warehouse selectByPrimaryKey(Integer warehouseId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Warehouse record, @Param("example") WarehouseQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Warehouse record, @Param("example") WarehouseQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Warehouse record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Warehouse record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<Warehouse> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<Warehouse> records);
}