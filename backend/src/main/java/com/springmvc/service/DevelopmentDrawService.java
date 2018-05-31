package com.springmvc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springmvc.dao.DrawMaterialBillDAO;
import com.springmvc.dao.DrawMaterialBillMaterialDAO;
import com.springmvc.dao.MaterialDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.DrawMaterialBill;
import com.springmvc.dto.DrawMaterialBillMaterial;
import com.springmvc.dto.Material;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.DrawMaterialBillMaterialQuery;
import com.springmvc.pojo.DrawMaterialBillQuery;
import com.springmvc.pojo.LogQuery;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("DevelopmentDrawService")
@Transactional
public class DevelopmentDrawService extends BaseService {
    @Resource
    private DrawMaterialBillDAO drawMaterialBillDAO;
    @Resource
    private DrawMaterialBillMaterialDAO drawMaterialBillMaterialDAO;
    @Resource
    private MaterialDAO materialDAO;
    /**
     * 当前领料表，新建一个
     * drawmaterialbill 来存储每次增加的从表信息，然后添加完毕之后一次性的打入数据库
     */
    private  DrawMaterialBill current =new DrawMaterialBill();
    /**
     * 为新建的领料单添加从表信息
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
     * 当前领料单移除material（没有到数据库中）
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
        this.current=new DrawMaterialBill();
    }

    /**
     * 增加单据到数据库，包括子表
     * @param billno 单据编号
     * @param reason 领料原因 1 生产,2 开发
     *  创始人，直接获取当前的user
     */
    public DrawMaterialBill add2Database(String billno,String remark,int reason){
        DrawMaterialBill toAdd=new DrawMaterialBill();
        Admin admin = RequestUtils.getLoginAdminFromCache();
        toAdd.setCreateBy(admin.getAdminId());
        toAdd.setBillNo(billno);
        toAdd.setBillState(1);//默认为待审核状态
        toAdd.setDrawReason(reason);
        toAdd.setRemark(remark);
        toAdd.setToPrincipal(admin.getAdminId());
        Date now=new Date();
        toAdd.setBillTime(now);
        toAdd.setWarehousePrincipal(admin.getAdminId());
        this.drawMaterialBillDAO.insert(toAdd);
        int billid=toAdd.getBillId();
        int itemNum=this.current.getSize();
        for(int i=0;i<itemNum;i++){
            DrawMaterialBillMaterial tmp=new DrawMaterialBillMaterial();
            tmp.setBillId(billid);
            tmp.setMaterialId(this.current.getMaterialid(i));
            tmp.setQuantity(this.current.getQuantity(i));
            tmp.setRemark(this.current.getRemark(i));
            this.drawMaterialBillMaterialDAO.insert(tmp);
        }
        addLog(LogType.ADMIN, Operate.ADD, admin.getAdminId());
        return toAdd;
    }

    /**
     * 删除一堆领料单
     * @param bill_id 删除的领料单id
     */
    public void deleteBILL(List<Integer> bill_id){
        // 检查是否被log引用,直接粘贴过来的
        System.out.println("deleteBILL");
        //先删除主表
        int l=bill_id.size();
        System.out.print(l);
        for(int i=0;i<l;i++){
            DrawMaterialBill tmp=drawMaterialBillDAO.selectByPrimaryKey(bill_id.get(i));
            if(tmp.getBillState()==2){
                //已审核，无法更改
                continue;
            }
            else{
                drawMaterialBillDAO.deleteByPrimaryKey(bill_id.get(i));
                System.out.println("deletemain");
                drawMaterialBillMaterialDAO.deleteByPrimaryKey(bill_id.get(i));
                System.out.println("delete small");

            }
        }
    }

    /**
     * 删除一堆（从表）物料（从数据库中）
     * 已审核状态不允许更改
     * @param bill_id 领料单id
     * @param material_id 物料单ID
     */
    public void deleteMaterial(int bill_id,List<Integer>material_id){
        DrawMaterialBill target=drawMaterialBillDAO.selectByPrimaryKey(bill_id);
        if(target.getBillState()==2)
            return;
        DrawMaterialBillMaterialQuery drawMaterialBillMaterialQuery =new DrawMaterialBillMaterialQuery();
        drawMaterialBillMaterialQuery.or().andBillIdEqualTo(bill_id).andMaterialIdIn(material_id);
        drawMaterialBillMaterialDAO.deleteByExample(drawMaterialBillMaterialQuery);
    }

    /**
     * 更改领料单
     * @param bill_id 被刚改的领料单
     * @param material_id 物料
     * @param quantity 物料数量
     * @param remark 备注
     */
    public void changeMaterial(int bill_id,int material_id,int quantity,String remark){
        DrawMaterialBillMaterial tmp=new DrawMaterialBillMaterial();
        System.out.println("********************************");
        System.out.println(bill_id);
        System.out.println(material_id);
        System.out.println(quantity);
        System.out.println(remark);
        System.out.println("********************************");
        tmp.setBillId(bill_id);
        tmp.setMaterialId(material_id);
        tmp.setQuantity((quantity));
        tmp.setRemark(remark);
        this.drawMaterialBillMaterialDAO.insert(tmp);

    }
    /**
     *  这个函数主要是为了前端提供
     *  是否可以更改领料单中具体物料
     *  前端就可以直接变为不可更改状态
     * @param bill_id 物料单ID
     * @return false 不可以 true 可以
     */
    public boolean changeMaterialItem(int bill_id){
        DrawMaterialBill cu=drawMaterialBillDAO.selectByPrimaryKey(bill_id);
        if(cu.getBillState()==2)
            return false;
        int privilage=cu.getDrawReason();
        if (privilage==1)
            return false;
        else return true;
    }

