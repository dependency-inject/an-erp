package com.springmvc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springmvc.dao.MaterialDAO;
import com.springmvc.dao.ReturnMaterialBillDAO;
import com.springmvc.dao.ReturnMaterialBillMaterialDAO;
import com.springmvc.dto.*;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.*;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("DevelopmentReturnService")
@Transactional
public class DevelopmentReturnService extends BaseService {
    @Resource
    private ReturnMaterialBillDAO returnMaterialBillDAO;
    @Resource
    private ReturnMaterialBillMaterialDAO returnMaterialBillMaterialDAO;
    @Resource
    private MaterialDAO materialDAO;
    /**
     * 当前退料表，新建一个
     * drawmaterialbill 来存储每次增加的从表信息，然后添加完毕之后一次性的打入数据库
     */
    private ReturnMaterialBill current =new ReturnMaterialBill();
    /**
     * 为新建的退料单添加从表信息
     * @param material_id 物料ID
     * @param quantity 物料数量
     * @param remark
     */
    public void addMaterial(int material_id,int quantity,String remark){
        boolean addSuccess=this.current.addListStore(material_id,quantity,remark);
        if(!addSuccess){
            throw new BadRequestException("material is already here");
        }
    }
    /**
     * 当前退料单移除material（没有到数据库中）
     * @param material_id 被移除的material id号
     */
    public void remove(int material_id){
        this.current.removeListStore(material_id);
    }
    /**
     * 取消订单
     * 当前订单置为空
     */
    public void cancelBill(){
        this.current=new ReturnMaterialBill();
    }

    /**
     * 增加单据到数据库，包括子表
     * @param billno 单据编号
     * @param reason 退料原因 1 生产,2 开发
     *  创始人，直接获取当前的user
     */
    public ReturnMaterialBill add2Database(String billno,String remark,int reason){
        ReturnMaterialBill toAdd=new ReturnMaterialBill();
        Admin admin = RequestUtils.getLoginAdminFromCache();
        toAdd.setCreateBy(admin.getAdminId());
        toAdd.setBillNo(billno);
        toAdd.setBillState(1);//默认为待审核状态
        toAdd.setReturnReason(reason);
        toAdd.setRemark(remark);
        toAdd.setFromPrincipal(admin.getAdminId());
        Date now=new Date();
        toAdd.setBillTime(now);
        toAdd.setWarehousePrincipal(admin.getAdminId());
        this.returnMaterialBillDAO.insert(toAdd);
        int billid=toAdd.getBillId();
        int itemNum=this.current.getSize();
        for(int i=0;i<itemNum;i++){
            ReturnMaterialBillMaterial tmp=new ReturnMaterialBillMaterial();
            tmp.setBillId(billid);
            tmp.setMaterialId(this.current.getMaterialid(i));
            tmp.setQuantity(this.current.getQuantity(i));
            tmp.setRemark(this.current.getRemark(i));
            this.returnMaterialBillMaterialDAO.insert(tmp);
        }
        addLog(LogType.ADMIN, Operate.ADD, admin.getAdminId());
        return toAdd;
    }

    /**
     * 删除一堆退料单
     * @param bill_id 删除的退料单id
     */
    public void deleteBILL(List<Integer> bill_id){
        // 检查是否被log引用,直接粘贴过来的
        System.out.println("deleteBILL");
        //先删除主表
        int l=bill_id.size();
        System.out.print(l);
        for(int i=0;i<l;i++){
            ReturnMaterialBill tmp=returnMaterialBillDAO.selectByPrimaryKey(bill_id.get(i));
            if(tmp.getBillState()==2){
                //已审核，无法更改
                continue;
            }
            else{
                returnMaterialBillDAO.deleteByPrimaryKey(bill_id.get(i));
                System.out.println("deletemain");
                returnMaterialBillMaterialDAO.deleteByPrimaryKey(bill_id.get(i));
                System.out.println("delete small");

            }
        }
    }

