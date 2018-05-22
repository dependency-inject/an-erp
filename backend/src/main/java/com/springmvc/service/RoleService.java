package com.springmvc.service;

import com.springmvc.dao.*;
import com.springmvc.dto.Admin;
import com.springmvc.dto.Permission;
import com.springmvc.dto.Role;
import com.springmvc.dto.RolePermission;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.*;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("RoleService")
@Transactional
public class RoleService extends BaseService {

    @Resource
    private AdminRoleDAO adminRoleDAO;

    @Resource
    private PermissionDAO permissionDAO;

    @Resource
    private RoleDAO roleDAO;

    @Resource
    private RolePermissionDAO rolePermissionDAO;

    /**
     * 新增角色信息
     *
     * 进行必要的检查：暂无
     * 将主表信息保存：role
     * 将关联的从表信息保存：暂无
     * 添加日志信息：LogType.ROLE, Operate.ADD
     *
     * @param roleName
     * @return
     */
    public Role addRole(String roleName) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Role role = new Role();
        role.setRoleName(roleName);
        role.setSysDefault(false);
        role.setCreateAt(new Date());
        role.setCreateBy(loginAdmin.getAdminId());
        role.setUpdateAt(new Date());
        role.setUpdateBy(loginAdmin.getAdminId());
        roleDAO.insertSelective(role);
        // 添加日志
        addLog(LogType.ROLE, Operate.ADD, role.getRoleId());
        return getRoleById(role.getRoleId());
    }

    /**
     * 查询角色信息（全部），包含权限信息
     *
     * 将主表信息取出：role left join role_permission
     *
     * @return
     */
    public List<Role> getRoleList() {
        return roleDAO.selectWithPermissionByExample(new RoleQuery());
    }

    /**
     * 查询权限信息（全部），包含模块信息
     *
     * 将主表信息取出：permission left join module
     *
     * @return
     */
    public List<Permission> getPermissionList() {
        PermissionQuery permissionQuery = new PermissionQuery();
        permissionQuery.setOrderByClause("module_sort,sort asc");
        return permissionDAO.selectWithModuleByExample(permissionQuery);
    }

    /**
     * 查询角色信息（单个）
     *
     * 将主表信息取出：role
     *
     * @param roleId
     * @return
     */
    public Role getRoleById(int roleId) {
        Role role = roleDAO.selectByPrimaryKey(roleId);
        return role;
    }

    /**
     * 删除角色信息
     *
     * 进行必要的检查：是否包含系统默认角色
     * 检查要删除的主表信息是否被其他信息引用：是否被admin_role信息引用
     * 删除主表信息：role
     * 删除关联的从表信息：role_permission
     * 添加日志信息：LogType.ROLE, Operate.REMOVE
     *
     * @param idList
     */
    public void removeRole(List<Integer> idList) {
        checkNotSystemDefault(idList);
        // 检查是否被admin引用
        AdminRoleQuery adminRoleQuery = new AdminRoleQuery();
        adminRoleQuery.or().andRoleIdIn(idList);
        if (adminRoleDAO.countByExample(adminRoleQuery) > 0) {
            throw new BadRequestException(ROLE_REFER_BY_ADMIN);
        }

        // 删除 role
        RoleQuery roleQuery = new RoleQuery();
        roleQuery.or().andRoleIdIn(idList);
        roleDAO.deleteByExample(roleQuery);
        // 删除关联 role_permission
        RolePermissionQuery rolePermissionQuery = new RolePermissionQuery();
        rolePermissionQuery.or().andRoleIdIn(idList);
        rolePermissionDAO.deleteByExample(rolePermissionQuery);
        // 添加日志
        addLog(LogType.ROLE, Operate.REMOVE, idList);
    }

    /**
     * 更新角色信息
     *
     * 进行必要的检查：是否为系统默认角色
     * 更新主表信息：role
     * 更新关联的从表信息：暂无
     * 添加日志信息：LogType.ROLE, Operate.UPDATE
     *
     * @param roleId
     * @param roleName
     * @return
     */
    public Role updateRole(Integer roleId, String roleName) {
        checkNotSystemDefault(Collections.singletonList(roleId));
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Role role = new Role();
        role.setRoleId(roleId);
        role.setRoleName(roleName);
        role.setUpdateAt(new Date());
        role.setUpdateBy(loginAdmin.getAdminId());
        roleDAO.updateByPrimaryKeySelective(role);
        // 添加日志
        addLog(LogType.ROLE, Operate.UPDATE, role.getRoleId());
        return getRoleById(role.getRoleId());
    }

    /**
     * 更新角色权限信息
     *
     * 进行必要的检查：是否为系统默认角色
     * 更新主表信息：暂无
     * 更新关联的从表信息：role_permission
     * 添加日志信息：LogType.ROLE_PERMISSION, Operate.UPDATE
     *
     * @param roleId
     * @param permissionIdList
     */
    public void updateRolePermissions(Integer roleId, List<Integer> permissionIdList) {
        checkNotSystemDefault(Collections.singletonList(roleId));

        // 先删除原来所有role_permission
        RolePermissionQuery rolePermissionQuery = new RolePermissionQuery();
        rolePermissionQuery.or().andRoleIdEqualTo(roleId);
        rolePermissionDAO.deleteByExample(rolePermissionQuery);
        // 再新增现有关联role_permission
        for (Integer permissionId : permissionIdList) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionDAO.insertSelective(rolePermission);
        }
        // 添加日志
        addLog(LogType.ROLE_PERMISSION, Operate.UPDATE, roleId);
    }

    private void checkNotSystemDefault(List<Integer> idList) {
        RoleQuery roleQuery = new RoleQuery();
        roleQuery.or().andRoleIdIn(idList).andSysDefaultEqualTo(true);
        if (roleDAO.countByExample(roleQuery) > 0) {
            throw new BadRequestException(SYSTEM_ROLE_OPERATION_DENIED);
        }
    }

    private static final String ROLE_REFER_BY_ADMIN = "角色被用户引用";
    private static final String SYSTEM_ROLE_OPERATION_DENIED = "系统默认角色不可操作";

}
