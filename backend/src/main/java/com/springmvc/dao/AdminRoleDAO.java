package com.springmvc.dao;

import com.springmvc.dto.AdminRole;
import com.springmvc.pojo.AdminRoleQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminRoleDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(AdminRoleQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(AdminRoleQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param adminRoleId
     */
    int deleteByPrimaryKey(Integer adminRoleId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(AdminRole record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(AdminRole record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<AdminRole> selectByExample(AdminRoleQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param adminRoleId
     */
    AdminRole selectByPrimaryKey(Integer adminRoleId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") AdminRole record, @Param("example") AdminRoleQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") AdminRole record, @Param("example") AdminRoleQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(AdminRole record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(AdminRole record);
}