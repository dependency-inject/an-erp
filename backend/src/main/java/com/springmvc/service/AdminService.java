package com.springmvc.service;

import com.springmvc.dao.AdminMapper;
import com.springmvc.pojo.Admin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("AdminService")

public class AdminService {
    @Resource
    private AdminMapper adminMapper;


    public Admin getAdmin(int id){
        Admin a=this.adminMapper.selectByPrimaryKey(id);
        if(a!=null){
            return a;
        }
        return null;
    }

    public int insertAdmin(Admin admin){
        return this.adminMapper.insert(admin);
    }
}
