package com.springmvc.dao;

import com.springmvc.dto.Admin;
import com.springmvc.dto.Role;
import com.springmvc.pojo.AdminQuery;
import java.util.List;

import com.springmvc.pojo.RoleQuery;
import org.apache.ibatis.annotations.Param;

public interface AdminDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(AdminQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(AdminQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param adminId
     */
    int deleteByPrimaryKey(Integer adminId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Admin record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Admin record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Admin> selectByExample(AdminQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param adminId
     */
    Admin selectByPrimaryKey(Integer adminId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Admin record, @Param("example") AdminQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Admin record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Admin record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<Admin> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<Admin> records);

    /**
     *  根据指定的条件查询符合条件的数据库记录（关联admin_role,role表取出roleIdList,roleNameList）
     *
     * @param example
     */
    List<Admin> selectWithRoleByExample(AdminQuery example);
}