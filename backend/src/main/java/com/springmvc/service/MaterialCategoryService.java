package com.springmvc.service;

import com.springmvc.dao.MaterialCategoryDAO;
import com.springmvc.dao.MaterialDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.MaterialCategory;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.MaterialCategoryQuery;
import com.springmvc.pojo.MaterialQuery;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service("MaterialCategoryService")
@Transactional
public class MaterialCategoryService extends BaseService {

    //DAO变量声明
    @Resource
    private MaterialCategoryDAO materialCategoryDAO;

    @Resource
    private MaterialDAO materialDAO;

    /**
     * 获取物料类别表的所有信息
     * @param getAll 一个获取的信号
     * @return 返回一个List
     */
    public List<MaterialCategory> getAll(@RequestParam Integer getAll){

        MaterialCategoryQuery materialCategoryQuery = new MaterialCategoryQuery();
        MaterialCategoryQuery.Criteria criteria = materialCategoryQuery.or();
        //选取所有记录
        criteria.andCategoryIdGreaterThanOrEqualTo(1);
        List<MaterialCategory> materialCategories = materialCategoryDAO.selectByExample(materialCategoryQuery);
        return materialCategories;
    }

    /**
     * 根据id获取一个分类记录
     * @param materialCategoryId 分类id
     * @return返回一条记录
     */
    public MaterialCategory getMaterialCategoryById(int materialCategoryId){
        MaterialCategory materialCategory = materialCategoryDAO.selectByPrimaryKey(materialCategoryId);
        return materialCategory;
    }
    /**
     * 添加物料类别
     * @param category_name 类别名称
     * @param parent_id     父类id
     * @return
     */
    public MaterialCategory addMaterialCategory(String category_name, Integer parent_id){

        //保存主表信息
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        MaterialCategory materialCategory = new MaterialCategory();
        materialCategory.setCategoryName(category_name);
        materialCategory.setParentId(parent_id);

        materialCategory.setCreateAt(new Date());
        materialCategory.setCreateBy(loginAdmin.getAdminId());
        materialCategory.setUpdateAt(new Date());
        materialCategory.setUpdateBy(loginAdmin.getAdminId());

        materialCategoryDAO.insertSelective(materialCategory);

        //添加日志信息
        addLog(LogType.MATERIAL_CATEGORY,Operate.ADD,materialCategory.getCategoryId());
        return getMaterialCategoryById(materialCategory.getCategoryId());
    }

    /**
     * 修改物料类别的信息
     * @param materialCategoryId 分类id
     * @param category_name      分类名称
     * @param parent_id          父类id
     * @return  返回一个修改过的记录
     */
    public MaterialCategory updateMaterialCategory(Integer materialCategoryId, String category_name, Integer parent_id){

        //更新数据
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        MaterialCategory materialCategory = new MaterialCategory();
        materialCategory.setCategoryId(materialCategoryId);
        materialCategory.setCategoryName(category_name);
        materialCategory.setParentId(parent_id);


        materialCategory.setUpdateAt(new Date());
        materialCategory.setUpdateBy(loginAdmin.getAdminId());

        materialCategoryDAO.updateByPrimaryKeySelective(materialCategory);

        //添加日志信息
        addLog(LogType.MATERIAL_CATEGORY,Operate.UPDATE,materialCategory.getCategoryId());
        return getMaterialCategoryById(materialCategory.getCategoryId());
    }

    /**
     * 删除物料分类
     *
     * 删除主表的信息:MaterialCategory
     *
     *判断是否被物料表引用
     * 添加日志信息
     * @param idList 一个分类id列表
     */
    public void removeMaterialCategory(List<Integer> idList){

        //检查是否被material引用
        MaterialQuery materialQuery = new MaterialQuery();
        materialQuery.or().andCategoryIdIn(idList);
        if(materialDAO.countByExample(materialQuery) > 0){
            throw new BadRequestException(MATERIALCATEGORY_REFEREF_BY_MATERIAL);
        }

        //未完待续
    }

    private static final String MATERIALCATEGORY_REFEREF_BY_MATERIAL = "物料分类被物料表引用";
}
