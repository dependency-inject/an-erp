package com.springmvc.service;

import com.springmvc.dao.MaterialDAO;
import com.springmvc.dto.DrawMaterialBill;
import com.springmvc.dto.Material;
import com.springmvc.pojo.DrawMaterialBillQuery;
import com.springmvc.pojo.MaterialQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Service("MaterialService")
@Transactional
public class MaterialService extends BaseService {
    @Resource
    private MaterialDAO materialDAO;
    public List<Material> getAll(){
        MaterialQuery materialQuery=new MaterialQuery();
        materialQuery.or();
        List<Material> total=this.materialDAO.selectByExample(materialQuery);
        return total;
    }
}