    /**
     * 删除一堆（从表）物料（从数据库中）
     * 已审核状态不允许更改
     * @param bill_id 退料单id
     * @param material_id 物料单ID
     */
    public void deleteMaterial(int bill_id,List<Integer>material_id){
        ReturnMaterialBill target=returnMaterialBillDAO.selectByPrimaryKey(bill_id);
        if(target.getBillState()==2)
            return;
        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery =new ReturnMaterialBillMaterialQuery();
        returnMaterialBillMaterialQuery.or().andBillIdEqualTo(bill_id).andMaterialIdIn(material_id);
        returnMaterialBillMaterialDAO.deleteByExample(returnMaterialBillMaterialQuery);
    }
    /**
     * 更改退料单
     * @param bill_id 被刚改的退料单
     * @param material_id 物料
     * @param quantity 物料数量
     * @param remark 备注
     */
    public void changeMaterial(int bill_id,int material_id,int quantity,String remark){
        ReturnMaterialBillMaterial tmp=new ReturnMaterialBillMaterial();
        tmp.setBillId(bill_id);
        tmp.setMaterialId(material_id);
        tmp.setQuantity((quantity));
        tmp.setRemark(remark);
        this.returnMaterialBillMaterialDAO.insert(tmp);

    }
    /**
     *  这个函数主要是为了前端提供
     *  是否可以更改退料单中具体物料
     *  前端就可以直接变为不可更改状态
     * @param bill_id 物料单ID
     * @return false 不可以 true 可以
     */
    public boolean changeMaterialItem(int bill_id){
        ReturnMaterialBill cu=returnMaterialBillDAO.selectByPrimaryKey(bill_id);
        if(cu.getBillState()==2)
            return false;
        int privilage=cu.getReturnReason();
        if (privilage==1)
            return false;
        else return true;
    }

    /**
     * 查询具体的退料单
     * @param bill_id
     * @return 退料单
     */
    public ReturnMaterialBill searchBill(int bill_id){
        ReturnMaterialBill cu=returnMaterialBillDAO.selectByPrimaryKey(bill_id);
        return cu;
    }

    /**
     *  取回一系列具体物料
     * @param bill_id
     * @param material_id
     * @return
     */
    public List<ReturnMaterialBillMaterial>searchBillMaterial(int bill_id,int material_id){
        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery =new ReturnMaterialBillMaterialQuery();
        returnMaterialBillMaterialQuery.or().andBillIdEqualTo(bill_id).andMaterialIdEqualTo(material_id);
        List<ReturnMaterialBillMaterial>cu=returnMaterialBillMaterialDAO.selectByExample(returnMaterialBillMaterialQuery);
        return cu;
    }

    /**
     * 更新备注
     * @param bill_id 退料单id
     * @param remark 新的备注
     */
    public void update(int bill_id,String remark){
        ReturnMaterialBill returnMaterialBill=new ReturnMaterialBill();
        returnMaterialBill.setRemark(remark);
        returnMaterialBill.setUpdateAt(new Date());
        returnMaterialBill.setUpdateBy(bill_id);
        ReturnMaterialBillQuery returnMaterialBillQuery=new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillIdEqualTo(bill_id);
        returnMaterialBillDAO.updateByExampleSelective(returnMaterialBill,returnMaterialBillQuery);
    }

    /**
     * 更改状态
     * @param bill_id
     * @param status，更改后的状态
     */
    public void setStatus(int bill_id,int status){
        ReturnMaterialBill returnMaterialBill=new ReturnMaterialBill();
        returnMaterialBill.setBillState(status);
        returnMaterialBill.setUpdateAt(new Date());
        returnMaterialBill.setUpdateBy(bill_id);
        ReturnMaterialBillQuery returnMaterialBillQuery=new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillIdEqualTo(bill_id);
        returnMaterialBillDAO.updateByExampleSelective(returnMaterialBill,returnMaterialBillQuery);

    }

