package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.MaterialInstockBillDAO;
import com.springmvc.dao.MaterialInstockBillMaterialDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.MaterialInstockBill;
import com.springmvc.dto.MaterialInstockBillMaterial;
import com.springmvc.dto.PageMode;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.MaterialInstockBillMaterialQuery;
import com.springmvc.pojo.MaterialInstockBillQuery;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("MaterialInstockService")
@Transactional
public class MaterialInstockService extends BaseService {

    @Resource
    private MaterialDAO materialDAO;

    @Resource
    private MaterialInstockBillDAO materialInstockBillDAO;

    @Resource
    private MaterialInstockBillMaterialDAO materialInstockBillMaterialDAO;

    /**
     * 删除物料入库单
     *
     * @param billIdList
     */

public void removeMaterialBill(List<Integer> billIdList)//修改一下删除
{
    //检查物料入库单是否存在
//    MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
//    materialInstockBillQuery.or().andBillIdEqualTo(billId);
//    if(materialInstockBillDAO.countByExample(materialInstockBillQuery)<=0)
//    {
//        throw new BadRequestException(MATERIAL_BILL_NOT_EXISTED);
//    }
    MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
    //检查物料入库单状态是否为待审核
//    materialInstockBillQuery=new MaterialInstockBillQuery();
    materialInstockBillQuery.or().andBillStateEqualTo(1);
    if(materialInstockBillDAO.countByExample(materialInstockBillQuery)<=0)
    {
        throw new BadRequestException(MATERIAL_BILL_STATE_WRONG);
    }
    //删除物料入库单
    materialInstockBillQuery=new MaterialInstockBillQuery();
    materialInstockBillQuery.or().andBillIdIn(billIdList);
    materialInstockBillDAO.deleteByExample(materialInstockBillQuery);
    //删除关联
    MaterialInstockBillMaterialQuery materialInstockBillMaterialQuery=new MaterialInstockBillMaterialQuery();
    materialInstockBillMaterialQuery.or().andBillIdIn(billIdList);
    materialInstockBillMaterialDAO.deleteByExample(materialInstockBillMaterialQuery);
    //添加日志
    addLog(LogType.MATERIAL_INSTOCK,Operate.REMOVE,billIdList);




}


    /**
     * 采购入库。
     * @param bill_no
     * @param from_principal
     * @param warehouse_principal
     * @param related_bill
     * @param remark
     * @return
     */

public MaterialInstockBill addMaterialInstockBillByPurchase(String bill_no, Integer from_principal, Integer warehouse_principal,
                                                  Integer related_bill, String remark, Integer state, List<Integer> materialIdList)
{
    MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
    Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
    MaterialInstockBill materialInstockBill=new MaterialInstockBill();
    materialInstockBill.setBillNo(bill_no);
    materialInstockBill.setFromPrincipal(from_principal);
    materialInstockBill.setWarehousePrincipal(warehouse_principal);
//    materialInstockBill.setBillTime(bill_time);
//    物料入库单创建时间，是否和下面的创建时间重复？
    materialInstockBill.setBillTime(new Date());
//    materialInstockBill.setMaterialSource(material_source);
//    采购入库
    materialInstockBill.setMaterialSource(2);
    materialInstockBill.setRelatedBill(related_bill);
//    materialInstockBill.setBillState(bill_state);
//    待审核
    materialInstockBill.setBillState(state);
    materialInstockBill.setRemark(remark);
//    完成时间和审核时间为空
    materialInstockBill.setAuditAt(null);
    materialInstockBill.setAuditBy(null);
    materialInstockBill.setFinishAt(null);
    materialInstockBill.setFinishBy(null);

    materialInstockBill.setCreateAt(new java.util.Date());
    materialInstockBill.setCreateBy(loginAdmin.getAdminId());
    materialInstockBill.setUpdateAt(new java.util.Date());
    materialInstockBill.setUpdateBy(loginAdmin.getAdminId());

    materialInstockBillDAO.insertSelective(materialInstockBill);
//    新增关联
//    for(Integer materialId : materialIdList)
//    {
//        MaterialInstockBillMaterial materialInstockBillMaterial=new MaterialInstockBillMaterial();
//        materialInstockBillMaterial.setBillId(materialInstockBill.getBillId());
//        materialInstockBillMaterial.setMaterialId(materialId);
//        materialInstockBillMaterialDAO.insertSelective(materialInstockBillMaterial);
//
//    }

//    仓库位置
//    MaterialInstockBillMaterial materialInstockBillMaterial=new MaterialInstockBillMaterial();
//    materialInstockBillMaterialDAO.insertSelective(materialInstockBillMaterial);


//    日志记录
    addLog(LogType.MATERIAL_INSTOCK,Operate.ADD,materialInstockBill.getBillId());
    return getMaterialInstockBillById(materialInstockBill.getBillId());

}


//
//缺采购入库导入
//

