package com.springmvc.service;

import com.springmvc.dao.*;
import com.springmvc.dto.DrawMaterialBill;
import com.springmvc.dto.DrawMaterialBillMaterial;
import com.springmvc.dto.Material;
import com.springmvc.dto.MaterialOutstockBill;
import com.springmvc.pojo.DrawMaterialBillMaterialQuery;
import com.springmvc.pojo.DrawMaterialBillQuery;
import com.springmvc.pojo.MaterialOutstockBillQuery;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("DrawMaterialService")
@Transactional
public class DrawMaterialService extends BaseService {

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private DrawMaterialBillDAO drawMaterialBillDAO;

    @Resource
    private DrawMaterialBillMaterialDAO drawMaterialBillMaterialDAO;

    @Resource
    private MaterialOutstockBillDAO materialOutstockBillDAO;

    /**
     * 查询领料单信息（单个）
     *
     * @param billId
     * @return
     */
    public DrawMaterialBill getById(Integer billId) {
        DrawMaterialBill drawMaterialBill = drawMaterialBillDAO.selectByPrimaryKey(billId);

        DrawMaterialBillMaterialQuery drawMaterialBillMaterialQuery = new DrawMaterialBillMaterialQuery();
        DrawMaterialBillMaterialQuery.Criteria criteria = drawMaterialBillMaterialQuery.or();
        criteria.andBillIdEqualTo(drawMaterialBill.getBillId());
        List<DrawMaterialBillMaterial> result = drawMaterialBillMaterialDAO.selectByExample(drawMaterialBillMaterialQuery);
        for(DrawMaterialBillMaterial item: result) {
            Material material = materialDAO.selectByPrimaryKey(item.getMaterialId());
            if (material != null) {
                item.setMaterialNo(material.getMaterialNo());
                item.setMaterialName(material.getMaterialName());
            }
        }
        drawMaterialBill.setMaterialList(result);
        return drawMaterialBill;
    }

    /**
     * 获取领料单表的所有信息
     *
     * @return 返回列表
     */
    public List<DrawMaterialBill> getList(Integer state, Boolean onlyNotOutstock) {
        DrawMaterialBillQuery drawMaterialBillQuery = new DrawMaterialBillQuery();
        DrawMaterialBillQuery.Criteria criteria = drawMaterialBillQuery.or();
        if (!ParamUtils.isNull(state) && !state.equals(-1)) {
            criteria.andBillStateEqualTo(state);
        }
        if (!ParamUtils.isNull(onlyNotOutstock) && onlyNotOutstock) {
            Set<Integer> billIdSet = new HashSet<Integer>();
            List<MaterialOutstockBill> billList = materialOutstockBillDAO.selectByExample(new MaterialOutstockBillQuery());
            for (MaterialOutstockBill bill: billList) {
                if (bill.getMaterialWhereabouts().equals(1) && !ParamUtils.isNull(bill.getRelatedBill())) {
                    billIdSet.add(bill.getRelatedBill());
                }
            }
            if (billIdSet.size() > 0) {
                criteria.andBillIdNotIn(new ArrayList<Integer>(billIdSet));
            }
        }
        return drawMaterialBillDAO.selectByExample(drawMaterialBillQuery);
    }
}
