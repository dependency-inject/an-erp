package com.springmvc.dao;

import com.springmvc.pojo.Product;
import com.springmvc.pojo.ProductQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(ProductQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(ProductQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param productId
     */
    int deleteByPrimaryKey(Integer productId);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Product> selectByExample(ProductQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param productId
     */
    Product selectByPrimaryKey(Integer productId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Product record, @Param("example") ProductQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Product record, @Param("example") ProductQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Product record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Product record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<Product> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<Product> records);
}