    /**
     * 更新物料入库单
     *
     * @param bill_no
     * @param from_principal
     * @param warehouse_principal
     * @param related_bill
     * @param bill_state
     * @param remark
     * @param materialIdList
     * @return
     */

public MaterialInstockBill updateMateialInstockBill(Integer bill_id, String bill_no, Integer from_principal, Integer warehouse_principal,
                                                    Integer related_bill, Integer bill_state, String remark,List<Integer> materialIdList)
{
    Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
    MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
    materialInstockBillQuery.or().andBillStateEqualTo(1);
    if(materialInstockBillDAO.countByExample(materialInstockBillQuery)<=0)
    {
        throw new BadRequestException(MATERIAL_BILL_STATE_WRONG);

    }
    materialInstockBillQuery=new MaterialInstockBillQuery();
    materialInstockBillQuery.or().andMaterialSourceEqualTo(1);
    if(materialInstockBillDAO.countByExample(materialInstockBillQuery)>=1)//？
    {
        throw new BadRequestException(MATERIAL_SOURCE_MODIFY_ERROR);

    }
    MaterialInstockBill materialInstockBill=new MaterialInstockBill();

    materialInstockBill.setBillId(bill_id);
    materialInstockBill.setBillNo(bill_no);
    materialInstockBill.setFromPrincipal(from_principal);
    materialInstockBill.setWarehousePrincipal(warehouse_principal);
//    materialInstockBill.setMaterialSource(material_source);
    materialInstockBill.setRelatedBill(related_bill);
    materialInstockBill.setBillState(bill_state);
    materialInstockBill.setRemark(remark);
//    完成时间和审核时间为空
    if(bill_state==1)
    {
        materialInstockBill.setAuditAt(null);
        materialInstockBill.setAuditBy(null);
        materialInstockBill.setFinishAt(null);
        materialInstockBill.setFinishBy(null);
    }

    else if(bill_state==2)
    {
        materialInstockBill.setAuditAt(new java.util.Date());
        materialInstockBill.setAuditBy(loginAdmin.getAdminId());
        materialInstockBill.setFinishAt(null);
        materialInstockBill.setFinishBy(null);

    }
    else
    {
        materialInstockBill.setFinishAt(new java.util.Date());
        materialInstockBill.setFinishBy(loginAdmin.getAdminId());
    }


    materialInstockBill.setUpdateAt(new java.util.Date());
    materialInstockBill.setUpdateBy(loginAdmin.getAdminId());

    materialInstockBillDAO.updateByPrimaryKeySelective(materialInstockBill);
//        仓库位置
    MaterialInstockBillMaterial materialInstockBillMaterial=new MaterialInstockBillMaterial();
    //materialInstockBillMaterialDAO.insertSelective(materialInstockBillMaterial);

//        删除关联
    MaterialInstockBillMaterialQuery materialInstockBillMaterialQuery=new MaterialInstockBillMaterialQuery();
    materialInstockBillMaterialQuery.or().andBillIdEqualTo(materialInstockBill.getBillId());
    materialInstockBillMaterialDAO.deleteByExample(materialInstockBillMaterialQuery);

//        新增关联
    for(Integer materialId : materialIdList)
    {
        materialInstockBillMaterial=new MaterialInstockBillMaterial();
        materialInstockBillMaterial.setBillId(materialInstockBill.getBillId());
        materialInstockBillMaterial.setMaterialId(materialId);
        materialInstockBillMaterialDAO.insertSelective(materialInstockBillMaterial);

    }
    //    日志记录
    addLog(LogType.MATERIAL_INSTOCK,Operate.UPDATE,materialInstockBill.getBillId());
    return getMaterialInstockBillById(materialInstockBill.getBillId());

}

    /**
     * 按照ID搜索物料入库单。
     * @param billId
     * @return
     */
    public MaterialInstockBill getMaterialInstockBillById(int billId)
{
    MaterialInstockBill materialInstockBill=materialInstockBillDAO.selectByPrimaryKey(billId);
    return materialInstockBill;



}

    /**
     * 搜索
     * @param current
     * @param limit
     * @param sortColumn
     * @param sort
     * @param searchKey
     * @param materialInstockState
     * @return
     */
    public PageMode<MaterialInstockBill> pageMaterialInstockBill(Integer current, Integer limit, String sortColumn, String sort,
                                                             String searchKey, Integer materialInstockState)
{
    MaterialInstockBillQuery materialInstockBillQuery=new MaterialInstockBillQuery();
    materialInstockBillQuery.setOffset((current-1)*limit);
    materialInstockBillQuery.setLimit(limit);
    if (!ParamUtils.isNull(sortColumn)) {
        materialInstockBillQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
    }
    //查询订单编号
    MaterialInstockBillQuery.Criteria criteria=materialInstockBillQuery.or();
    if (!ParamUtils.isNull(searchKey)) {
        criteria.andBillNoLike("%" + searchKey + "%");
    }
    if (!ParamUtils.isNull(materialInstockState) && !materialInstockState.equals(-1)) {
        criteria.andBillStateEqualTo(materialInstockState);
    }

    //查询备注（remark）
    criteria=materialInstockBillQuery.or();
    if (!ParamUtils.isNull(searchKey)) {
        criteria.andRemarkLike("%" + searchKey + "%");
    }
    if (!ParamUtils.isNull(materialInstockState) && !materialInstockState.equals(-1)) {
        criteria.andBillStateEqualTo(materialInstockState);
    }

    //返回warehouse
    List<MaterialInstockBill> result=materialInstockBillDAO.selectByExample(materialInstockBillQuery);
    return new PageMode<MaterialInstockBill>(result,materialInstockBillDAO.countByExample(materialInstockBillQuery));





}
private static final String MATERIAL_BILL_NOT_EXISTED="物料入库单不存在";
private static final String MATERIAL_BILL_STATE_WRONG="物料入库单状态不符合要求";//只有待审核可修改和可删除
private static final String MATERIAL_SOURCE_MODIFY_ERROR="退料入库的入库单无法修改";
}
