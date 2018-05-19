package com.springmvc.dao;

import com.springmvc.dto.ProductInstockBillProduct;
import com.springmvc.pojo.ProductInstockBillProductQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductInstockBillProductDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(ProductInstockBillProductQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(ProductInstockBillProductQuery example);

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
    int insert(ProductInstockBillProduct record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(ProductInstockBillProduct record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<ProductInstockBillProduct> selectByExample(ProductInstockBillProductQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param billProductId
     */
    ProductInstockBillProduct selectByPrimaryKey(Integer billProductId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ProductInstockBillProduct record, @Param("example") ProductInstockBillProductQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ProductInstockBillProduct record, @Param("example") ProductInstockBillProductQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ProductInstockBillProduct record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(ProductInstockBillProduct record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<ProductInstockBillProduct> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<ProductInstockBillProduct> records);
}