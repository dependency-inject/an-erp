package com.springmvc.service;

import com.springmvc.dao.*;
import com.springmvc.dto.Material;
import com.springmvc.dto.MaterialInstockBill;
import com.springmvc.dto.ReturnMaterialBill;
import com.springmvc.dto.ReturnMaterialBillMaterial;
import com.springmvc.pojo.MaterialInstockBillQuery;
import com.springmvc.pojo.ReturnMaterialBillMaterialQuery;
import com.springmvc.pojo.ReturnMaterialBillQuery;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("ReturnMaterialService")
@Transactional
public class ReturnMaterialService extends BaseService {

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private ReturnMaterialBillDAO returnMaterialBillDAO;

    @Resource
    private ReturnMaterialBillMaterialDAO returnMaterialBillMaterialDAO;

    @Resource
    private MaterialInstockBillDAO materialInstockBillDAO;

    /**
     * 查询退料单信息（单个）
     *
     * @param billId
     * @return
     */
    public ReturnMaterialBill getById(Integer billId) {
        ReturnMaterialBill returnMaterialBill = returnMaterialBillDAO.selectByPrimaryKey(billId);

        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery = new ReturnMaterialBillMaterialQuery();
        ReturnMaterialBillMaterialQuery.Criteria criteria = returnMaterialBillMaterialQuery.or();
        criteria.andBillIdEqualTo(returnMaterialBill.getBillId());
        List<ReturnMaterialBillMaterial> result = returnMaterialBillMaterialDAO.selectByExample(returnMaterialBillMaterialQuery);
        for(ReturnMaterialBillMaterial item: result) {
            Material material = materialDAO.selectByPrimaryKey(item.getMaterialId());
            if (material != null) {
                item.setMaterialNo(material.getMaterialNo());
                item.setMaterialName(material.getMaterialName());
            }
        }
        returnMaterialBill.setMaterialList(result);
        return returnMaterialBill;
    }

    /**
     * 获取退料单表的所有信息
     *
     * @return 返回列表
     */
    public List<ReturnMaterialBill> getList(Integer state, Boolean onlyNotInstock) {
        ReturnMaterialBillQuery returnMaterialBillQuery = new ReturnMaterialBillQuery();
        ReturnMaterialBillQuery.Criteria criteria = returnMaterialBillQuery.or();
        if (!ParamUtils.isNull(state) && !state.equals(-1)) {
            criteria.andBillStateEqualTo(state);
        }
        if (!ParamUtils.isNull(onlyNotInstock) && onlyNotInstock) {
            Set<Integer> billIdSet = new HashSet<Integer>();
            List<MaterialInstockBill> billList = materialInstockBillDAO.selectByExample(new MaterialInstockBillQuery());
            for (MaterialInstockBill bill: billList) {
                if (bill.getMaterialSource().equals(1) && !ParamUtils.isNull(bill.getRelatedBill())) {
                    billIdSet.add(bill.getRelatedBill());
                }
            }
            if (billIdSet.size() > 0) {
                criteria.andBillIdNotIn(new ArrayList<Integer>(billIdSet));
            }
        }
        return returnMaterialBillDAO.selectByExample(returnMaterialBillQuery);
    }
}
