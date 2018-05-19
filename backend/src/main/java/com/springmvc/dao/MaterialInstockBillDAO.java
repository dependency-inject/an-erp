package com.springmvc.dao;

import com.springmvc.dto.MaterialInstockBill;
import com.springmvc.pojo.MaterialInstockBillQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MaterialInstockBillDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(MaterialInstockBillQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(MaterialInstockBillQuery example);

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
    int insert(MaterialInstockBill record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(MaterialInstockBill record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<MaterialInstockBill> selectByExample(MaterialInstockBillQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param billId
     */
    MaterialInstockBill selectByPrimaryKey(Integer billId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") MaterialInstockBill record, @Param("example") MaterialInstockBillQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") MaterialInstockBill record, @Param("example") MaterialInstockBillQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(MaterialInstockBill record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(MaterialInstockBill record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<MaterialInstockBill> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<MaterialInstockBill> records);
}