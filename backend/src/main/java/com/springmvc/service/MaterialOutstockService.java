package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.MaterialOutstockBillMaterialDAO;
import com.springmvc.dao.MaterialOutstockBillDAO;
import com.springmvc.dao.MaterialOutstockBillMaterialDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.MaterialOutstockBill;
import com.springmvc.dto.MaterialOutstockBillMaterial;
import com.springmvc.dto.PageMode;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.MaterialOutstockBillMaterialQuery;
import com.springmvc.pojo.MaterialOutstockBillQuery;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service("MaterialOutstockService")
@Transactional

public class MaterialOutstockService extends BaseService {
    @Resource
    private MaterialDAO materialDAO;
    @Resource
    private MaterialOutstockBillDAO materialOutstockBillDAO;
    @Resource
    private MaterialOutstockBillMaterialDAO materialOutstockBillMaterialDAO;

    /**
     * 删除物料出库单
     * @param billIdList
     */
    public void removeMaterialOutstockBill(List<Integer> billIdList)//修改一下删除
    {
        MaterialOutstockBillQuery materialOutstockBillQuery=new MaterialOutstockBillQuery();
        //检查物料出库单状态是否为待审核。
        materialOutstockBillQuery=new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillStateEqualTo(1);
        if(materialOutstockBillDAO.countByExample(materialOutstockBillQuery)<=0)
        {
            throw new BadRequestException(MATERIAL_BILL_STATE_WRONG);
        }
        //删除物料出库单
        materialOutstockBillQuery=new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillIdIn(billIdList);
        materialOutstockBillDAO.deleteByExample(materialOutstockBillQuery);
        //删除关联
        MaterialOutstockBillMaterialQuery materialOutstockBillMaterialQuery=new MaterialOutstockBillMaterialQuery();
        materialOutstockBillMaterialQuery.or().andBillIdIn(billIdList);
        materialOutstockBillMaterialDAO.deleteByExample(materialOutstockBillMaterialQuery);
        //添加日志
        addLog(LogType.MATERIAL_OUTSTOCK,Operate.REMOVE,billIdList);


    }

    /**
     * 添加物料出库单
     * @param bill_no
     * @param to_principal
     * @param warehouse_principal
     * @param related_bill
     * @param remark
     * @param state
     * @param materialIdList
     * @return
     */
    public MaterialOutstockBill addMaterialOutstockBill(String bill_no, Integer to_principal,
                                                        Integer warehouse_principal,
                                                        Integer related_bill, String remark,
                                                        Integer state, List<Integer> materialIdList)
    {
        MaterialOutstockBillQuery materialOutstockBillQuery=new MaterialOutstockBillQuery();
        Admin loginAdmin= RequestUtils.getLoginAdminFromCache();
        MaterialOutstockBill materialOutstockBill=new MaterialOutstockBill();
        materialOutstockBill.setBillNo(bill_no);
        materialOutstockBill.setToPrincipal(to_principal);
        materialOutstockBill.setWarehousePrincipal(warehouse_principal);
        materialOutstockBill.setBillTime(new Date());
        materialOutstockBill.setMaterialWhereabouts(1);
        materialOutstockBill.setRelatedBill(related_bill);
        materialOutstockBill.setBillState(state);
        materialOutstockBill.setRemark(remark);
        materialOutstockBill.setAuditAt(null);
        materialOutstockBill.setAuditBy(null);
        materialOutstockBill.setFinishAt(null);
        materialOutstockBill.setFinishBy(null);
        materialOutstockBill.setCreateAt(new Date());
        materialOutstockBill.setCreateBy(loginAdmin.getAdminId());
        materialOutstockBill.setUpdateAt(new Date());
        materialOutstockBill.setUpdateBy(loginAdmin.getAdminId());
        materialOutstockBillDAO.insertSelective(materialOutstockBill);
        for(Integer materialId:materialIdList)
        {
            MaterialOutstockBillMaterial materialOutstockBillMaterial=new MaterialOutstockBillMaterial();
            materialOutstockBillMaterial.setBillId(materialOutstockBill.getBillId());
            materialOutstockBillMaterial.setMaterialId(materialId);
            materialOutstockBillMaterialDAO.insertSelective(materialOutstockBillMaterial);
        }
        addLog(LogType.MATERIAL_OUTSTOCK,Operate.ADD,materialOutstockBill.getBillId());
        return getMaterialOutstockBillById(materialOutstockBill.getBillId());
    }

