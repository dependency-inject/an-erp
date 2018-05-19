package com.springmvc.dao;

import com.springmvc.dto.Permission;
import com.springmvc.pojo.PermissionQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PermissionDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(PermissionQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(PermissionQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param permissionId
     */
    int deleteByPrimaryKey(Integer permissionId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Permission record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Permission record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Permission> selectByExample(PermissionQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param permissionId
     */
    Permission selectByPrimaryKey(Integer permissionId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Permission record, @Param("example") PermissionQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Permission record, @Param("example") PermissionQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Permission record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Permission record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<Permission> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<Permission> records);

    /**
     *  根据指定的条件查询符合条件的数据库记录（关联module表取出moduleName和moduleSort）
     *
     * @param example
     */
    List<Permission> selectWithModuleByExample(PermissionQuery example);
}