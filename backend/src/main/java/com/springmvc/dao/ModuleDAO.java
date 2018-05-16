package com.springmvc.dao;

import com.springmvc.pojo.Module;
import com.springmvc.pojo.ModuleQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModuleDAO {
    /**
     *  根据指定的条件获取数据库记录数
     *
     * @param example
     */
    long countByExample(ModuleQuery example);

    /**
     *  根据指定的条件删除数据库符合条件的记录
     *
     * @param example
     */
    int deleteByExample(ModuleQuery example);

    /**
     *  根据主键删除数据库的记录
     *
     * @param moduleId
     */
    int deleteByPrimaryKey(Integer moduleId);

    /**
     *  新写入数据库记录
     *
     * @param record
     */
    int insert(Module record);

    /**
     *  动态字段,写入数据库记录
     *
     * @param record
     */
    int insertSelective(Module record);

    /**
     *  根据指定的条件查询符合条件的数据库记录
     *
     * @param example
     */
    List<Module> selectByExample(ModuleQuery example);

    /**
     *  根据指定主键获取一条数据库记录
     *
     * @param moduleId
     */
    Module selectByPrimaryKey(Integer moduleId);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") Module record, @Param("example") ModuleQuery example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") Module record, @Param("example") ModuleQuery example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKeySelective(Module record);

    /**
     *  根据主键来更新符合条件的数据库记录
     *
     * @param record
     */
    int updateByPrimaryKey(Module record);

    /**
     *  批量写入数据库记录
     *
     * @param records
     */
    int insertBatchSelective(List<Module> records);

    /**
     *  批量更新数据库记录
     *
     * @param records
     */
    int updateBatchByPrimaryKeySelective(List<Module> records);
}