    /**
     * 返回所有的内容
     * @return
     */
    public List<ReturnMaterialBill> getAll(){
        ReturnMaterialBillQuery returnMaterialBillQuery=new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andBillStateEqualTo(2);;
        List<ReturnMaterialBill>all=this.returnMaterialBillDAO.selectByExample(returnMaterialBillQuery);
        return all;
    }

    /**
     * 返回所有的具体退料单的具体item
     * @param bill_id
     * @return
     */
    public List<ReturnMaterialBillMaterial>getAllByID(int bill_id){
        ReturnMaterialBillMaterialQuery returnMaterialBillMaterialQuery=new ReturnMaterialBillMaterialQuery();
        returnMaterialBillMaterialQuery.or().andBillIdEqualTo(bill_id);
        List<ReturnMaterialBillMaterial>all=this.returnMaterialBillMaterialDAO.selectByExample(returnMaterialBillMaterialQuery);
        int len=all.size();
        for(int i=0;i<len;i++){
            Material tmp=materialDAO.selectByPrimaryKey(all.get(i).getMaterialId());
            all.get(i).name=tmp.getMaterialName();
        }
        return all;
    }

    /**
     * 获得具体内容
     * @param bill_id 单据编号
     * @return
     */
    public ReturnMaterialBill getById(int bill_id){
        return this.returnMaterialBillDAO.selectByPrimaryKey(bill_id);
    }

    /**
     * 以下的函数都是根据提供的值
     * 搜索符合条件的退料单
     * @param principal
     * @return
     */
    public List<ReturnMaterialBill> getByPrincipal(int principal){
        ReturnMaterialBillQuery returnMaterialBillQuery=new ReturnMaterialBillQuery();
        returnMaterialBillQuery.or().andAuditByEqualTo(principal);
        List<ReturnMaterialBill>all=this.returnMaterialBillDAO.selectByExample(returnMaterialBillQuery);
        return all;
    }

    /**
     * 根据前面的search 框传过来的map 进行检索，可能有空字符串，主要处理空字符串
     * @param search 传过来的serach 字符串，为json格式
     * @return 检索到的内容
     */
    public List<ReturnMaterialBill> getByMap(String search){
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(search);
        Iterator<String> keys = jsonObject.keySet().iterator();
        String key = null;
        Object value = null;
        while(keys.hasNext()){
            key = keys.next();
            value = jsonObject.get(key);
            map.put(key, value);
        }
        ReturnMaterialBillQuery returnMaterialBillQuery=new ReturnMaterialBillQuery();
        ReturnMaterialBillQuery.Criteria searc=returnMaterialBillQuery.createCriteria();
        searc.andReturnReasonEqualTo(2);
        String fromprincipal=map.get("fromPrincipal").toString();
        if(fromprincipal.length()!=0)
        {
            searc.andFromPrincipalEqualTo(Integer.parseInt(fromprincipal));
            System.out.println("principal");
        }
        String billTime=map.get("billTime").toString();
        JSONArray jsonArray = JSONArray.parseArray(billTime);
        String datestart=jsonArray.getString(0);
        String dateend=jsonArray.getString(1);
        if(dateend!=null&&datestart!=null){
            System.out.println("date");
            System.out.println("*************************");
            System.out.println(Long.parseLong(datestart));
            java.sql.Date billstart=new java.sql.Date(Long.parseLong(datestart));
            System.out.println(billstart);
            java.sql.Date billend=new java.sql.Date(Long.parseLong(dateend));
            searc.andBillTimeBetween(billstart,billend);
            System.out.println(billstart);
            System.out.println(billend);
            System.out.println("*************************");
        }
        String billno=map.get("billNo").toString();
        if(billno.length()!=0){
            searc.andBillNoEqualTo(billno);
            System.out.println("billno");
        }
        String billstate=map.get("billState").toString();
        if(billstate.trim().length()!=0){
            int state=Integer.parseInt(billstate);
            searc.andBillStateEqualTo(state);
            System.out.println("billstate");
        }
        List<ReturnMaterialBill>all=this.returnMaterialBillDAO.selectByExample(returnMaterialBillQuery);
        return all;
    }
}
