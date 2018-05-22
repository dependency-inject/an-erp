package com.springmvc.dao;

import com.springmvc.dto.Client;
import com.springmvc.pojo.ClientQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClientDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(ClientQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(ClientQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param clientId
     */
    int deleteByPrimaryKey(Integer clientId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Client record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Client record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Client> selectByExample(ClientQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param clientId
     */
    Client selectByPrimaryKey(Integer clientId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Client record, @Param("example") ClientQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Client record, @Param("example") ClientQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Client record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Client record);
}