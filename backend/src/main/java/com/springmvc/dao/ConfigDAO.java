package com.springmvc.dao;

import com.springmvc.dto.Config;
import com.springmvc.pojo.ConfigQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConfigDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(ConfigQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(ConfigQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param configId
     */
    int deleteByPrimaryKey(Integer configId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Config record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Config record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Config> selectByExample(ConfigQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param configId
     */
    Config selectByPrimaryKey(Integer configId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Config record, @Param("example") ConfigQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Config record, @Param("example") ConfigQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Config record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Config record);
}