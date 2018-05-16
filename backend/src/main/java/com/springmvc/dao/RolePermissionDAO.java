package com.springmvc.dao;

import com.springmvc.pojo.RolePermission;
import com.springmvc.pojo.RolePermissionQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RolePermissionDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(RolePermissionQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(RolePermissionQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param rolePermissionId
     */
    int deleteByPrimaryKey(Integer rolePermissionId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(RolePermission record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(RolePermission record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<RolePermission> selectByExample(RolePermissionQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param rolePermissionId
     */
    RolePermission selectByPrimaryKey(Integer rolePermissionId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") RolePermission record, @Param("example") RolePermissionQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") RolePermission record, @Param("example") RolePermissionQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(RolePermission record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(RolePermission record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<RolePermission> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<RolePermission> records);
}