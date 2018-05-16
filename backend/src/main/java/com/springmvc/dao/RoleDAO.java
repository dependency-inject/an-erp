package com.springmvc.dao;

import com.springmvc.pojo.Role;
import com.springmvc.pojo.RoleQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(RoleQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(RoleQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param roleId
     */
    int deleteByPrimaryKey(Integer roleId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Role record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Role record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Role> selectByExample(RoleQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param roleId
     */
    Role selectByPrimaryKey(Integer roleId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Role record, @Param("example") RoleQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Role record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<Role> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<Role> records);
}