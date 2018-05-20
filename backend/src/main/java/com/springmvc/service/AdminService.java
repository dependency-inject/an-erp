package com.springmvc.service;

import com.springmvc.dao.AdminDAO;
import com.springmvc.dao.AdminRoleDAO;
import com.springmvc.dao.PermissionDAO;
import com.springmvc.dao.RoleDAO;
import com.springmvc.dto.*;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.*;
import com.springmvc.utils.MD5Utils;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("AdminService")
@Transactional
public class AdminService extends BaseService {

    @Resource
    private AdminDAO adminDAO;

    @Resource
    private AdminRoleDAO adminRoleDAO;

    @Resource
    private PermissionDAO permissionDAO;
    
    @Resource
    private RoleDAO roleDAO;

    public Admin addAdmin(String loginName, String trueName, Boolean closed, String mobile, List<Integer> roleIdList) {
        AdminQuery adminQuery = new AdminQuery();
        adminQuery.or().andLoginNameEqualTo(loginName);
        if (adminDAO.countByExample(adminQuery) > 0) {
            throw new BadRequestException(LOGIN_NAME_EXIST);
        }

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Admin admin = new Admin();
        admin.setLoginName(loginName);
        admin.setPassword(MD5Utils.strToMD5(loginName));
        admin.setTrueName(trueName);
        admin.setClosed(closed);
        admin.setMobile(mobile);
        admin.setSysDefault(false);
        admin.setCreateAt(new Date());
        admin.setCreateBy(loginAdmin.getAdminId());
        admin.setUpdateAt(new Date());
        admin.setUpdateBy(loginAdmin.getAdminId());
        adminDAO.insertSelective(admin);

        // 新增关联role
        for (Integer roleId : roleIdList) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getAdminId());
            adminRole.setRoleId(roleId);
            adminRoleDAO.insertSelective(adminRole);
        }
        // 添加日志
        addLog(LogType.ADMIN, Operate.ADD, admin.getAdminId());
        return getAdminById(admin.getAdminId());
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException(TWO_INPUT_PASSWORDS_NOT_SAME);
        }

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Admin admin = adminDAO.selectByPrimaryKey(loginAdmin.getAdminId());
        if (!admin.getPassword().equals(MD5Utils.strToMD5(oldPassword))) {
            throw new BadRequestException(OLD_PASSWORD_INCORRECT);
        }

        admin.setPassword(MD5Utils.strToMD5(newPassword));
        adminDAO.updateByPrimaryKeySelective(admin);
    }

    public Admin getAdminById(int adminId) {
        Admin admin = adminDAO.selectByPrimaryKey(adminId);
        // 不返回密码
        admin.setPassword(null);
        return admin;
    }

    public Admin getAdminWithPermissionById(int adminId) {
        Admin admin = getAdminWithRoleById(adminId);

        RoleQuery roleQuery = new RoleQuery();
        roleQuery.or().andRoleIdIn(ParamUtils.toIntList(admin.getRoleIdList()));
        List<Role> roleList = roleDAO.selectWithPermissionByExample(roleQuery);
        Set<Integer> permissionIdSet = new HashSet<Integer>();
        for (Role role : roleList) {
            permissionIdSet.addAll(ParamUtils.toIntList(role.getPermissionIdList()));
        }

        PermissionQuery permissionQuery = new PermissionQuery();
        permissionQuery.or().andPermissionIdIn(new ArrayList<Integer>(permissionIdSet));
        List<Permission> permissionList = permissionDAO.selectWithModuleByExample(permissionQuery);
        List<String> permissionNameList = new ArrayList<String>();
        for (Permission permission : permissionList) {
            permissionNameList.add(permission.getModuleName() + "@" + permission.getPermissionName());
        }
        admin.setPermissionNameList(ParamUtils.toStr(permissionNameList));
        return admin;
    }

    public Admin getAdminWithRoleById(int adminId) {
        AdminQuery adminQuery = new AdminQuery();
        adminQuery.or().andAdminIdEqualTo(adminId);
        List<Admin> adminList = adminDAO.selectWithRoleByExample(adminQuery);
        if (adminList.size() == 0) {
            return null;
        }
        Admin admin = adminList.get(0);
        // 不返回密码
        admin.setPassword(null);
        return admin;
    }

    public Admin login(String loginName, String password) throws Exception {
        AdminQuery adminQuery = new AdminQuery();
        adminQuery.or().andLoginNameEqualTo(loginName);
        List<Admin> list = adminDAO.selectByExample(adminQuery);
        if (list.size() == 0) {
            throw new Exception(LOGIN_NAME_OR_PASSWORD_ERROR);
        }
        Admin admin = list.get(0);
        if (!admin.getPassword().equals(MD5Utils.strToMD5(password))) {
            throw new Exception(LOGIN_NAME_OR_PASSWORD_ERROR);
        }
        if (admin.getClosed()){
            throw new Exception(ACCOUNT_IS_CLOSED);
        }
        // 添加日志
        addLog(LogType.SYSTEM, Operate.LOGIN, admin.getAdminId(), admin.getAdminId());
        return getAdminById(admin.getAdminId());
    }

    public PageMode<Admin> pageAdmin(Integer current, Integer limit, String sortColumn, String sort,
                                     String searchKey, Integer closed) {
        AdminQuery adminQuery = new AdminQuery();
        adminQuery.setOffset((current-1) * limit);
        adminQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            adminQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }

        // TODO: 目前对searchKey支持比较机械
        // 搜索登录名
        AdminQuery.Criteria criteria = adminQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andLoginNameLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(closed) && !closed.equals(-1)) {
            criteria.andClosedEqualTo(closed > 0);
        }
        // 搜索真实姓名
        criteria = adminQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andTrueNameLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(closed) && !closed.equals(-1)) {
            criteria.andClosedEqualTo(closed > 0);
        }
        // 搜索手机
        criteria = adminQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andMobileLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(closed) && !closed.equals(-1)) {
            criteria.andClosedEqualTo(closed > 0);
        }

        List<Admin> result = adminDAO.selectWithRoleByExample(adminQuery);
        // 不返回密码
        for (Admin admin : result) {
            admin.setPassword(null);
        }
        return new PageMode<Admin>(result, adminDAO.countByExample(adminQuery));
    }

    public void removeAdmin(List<Integer> idList) {
        checkNotSystemDefault(idList);
        // 检查是否被log引用
        LogQuery logQuery = new LogQuery();
        logQuery.or().andAdminIdIn(idList).andLogTypeNotEqualTo(LogType.SYSTEM.type);
        logQuery.or().andAdminIdIn(idList).andOperateNotEqualTo(Operate.LOGIN.operate);
        if (logDAO.countByExample(logQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_LOG);
        }

        // 删除 admin
        AdminQuery adminQuery = new AdminQuery();
        adminQuery.or().andAdminIdIn(idList);
        adminDAO.deleteByExample(adminQuery);
        // 删除关联 admin_role
        AdminRoleQuery adminRoleQuery = new AdminRoleQuery();
        adminRoleQuery.or().andAdminIdIn(idList);
        adminRoleDAO.deleteByExample(adminRoleQuery);
        // 删除关联 log
        logQuery = new LogQuery();
        logQuery.or().andAdminIdIn(idList);
        logDAO.deleteByExample(logQuery);
        // 添加日志
        addLog(LogType.ADMIN, Operate.REMOVE, idList);
    }

    public Admin updateAdmin(Integer adminId, String trueName, Boolean closed, String mobile, List<Integer> roleIdList) {
        checkNotSystemDefault(Collections.singletonList(adminId));
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Admin admin = new Admin();
        admin.setAdminId(adminId);
        admin.setTrueName(trueName);
        admin.setClosed(closed);
        admin.setMobile(mobile);
        admin.setUpdateAt(new Date());
        admin.setUpdateBy(loginAdmin.getAdminId());
        adminDAO.updateByPrimaryKeySelective(admin);

        // 先删除原来所有admin_role
        AdminRoleQuery adminRoleQuery = new AdminRoleQuery();
        adminRoleQuery.or().andAdminIdEqualTo(admin.getAdminId());
        adminRoleDAO.deleteByExample(adminRoleQuery);
        // 再新增现有关联admin_role
        for (Integer roleId : roleIdList) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getAdminId());
            adminRole.setRoleId(roleId);
            adminRoleDAO.insertSelective(adminRole);
        }
        // 添加日志
        addLog(LogType.ADMIN, Operate.UPDATE, admin.getAdminId());
        return getAdminById(admin.getAdminId());
    }

    public void updateAdminClosedState(List<Integer> idList, Boolean closed) {
        checkNotSystemDefault(idList);
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Admin admin = new Admin();
        admin.setClosed(closed);
        admin.setUpdateAt(new Date());
        admin.setUpdateBy(loginAdmin.getAdminId());

        AdminQuery adminQuery = new AdminQuery();
        adminQuery.or().andAdminIdIn(idList);
        adminDAO.updateByExampleSelective(admin, adminQuery);
        // 添加日志
        addLog(LogType.ADMIN, Operate.UPDATE, idList);
    }

    private void checkNotSystemDefault(List<Integer> idList) {
        AdminQuery adminQuery = new AdminQuery();
        adminQuery.or().andAdminIdIn(idList).andSysDefaultEqualTo(true);
        if (adminDAO.countByExample(adminQuery) > 0) {
            throw new BadRequestException(SYSTEM_ADMIN_OPERATION_DENIED);
        }
    }

    private static final String LOGIN_NAME_EXIST = "登录名已存在";
    private static final String TWO_INPUT_PASSWORDS_NOT_SAME = "两次输入密码不一致";
    private static final String OLD_PASSWORD_INCORRECT = "旧密码错误";
    private static final String LOGIN_NAME_OR_PASSWORD_ERROR = "登录名或密码错误";
    private static final String ACCOUNT_IS_CLOSED = "账号已停用";
    private static final String ADMIN_REFER_BY_LOG = "用户被日志引用";
    private static final String SYSTEM_ADMIN_OPERATION_DENIED = "系统默认用户不可操作";

}