    //TODO:领料单生成的出库单不可编辑？是否意味着不可修改?
    /**
     * 更新物料出库单
     * @param bill_no
     * @param to_principal
     * @param warehouse_principal
     * @param related_bill
     * @param bill_state
     * @param remark
     * @param materialIdList
     * @return
     */
    public MaterialOutstockBill updateMaterialOutstockBill(Integer bill_id, String bill_no, Integer to_principal,
                                                           Integer warehouse_principal,
                                                           Integer related_bill,Integer bill_state,
                                                           String remark,List<Integer> materialIdList)
    {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        MaterialOutstockBillQuery materialOutstockBillQuery=new MaterialOutstockBillQuery();
        materialOutstockBillQuery.or().andBillStateEqualTo(1);
        if(materialOutstockBillDAO.countByExample(materialOutstockBillQuery)<=0)
        {
            throw new BadRequestException(MATERIAL_BILL_STATE_WRONG);

        }
        materialOutstockBillQuery=new MaterialOutstockBillQuery();



        //领料单生成的出库单不可编辑？是否意味着不可修改？
//        materialOutstockBillQuery.or().andMaterialWhereaboutsEqualTo(1);
//        if(materialOutstockBillDAO.countByExample(materialOutstockBillQuery)>=1)
//        {
//            throw new BadRequestException(MATERIAL_WHEREABOUS_MODIFY_ERROR);
//
//        }

        MaterialOutstockBill materialOutstockBill=new MaterialOutstockBill();

        materialOutstockBill.setBillId(bill_id);
        materialOutstockBill.setBillNo(bill_no);
        materialOutstockBill.setToPrincipal(to_principal);
        materialOutstockBill.setWarehousePrincipal(warehouse_principal);
        materialOutstockBill.setRelatedBill(related_bill);
        materialOutstockBill.setBillState(bill_state);
        materialOutstockBill.setRemark(remark);
//    完成时间和审核时间为空
        if(bill_state==1)
        {
            materialOutstockBill.setAuditAt(null);
            materialOutstockBill.setAuditBy(null);
            materialOutstockBill.setFinishAt(null);
            materialOutstockBill.setFinishBy(null);
        }

        else if(bill_state==2)
        {
            materialOutstockBill.setAuditAt(new java.util.Date());
            materialOutstockBill.setAuditBy(loginAdmin.getAdminId());
            materialOutstockBill.setFinishAt(null);
            materialOutstockBill.setFinishBy(null);

        }
        else
        {
            materialOutstockBill.setFinishAt(new java.util.Date());
            materialOutstockBill.setFinishBy(loginAdmin.getAdminId());

        }


        materialOutstockBill.setUpdateAt(new java.util.Date());
        materialOutstockBill.setUpdateBy(loginAdmin.getAdminId());

        materialOutstockBillDAO.updateByPrimaryKeySelective(materialOutstockBill);


//        删除关联
        MaterialOutstockBillMaterialQuery materialOutstockBillMaterialQuery=new MaterialOutstockBillMaterialQuery();
        materialOutstockBillMaterialQuery.or().andBillIdEqualTo(materialOutstockBill.getBillId());
        materialOutstockBillMaterialDAO.deleteByExample(materialOutstockBillMaterialQuery);

        MaterialOutstockBillMaterial materialOutstockBillMaterial;
//        新增关联
        for(Integer materialId : materialIdList)
        {
            materialOutstockBillMaterial=new MaterialOutstockBillMaterial();
            materialOutstockBillMaterial.setBillId(materialOutstockBill.getBillId());
            materialOutstockBillMaterial.setMaterialId(materialId);
            materialOutstockBillMaterialDAO.insertSelective(materialOutstockBillMaterial);

        }
        //    日志记录
        addLog(LogType.MATERIAL_OUTSTOCK,Operate.UPDATE,materialOutstockBill.getBillId());
        return getMaterialOutstockBillById(materialOutstockBill.getBillId());

    }

    /**
     * 按照id获取物料出库单
     * @param billId
     * @return
     */
    public MaterialOutstockBill getMaterialOutstockBillById(int billId)
    {
        MaterialOutstockBill materialOutstockBill=materialOutstockBillDAO.selectByPrimaryKey(billId);
        return  materialOutstockBill;
    }

    /**
     * 搜索
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @param materialOutstockState
     * @return
     */
    public PageMode<MaterialOutstockBill> pageMaterialOutstockBill(Integer current, Integer limit, String sortColumn,
                                                                   String sort, String searchKey,
                                                                   Integer materialOutstockState)
    {
        MaterialOutstockBillQuery materialOutstockBillQuery=new MaterialOutstockBillQuery();
        materialOutstockBillQuery.setOffset((current-1)*limit);
        materialOutstockBillQuery.setLimit(limit);
        if (!ParamUtils.isNull(sortColumn)) {
            materialOutstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }
        //查询订单编号
        MaterialOutstockBillQuery.Criteria criteria=materialOutstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andBillNoLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(materialOutstockState) && !materialOutstockState.equals(-1)) {
            criteria.andBillStateEqualTo(materialOutstockState);
        }

        //查询备注（remark）
        criteria=materialOutstockBillQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andRemarkLike("%" + searchKey + "%");
        }
        if (!ParamUtils.isNull(materialOutstockState) && !materialOutstockState.equals(-1)) {
            criteria.andBillStateEqualTo(materialOutstockState);
        }

        List<MaterialOutstockBill> result=materialOutstockBillDAO.selectByExample(materialOutstockBillQuery);
        return new PageMode<MaterialOutstockBill>(result,materialOutstockBillDAO.countByExample(materialOutstockBillQuery));
    }


    private static final String MATERIAL_BILL_STATE_WRONG="物料出库单状态不符合要求";//只有待审核可修改和可删除
    private static final String MATERIAL_WHEREABOUS_MODIFY_ERROR="领料单生成的出库单不可修改";//?

}
