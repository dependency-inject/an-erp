package com.springmvc.service;

import com.springmvc.dao.*;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.*;
import com.springmvc.utils.MD5Utils;
import com.springmvc.utils.ParamUtils;
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

    public List<Role> getRoleList() {
        return roleDAO.selectWithPermissionByExample(new RoleQuery());
    }

    public List<Permission> getPermissionList() {
        PermissionQuery permissionQuery = new PermissionQuery();
        permissionQuery.setOrderByClause("module_sort,sort asc");
        return permissionDAO.selectWithModuleByExample(permissionQuery);
    }

    public Role getRoleById(int roleId) {
        Role role = roleDAO.selectByPrimaryKey(roleId);
        return role;
    }

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

    public void updateRolePermissions(Integer roleId, List<Integer> permissionIdList) {
        checkNotSystemDefault(Collections.singletonList(roleId));

        List<RolePermission> permissionList = new ArrayList<RolePermission>();
        for (Integer permissionId : permissionIdList) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            permissionList.add(rolePermission);
        }

        RolePermissionQuery rolePermissionQuery = new RolePermissionQuery();
        rolePermissionQuery.or().andRoleIdEqualTo(roleId);
        // 先删除原来所有权限
        rolePermissionDAO.deleteByExample(rolePermissionQuery);
        // 再新增现有权限
        rolePermissionDAO.insertBatchSelective(permissionList);
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
