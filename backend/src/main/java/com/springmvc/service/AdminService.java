package com.springmvc.service;

import com.springmvc.dao.*;
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

    @Resource
    private MaterialInstockBillDAO materialInstockBillDAO;

    @Resource
    private MaterialInstockBillMaterialDAO materialInstockBillMaterialDAO;

    @Resource
    private MaterialOutstockBillDAO materialOutstockBillDAO;

    @Resource
    private MaterialOutstockBillMaterialDAO materialOutstockBillMaterialDAO;

    @Resource
    private ProductInstockBillDAO productInstockBillDAO;

    @Resource
    private ProductInstockBillProductDAO productInstockBillProductDAO;

    @Resource
    private ProductOutstockBillDAO productOutstockBillDAO;

    @Resource
    private ProductOutstockBillProductDAO productOutstockBillProductDAO;

    /**
     * 新增用户信息
     *
     * 进行必要的检查：login_name 是否已存在
     * 将主表信息保存：admin
     * 将关联的从表信息保存：admin_role
     * 添加日志信息：LogType.ADMIN, Operate.ADD
     *
     * @param loginName
     * @param trueName
     * @param closed
     * @param mobile
     * @param roleIdList
     * @return
     */
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

    /**
     * 修改密码
     *
     * 进行必要的检查：两次输入新密码一致、旧密码是否正确
     * 将主表信息保存：admin.password（MD5加密）
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
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

    /**
     * 查询用户信息（单个）
     *
     * 将主表信息取出：admin
     * 过滤不显示的信息：password
     *
     * @param adminId
     * @return
     */
    public Admin getAdminById(int adminId) {
        Admin admin = adminDAO.selectByPrimaryKey(adminId);
        // 不返回密码
        admin.setPassword(null);
        return admin;
    }

    /**
     * 查询用户信息（单个），包含权限信息
     *
     * 将主表信息取出：admin left join admin_role
     * 通过 admin_role.role_id 取出 role left join role_permission
     * 通过 role_permission.permission_id 取出 permission left join module
     *
     * permissionNameList: join(module.module_name + '@' + permission.permission_name, ',')
     *
     * @param adminId
     * @return
     */
    public Admin getAdminWithPermissionById(int adminId) {
        Admin admin = getAdminWithRoleById(adminId);

        List<Integer> roleIdList = ParamUtils.toIntList(admin.getRoleIdList());
        if (roleIdList.size() > 0) {
            RoleQuery roleQuery = new RoleQuery();
            roleQuery.or().andRoleIdIn(roleIdList);
            List<Role> roleList = roleDAO.selectWithPermissionByExample(roleQuery);
            Set<Integer> permissionIdSet = new HashSet<Integer>();
            for (Role role : roleList) {
                permissionIdSet.addAll(ParamUtils.toIntList(role.getPermissionIdList()));
            }

            if (permissionIdSet.size() > 0) {
                PermissionQuery permissionQuery = new PermissionQuery();
                permissionQuery.or().andPermissionIdIn(new ArrayList<Integer>(permissionIdSet));
                List<Permission> permissionList = permissionDAO.selectWithModuleByExample(permissionQuery);
                List<String> permissionNameList = new ArrayList<String>();
                for (Permission permission : permissionList) {
                    permissionNameList.add(permission.getModuleName() + "@" + permission.getPermissionName());
                }
                admin.setPermissionNameList(ParamUtils.toStr(permissionNameList));
            }
        }
        return admin;
    }

    /**
     * 查询用户信息（单个），包含角色信息
     *
     * 将主表信息取出：admin left join admin_role
     * 过滤不显示的信息：password
     *
     * @param adminId
     * @return
     */
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

    /**
     * 查询全部用户信息
     *
     * @param closed
     * @return
     */
    public List<Admin> getList(Integer closed) {
        AdminQuery adminQuery = new AdminQuery();
        if (!ParamUtils.isNull(closed) && !closed.equals(-1)) {
            adminQuery.or().andClosedEqualTo(closed > 0);
        }
        return adminDAO.selectByExample(adminQuery);
    }

    /**
     * 用户登录
     *
     * 进行必要的检查：登录名是否存在、密码是否正确、对应账号状态是否为停用
     * 添加日志信息：LogType.SYSTEM, Operate.LOGIN
     *
     * @param loginName
     * @param password
     * @return
     * @throws Exception
     */
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

    /**
     * 查询用户信息（分页）
     *
     * 将主表信息取出：admin left join admin_role（同时包含总记录数）
     * 搜索字段：登录名、真实姓名、手机
     * 筛选字段：账号状态
     * 过滤不显示的信息：password
     *
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @param closed
     * @return
     */
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

    /**
     * 删除用户信息
     *
     * 进行必要的检查：是否包含系统默认用户
     * 检查要删除的主表信息是否被其他信息引用：是否被“登录系统”之外的log信息引用
     * 删除主表信息：admin
     * 删除关联的从表信息：admin_role、log
     * 添加日志信息：LogType.ADMIN, Operate.REMOVE
     *
     * @param idList
     */
    public void removeAdmin(List<Integer> idList) {
        checkNotSystemDefault(idList);

        // 检查是否被物料入库单引用
        MaterialInstockBillQuery materialInstockBillQuery = new MaterialInstockBillQuery();
        materialInstockBillQuery.or().andFromPrincipalIn(idList);
        materialInstockBillQuery.or().andWarehousePrincipalIn(idList);
        if (materialInstockBillDAO.countByExample(materialInstockBillQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_MATERIAL_INSTOCK);
        }
        MaterialInstockBillMaterialQuery materialInstockBillMaterialQuery = new MaterialInstockBillMaterialQuery();
        materialInstockBillMaterialQuery.or().andPrincipalIn(idList);
        if (materialInstockBillMaterialDAO.countByExample(materialInstockBillMaterialQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_MATERIAL_INSTOCK);
        }

        // 检查是否被物料出库单引用
        MaterialOutstockBillQuery materialOutstockBillQuery = new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andToPrincipalIn(idList);
        materialOutstockBillQuery.or().andWarehousePrincipalIn(idList);
        if (materialOutstockBillDAO.countByExample(materialOutstockBillQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_MATERIAL_OUTSTOCK);
        }
        MaterialOutstockBillMaterialQuery materialOutstockBillMaterialQuery = new MaterialOutstockBillMaterialQuery();
        materialOutstockBillMaterialQuery.or().andPrincipalIn(idList);
        if (materialOutstockBillMaterialDAO.countByExample(materialOutstockBillMaterialQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_MATERIAL_OUTSTOCK);
        }

        // 检查是否被货品入库单引用
        ProductInstockBillQuery productInstockBillQuery = new ProductInstockBillQuery();
        productInstockBillQuery.or().andFromPrincipalIn(idList);
        productInstockBillQuery.or().andWarehousePrincipalIn(idList);
        if (productInstockBillDAO.countByExample(productInstockBillQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_PRODUCT_INSTOCK);
        }
        ProductInstockBillProductQuery productInstockBillProductQuery = new ProductInstockBillProductQuery();
        productInstockBillProductQuery.or().andPrincipalIn(idList);
        if (productInstockBillProductDAO.countByExample(productInstockBillProductQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_PRODUCT_INSTOCK);
        }

        // 检查是否被货品出库单引用
        ProductOutstockBillQuery productOutstockBillQuery = new ProductOutstockBillQuery();
        productOutstockBillQuery.or().andToPrincipalIn(idList);
        productOutstockBillQuery.or().andWarehousePrincipalIn(idList);
        if (productOutstockBillDAO.countByExample(productOutstockBillQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_PRODUCT_OUTSTOCK);
        }
        ProductOutstockBillProductQuery productOutstockBillProductQuery = new ProductOutstockBillProductQuery();
        productOutstockBillProductQuery.or().andPrincipalIn(idList);
        if (productOutstockBillProductDAO.countByExample(productOutstockBillProductQuery) > 0) {
            throw new BadRequestException(ADMIN_REFER_BY_PRODUCT_OUTSTOCK);
        }

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

    /**
     * 更新用户信息
     *
     * 进行必要的检查：是否为系统默认用户
     * 更新主表信息：admin
     * 更新关联的从表信息：admin_role
     * 添加日志信息：LogType.ADMIN, Operate.UPDATE
     *
     * @param adminId
     * @param trueName
     * @param closed
     * @param mobile
     * @param roleIdList
     * @return
     */
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

    /**
     * 更新用户账号状态信息
     *
     * 进行必要的检查：是否为系统默认用户
     * 更新主表信息：admin
     * 更新关联的从表信息：暂无
     * 添加日志信息：LogType.ADMIN, Operate.UPDATE
     *
     * @param idList
     * @param closed
     */
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
    private static final String ADMIN_REFER_BY_MATERIAL_INSTOCK = "用户被物料入库单引用";
    private static final String ADMIN_REFER_BY_MATERIAL_OUTSTOCK = "用户被物料出库单引用";
    private static final String ADMIN_REFER_BY_PRODUCT_INSTOCK = "用户被货品入库单引用";
    private static final String ADMIN_REFER_BY_PRODUCT_OUTSTOCK = "用户被货品出库单引用";
    private static final String ADMIN_REFER_BY_LOG = "用户被日志引用";
    private static final String SYSTEM_ADMIN_OPERATION_DENIED = "系统默认用户不可操作";

}
