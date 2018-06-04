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
     * @return 返回一个List
     */
    public List<MaterialCategory> getAll(){
        return materialCategoryDAO.selectByExample(new MaterialCategoryQuery());
    }

    /**
     * 根据id获取一个分类记录
     * @param categoryId 分类id
     * @return返回一条记录
     */
    public MaterialCategory getMaterialCategoryById(int categoryId){
        MaterialCategory materialCategory = materialCategoryDAO.selectByPrimaryKey(categoryId);
        return materialCategory;
    }
    /**
     * 添加物料类别
     * @param categoryName 类别名称
     * @param parentId     父类id
     * @return
     */
    public MaterialCategory addMaterialCategory(String categoryName, Integer parentId){

        //保存主表信息
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        MaterialCategory materialCategory = new MaterialCategory();
        materialCategory.setCategoryName(categoryName);
        materialCategory.setParentId(parentId);

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
     * @param categoryId 分类id
     * @param categoryName      分类名称
     * @param parentId          父类id
     * @return  返回一个修改过的记录
     */
    public MaterialCategory updateMaterialCategory(Integer categoryId, String categoryName, Integer parentId){

        //更新数据
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();
        MaterialCategory materialCategory = new MaterialCategory();
        materialCategory.setCategoryId(categoryId);
        materialCategory.setCategoryName(categoryName);
        // 不更新上级类别信息
//        materialCategory.setParentId(parentId);


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

        //检查是否拥有子类
        MaterialCategoryQuery materialCategoryQuery = new MaterialCategoryQuery();
        materialCategoryQuery.or().andParentIdIn(idList);
        if(materialCategoryDAO.countByExample(materialCategoryQuery) > 0){
            throw new BadRequestException(CANNOT_REMOVE_CATEGORY_NOT_EMPTY);
        }

        //正常删除
        MaterialCategoryQuery materialCategoryQuery1 = new MaterialCategoryQuery();
        materialCategoryQuery1.or().andCategoryIdIn(idList);
        materialCategoryDAO.deleteByExample(materialCategoryQuery1);

        //添加日志
        addLog(LogType.MATERIAL_CATEGORY, Operate.REMOVE, idList);
    }

    private static final String MATERIALCATEGORY_REFEREF_BY_MATERIAL = "物料分类被物料表引用";
    private static final String CANNOT_REMOVE_CATEGORY_NOT_EMPTY = "不能删除含有子类的分类";
}
