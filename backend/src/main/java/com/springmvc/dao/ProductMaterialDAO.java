package com.springmvc.dao;

import com.springmvc.pojo.ProductMaterial;
import com.springmvc.pojo.ProductMaterialQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductMaterialDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(ProductMaterialQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(ProductMaterialQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param productMaterialId
     */
    int deleteByPrimaryKey(Integer productMaterialId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(ProductMaterial record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(ProductMaterial record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<ProductMaterial> selectByExample(ProductMaterialQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param productMaterialId
     */
    ProductMaterial selectByPrimaryKey(Integer productMaterialId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ProductMaterial record, @Param("example") ProductMaterialQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ProductMaterial record, @Param("example") ProductMaterialQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ProductMaterial record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(ProductMaterial record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<ProductMaterial> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<ProductMaterial> records);
}