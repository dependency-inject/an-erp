package com.springmvc.service;

import com.springmvc.dao.LogDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.Log;
import com.springmvc.utils.RequestUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class BaseService {

    @Resource
    protected LogDAO logDAO;

    public void addLog(LogType logType, Operate operate, Integer id) {
        addLog(logType, operate, Collections.singletonList(id));
    }

    public void addLog(LogType logType, Operate operate, List<Integer> idList) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        addLog(logType, operate, idList, loginAdmin.getAdminId());
    }

    /**
     * 有些情况下（如登录时），loginAdmin还未写入Session，由调用者传入loginAdminId
     * @param logType
     * @param operate
     * @param id
     * @param loginAdminId
     */
    public void addLog(LogType logType, Operate operate, Integer id, Integer loginAdminId) {
        addLog(logType, operate, Collections.singletonList(id), loginAdminId);
    }

    /**
     * 有些情况下（如登录时），loginAdmin还未写入Session，由调用者传入loginAdminId
     * @param logType
     * @param operate
     * @param idList
     * @param loginAdminId
     */
    public void addLog(LogType logType, Operate operate, List<Integer> idList, Integer loginAdminId) {
        StringBuilder sb = new StringBuilder();
        sb.append(operate.operate).append(logType.type);
        sb.append("。ID：").append(idList);

        Log log = new Log();
        log.setAdminId(loginAdminId);
        log.setLogType(logType.type);
        log.setOperate(operate.operate);
        log.setLogTime(new Date());
        log.setDescription(sb.toString());
        // TODO: ip、sql
//        log.setIp();
        log.setCreateAt(new Date());
        log.setCreateBy(loginAdminId);
        logDAO.insertSelective(log);
    }

    public enum LogType {
        PRODUCT("货品"), PRODUCT_CATEGORY("货品分类"), MATERIAL("物料"), MATERIAL_CATEGORY("物料分类"), PRODUCT_MATERIAL("货品物料组成"),
        ORDER("订单"), CLIENT("客户"),
        DRAW_MATERIAL_BILL("领料单"), RETURN_MATERIAL_BILL("退料单"),
        WAREHOUSE("仓库"), PRODUCT_OUTSTOCK("货品出库单"), PRODUCT_INSTOCK("货品入库单"), MATERIAL_OUTSTOCK("物料出库单"), MATERIAL_INSTOCK("物料入库单"),
        SUPPLIER("供应商"), SUPPLIER_MATERIAL("供应商供料"),
        MATERIAL_COST("物料成本"),
        ADMIN("用户"), ROLE("角色"), ROLE_PERMISSION("角色权限"), SYSTEM("系统");

        public final String type;

        LogType(String type) {
            this.type = type;
        }
    }

    public enum Operate {
        ADD("新增"), UPDATE("更新"), REMOVE("删除"),
        AUDIT("审核"), UNAUDIT("反审核"), PRODUCE("启动生产"), FINISH("完成"), CANCEL("取消"), LOGIN("登录");

        public final String operate;

        Operate(String operate) {
            this.operate = operate;
        }
    }
}
