package com.springmvc.dto;

import com.springmvc.pojo.ReturnMaterialBillEntity;
import java.util.ArrayList;

public class ReturnMaterialBill extends ReturnMaterialBillEntity {
    /**
     * 存储从表
     */
    private ArrayList<Integer> material_id_list = new ArrayList<Integer>();
    private ArrayList<Integer> quantity_list = new ArrayList<Integer>();
    private ArrayList<String> remark_list = new ArrayList<String>();
    /**
     * 添加从表内容 但是不插入大数据库,
     * 要检查是否有materialid重复
     * @param material_id 物料id
     * @param quantity number of material
     * @param remark 备注
     * @return ture success false fail
     */
    public boolean addListStore(int material_id,int quantity,String remark){
        if(this.material_id_list.contains(material_id))
            return false;
        this.material_id_list.add(material_id);
        this.quantity_list.add(quantity);
        this.remark_list.add(remark);
        return true;
    }

    /**
     * 移除某一个具体物料，利用material_id
     * @param material_id
     */
    public boolean removeListStore(int material_id){
        if(this.material_id_list.contains(material_id)){
            int index = this.material_id_list.indexOf(material_id);
            this.material_id_list.remove(index);
            this.quantity_list.remove(index);
            this.remark_list.remove(index);
            return true;
        }
        return false;
    }

    /**
     * 这三个函数都是用来取值的
     * @param i 第i个
     * @return
     */
    public int getMaterialid(int i){
        return this.material_id_list.get(i);
    }
    public int getQuantity(int i){
        return this.quantity_list.get(i);
    }
    public String getRemark(int i){
        return this.remark_list.get(i);
    }
    /**
     *返回共有多少个具体物料
     * @return 个数
     */
    public int getSize(){
        return this.material_id_list.size();
    }
}