package com.springmvc.dto;

import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.DrawMaterialBillEntity;
import com.springmvc.pojo.DrawMaterialBillMaterialQuery;

import javax.annotation.Resource;
import java.util.ArrayList;

public class DrawMaterialBill extends DrawMaterialBillEntity {

    /**
     * 存储从表
     */
    private ArrayList<Integer> material_id=new ArrayList<Integer>(),quantity=new ArrayList<Integer>();
    private ArrayList<String> remark=new ArrayList<String>();
    /**
     * 添加从表内容 但是不插入大数据库,
     * 要检查是否有materialid重复
     * @param material_id 物料id
     * @param quantity number of material
     * @param remark 规格
     * @return ture success false fail
     */
    public boolean addListStore(int material_id,int quantity,String remark) {
        if (this.material_id.contains(material_id))
            return false;
        else {
            this.material_id.add(material_id);
            this.quantity.add(quantity);
            this.remark.add(remark);
            return true;
        }
    }


    /**
     * 移除某一个具体物料，利用material_id
     * @param material_id
     */
    public void removeListStore(int material_id){
        int where=this.material_id.indexOf(material_id);
        this.material_id.remove(where);
        this.remark.remove(where);
        this.quantity.remove(where);

    }

    /**
     * 这三个函数都是用来去值的
     * @param i 第i个
     * @return
     */
    public int getMaterialid(int i){return this.material_id.get(i);}
    public int getQuantity(int i){return this.quantity.get(i);}
    public String getRemark(int i){return this.remark.get(i);}

    /**
     *返回共有多少个具体物料
     * @return 个数
     */
    public int getSize(){
        return this.material_id.size();
    }
}