    /**
     * 查询具体的领料单
     * @param bill_id
     * @return 领料单
     */
    public DrawMaterialBill searchBill(int bill_id){
        DrawMaterialBill cu=drawMaterialBillDAO.selectByPrimaryKey(bill_id);
        return cu;
    }

    /**
     *  取回一系列具体物料
     * @param bill_id
     * @param material_id
     * @return
     */
    public List<DrawMaterialBillMaterial>searchBillMaterial(int bill_id,int material_id){
        DrawMaterialBillMaterialQuery drawMaterialBillMaterialQuery =new DrawMaterialBillMaterialQuery();
        drawMaterialBillMaterialQuery.or().andBillIdEqualTo(bill_id).andMaterialIdEqualTo(material_id);
        List<DrawMaterialBillMaterial>cu=drawMaterialBillMaterialDAO.selectByExample(drawMaterialBillMaterialQuery);
        return cu;
    }

    /**
     * 更新备注
     * @param bill_id 领料单id
     * @param remark 新的备注
     */
    public void update(int bill_id,String remark){
        DrawMaterialBill drawMaterialBill=new DrawMaterialBill();
        drawMaterialBill.setRemark(remark);
        drawMaterialBill.setUpdateAt(new Date());
        drawMaterialBill.setUpdateBy(bill_id);
        DrawMaterialBillQuery drawMaterialBillQuery=new DrawMaterialBillQuery();
        drawMaterialBillQuery.or().andBillIdEqualTo(bill_id);
        drawMaterialBillDAO.updateByExampleSelective(drawMaterialBill,drawMaterialBillQuery);
    }

    /**
     * 更改状态
     * @param bill_id
     * @param status，更改后的状态
     */
    public void setStatus(int bill_id,int status){
        DrawMaterialBill drawMaterialBill=new DrawMaterialBill();
        drawMaterialBill.setBillState(status);
        drawMaterialBill.setUpdateAt(new Date());
        drawMaterialBill.setUpdateBy(bill_id);
        DrawMaterialBillQuery drawMaterialBillQuery=new DrawMaterialBillQuery();
        drawMaterialBillQuery.or().andBillIdEqualTo(bill_id);
        drawMaterialBillDAO.updateByExampleSelective(drawMaterialBill,drawMaterialBillQuery);

    }

    /**
     * 返回所有的内容
     * @return
     */
    public List<DrawMaterialBill> getAll(){
        DrawMaterialBillQuery drawMaterialBillQuery=new DrawMaterialBillQuery();
        drawMaterialBillQuery.or().andBillStateEqualTo(2);
        List<DrawMaterialBill>all=this.drawMaterialBillDAO.selectByExample(drawMaterialBillQuery);
        return all;
    }

    /**
     * 返回所有的具体领料单的具体item
     * @param bill_id 领料单od
     * @return
     */
    public List<DrawMaterialBillMaterial>getAllByID(int bill_id){
        DrawMaterialBillMaterialQuery drawMaterialBillMaterialQuery=new DrawMaterialBillMaterialQuery();
        drawMaterialBillMaterialQuery.or().andBillIdEqualTo(bill_id);
        List<DrawMaterialBillMaterial>all=this.drawMaterialBillMaterialDAO.selectByExample(drawMaterialBillMaterialQuery);
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
    public DrawMaterialBill getById(int bill_id){
        return this.drawMaterialBillDAO.selectByPrimaryKey(bill_id);
    }

    /**
     * 以下的函数都是根据提供的值
     * 搜索符合条件的领料单
     * @param principal
     * @return
     */
    public List<DrawMaterialBill> getByPrincipal(int principal){
        DrawMaterialBillQuery drawMaterialBillQuery=new DrawMaterialBillQuery();
        drawMaterialBillQuery.or().andAuditByEqualTo(principal);
        List<DrawMaterialBill>all=this.drawMaterialBillDAO.selectByExample(drawMaterialBillQuery);
        return all;
    }

    /**
     * 根据前面的search 框传过来的map 进行检索，可能有空字符串，主要处理空字符串
     * @param search 传过来的serach 字符串，为json格式
     * @return 检索到的内容
     */
    public List<DrawMaterialBill> getByMap(String search){
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
        DrawMaterialBillQuery drawMaterialBillQuery=new DrawMaterialBillQuery();
        DrawMaterialBillQuery.Criteria searc=drawMaterialBillQuery.createCriteria();
        searc.andDrawReasonEqualTo(2);
        String toprincipal=map.get("toPrincipal").toString();
        if(toprincipal.length()!=0)
        {
            searc.andToPrincipalEqualTo(Integer.parseInt(toprincipal));
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
        List<DrawMaterialBill>all=this.drawMaterialBillDAO.selectByExample(drawMaterialBillQuery);
        return all;
    }
}
