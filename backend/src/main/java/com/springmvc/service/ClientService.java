package com.springmvc.service;

import com.springmvc.dao.ClientDAO;
import com.springmvc.dto.Client;
import com.springmvc.dto.PageMode;
import com.springmvc.pojo.ClientQuery;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("ClientService")
@Transactional
public class ClientService extends BaseService {
    @Resource
    private ClientDAO clientDAO;


    /**
     * 查询客户信息（分页）
     *
     * 将主表信息取出：
     * 搜索字段：编号、客户名称、联系电话
     * @param current 当前位置
     * @param limit 一次读取长度
     * @param sortColumn 按哪一列排序
     * @param sort  排序方式 升序 降序
     * @param searchKey 关键字搜索
     * @return
     */
    public PageMode<Client> pageClient(Integer current, Integer limit, String sortColumn, String sort, String searchKey) {
        ClientQuery clientQuery = new ClientQuery();
        clientQuery.setOffset((current-1) * limit);
        clientQuery.setLimit(limit);
        if (ParamUtils.isNull(sortColumn)) {
            sortColumn = "clientId";
            sort = "asc";
        }

        ClientQuery.Criteria criteria = clientQuery.or();

        if (!ParamUtils.isNull(searchKey)) {
            //姓名查找
            criteria.andClientNameLike("%" + searchKey + "%");
            criteria = clientQuery.or();
            //手机号码查找
            criteria.andContactPhoneLike("%" + searchKey + "%");
        }
        clientQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        List<Client> result = clientDAO.selectByExample(clientQuery);
        clientQuery.setLimit(0);
        return new PageMode<Client>(result, clientDAO.countByExample(clientQuery));
    }

    /**
     * 新增客户信息
     */
    public int addClient(String name, String phone){
        Client client = new Client();
        client.setClientName(name);
        client.setContactPhone(phone);
        return clientDAO.insert(client);
    }

    /**
     * 修改客户信息
     */
    public int updateClient(Integer id, String name, String phone) {
        Client client = clientDAO.selectByPrimaryKey(id);
        client.setClientName(name);
        client.setContactPhone(phone);
        return clientDAO.updateByPrimaryKey(client);
    }

    /**
     * 删除客户
     */
    public int deleteClient(Integer id) {
        return clientDAO.deleteByPrimaryKey(id);
    }
}
