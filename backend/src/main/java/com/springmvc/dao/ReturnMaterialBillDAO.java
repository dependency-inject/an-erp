package com.springmvc.dao;

import com.springmvc.pojo.ReturnMaterialBill;
import com.springmvc.pojo.ReturnMaterialBillQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReturnMaterialBillDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(ReturnMaterialBillQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(ReturnMaterialBillQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param billId
     */
    int deleteByPrimaryKey(Integer billId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(ReturnMaterialBill record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(ReturnMaterialBill record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<ReturnMaterialBill> selectByExample(ReturnMaterialBillQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param billId
     */
    ReturnMaterialBill selectByPrimaryKey(Integer billId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ReturnMaterialBill record, @Param("example") ReturnMaterialBillQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ReturnMaterialBill record, @Param("example") ReturnMaterialBillQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ReturnMaterialBill record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(ReturnMaterialBill record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<ReturnMaterialBill> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<ReturnMaterialBill> records);
}