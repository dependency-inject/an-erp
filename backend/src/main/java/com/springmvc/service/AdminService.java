package com.springmvc.service;

import com.springmvc.dao.AdminDAO;
import com.springmvc.pojo.Admin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("AdminService")
public class AdminService {

    @Resource
    private AdminDAO adminDAO;


    public Admin getAdmin(int id){
        Admin a=this.adminDAO.selectByPrimaryKey(id);
        if(a!=null){
            return a;
        }
        return null;
    }

    public int insertAdmin(Admin admin){
        return this.adminDAO.insert(admin);
    }
}
