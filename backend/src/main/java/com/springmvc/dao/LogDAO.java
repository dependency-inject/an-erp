package com.springmvc.dao;

import com.springmvc.dto.Log;
import com.springmvc.pojo.LogQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LogDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(LogQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(LogQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param logId
     */
    int deleteByPrimaryKey(Integer logId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Log record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Log record);

    /**
     * 
     *
     * @param example
     */
    List<Log> selectByExampleWithBLOBs(LogQuery example);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Log> selectByExample(LogQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param logId
     */
    Log selectByPrimaryKey(Integer logId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Log record, @Param("example") LogQuery example);

    /**
     * 
     *
     * @param record
     * @param example
     */
    int updateByExampleWithBLOBs(@Param("record") Log record, @Param("example") LogQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Log record, @Param("example") LogQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Log record);

    /**
     * 
     *
     * @param record
     */
    int updateByPrimaryKeyWithBLOBs(Log record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Log record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<Log> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<Log> records);
}