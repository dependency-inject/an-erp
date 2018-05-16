package com.springmvc.service;

import com.springmvc.dao.AdminDAO;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.Admin;
import com.springmvc.pojo.AdminQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("AdminService")
public class AdminService {

    @Resource
    private AdminDAO adminDAO;

    public Admin addAdmin(String loginName, String trueName, Boolean closed, String mobile) throws BadRequestException {
        AdminQuery query = new AdminQuery();
        query.or().andLoginNameEqualTo(loginName);
        if (adminDAO.countByExample(query) > 0) {
            throw new BadRequestException("登录名已存在");
        }

        Admin admin = new Admin();
        admin.setLoginName(loginName);
        admin.setTrueName(trueName);
        admin.setClosed(closed);
        admin.setMobile(mobile);
        admin.setSysDefault(false);
        //
        adminDAO.insertSelective(admin);

        return getAdminById(admin.getAdminId());
    }

    public Admin getAdminById(int adminId) {
        Admin admin = adminDAO.selectByPrimaryKey(adminId);
        admin.setPassword(null);
        return admin;
    }
}
