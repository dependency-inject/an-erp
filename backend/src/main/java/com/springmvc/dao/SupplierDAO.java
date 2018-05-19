package com.springmvc.dao;

import com.springmvc.dto.Supplier;
import com.springmvc.pojo.SupplierQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SupplierDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(SupplierQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(SupplierQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param supplierId
     */
    int deleteByPrimaryKey(Integer supplierId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Supplier record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Supplier record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Supplier> selectByExample(SupplierQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param supplierId
     */
    Supplier selectByPrimaryKey(Integer supplierId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Supplier record, @Param("example") SupplierQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Supplier record, @Param("example") SupplierQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Supplier record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Supplier record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<Supplier> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<Supplier> records);
}