package com.springmvc.dao;

import com.springmvc.dto.OrderBillProduct;
import com.springmvc.pojo.OrderBillProductQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderBillProductDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(OrderBillProductQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(OrderBillProductQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param billProductId
     */
    int deleteByPrimaryKey(Integer billProductId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(OrderBillProduct record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(OrderBillProduct record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<OrderBillProduct> selectByExample(OrderBillProductQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param billProductId
     */
    OrderBillProduct selectByPrimaryKey(Integer billProductId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") OrderBillProduct record, @Param("example") OrderBillProductQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") OrderBillProduct record, @Param("example") OrderBillProductQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(OrderBillProduct record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(OrderBillProduct record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<OrderBillProduct> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<OrderBillProduct> records);
}