package com.springmvc.service;

import com.springmvc.dao.ClientDAO;
import com.springmvc.dao.OrderBillDAO;
import com.springmvc.dto.Admin;
import com.springmvc.dto.Client;
import com.springmvc.dto.PageMode;
import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.ClientQuery;
import com.springmvc.pojo.OrderBillQuery;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("ClientService")
@Transactional
public class ClientService extends BaseService {
    @Resource
    private ClientDAO clientDAO;

    @Resource
    private OrderBillDAO orderBillDAO;

    /**
     * 查询客户信息（分页）
     *
     * 将主表信息取出：
     * 搜索字段：客户名称、联系人、联系电话
     *
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
        if (!ParamUtils.isNull(sortColumn)) {
            clientQuery.setOrderByClause(ParamUtils.camel2Underline(sortColumn) + " " + sort);
        }

        // 搜索客户名称
        ClientQuery.Criteria criteria = clientQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andClientNameLike("%" + searchKey + "%");
        }
        // 搜索联系人
        criteria = clientQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andContactLike("%" + searchKey + "%");
        }
        // 搜索联系电话
        criteria = clientQuery.or();
        if (!ParamUtils.isNull(searchKey)) {
            criteria.andContactPhoneLike("%" + searchKey + "%");
        }

        List<Client> result = clientDAO.selectByExample(clientQuery);
        return new PageMode<Client>(result, clientDAO.countByExample(clientQuery));
    }

    /**
     * 查询用户信息（单个）
     *
     * 将主表信息取出：client
     *
     * @param clientId
     * @return
     */
    public Client getClientById(int clientId) {
        return clientDAO.selectByPrimaryKey(clientId);
    }

    /**
     * 新增客户信息
     *
     * 将主表信息保存：client
     * 添加日志信息：LogType.CLIENT, Operate.ADD
     */
    public Client addClient(String clientName, String contact, String contactPhone) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Client client = new Client();
        client.setClientName(clientName);
        client.setContact(contact);
        client.setContactPhone(contactPhone);
        client.setCreateAt(new Date());
        client.setCreateBy(loginAdmin.getAdminId());
        client.setUpdateAt(new Date());
        client.setUpdateBy(loginAdmin.getAdminId());
        clientDAO.insertSelective(client);

        // 添加日志
        addLog(LogType.CLIENT, Operate.ADD, client.getClientId());
        return getClientById(client.getClientId());
    }

    /**
     * 修改客户信息
     *
     * 更新主表信息：client
     * 添加日志信息：LogType.CLIENT, Operate.UPDATE
     */
    public Client updateClient(Integer clientId, String clientName, String contact, String contactPhone) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Client client = new Client();
        client.setClientId(clientId);
        client.setClientName(clientName);
        client.setContact(contact);
        client.setContactPhone(contactPhone);
        client.setUpdateAt(new Date());
        client.setUpdateBy(loginAdmin.getAdminId());
        clientDAO.updateByPrimaryKeySelective(client);

        // 添加日志
        addLog(LogType.CLIENT, Operate.UPDATE, client.getClientId());
        return getClientById(client.getClientId());
    }

    /**
     * 删除客户
     *
     * 检查要删除的主表信息是否被其他信息引用：order_bill
     * 删除主表信息：client
     * 添加日志信息：LogType.CLIENT, Operate.REMOVE
     */
    public void deleteClient(List<Integer> idList) {
        OrderBillQuery orderBillQuery = new OrderBillQuery();
        orderBillQuery.or().andClientIdIn(idList);
        if (orderBillDAO.countByExample(orderBillQuery) > 0) {
            throw new BadRequestException(CLIENT_REFER_BY_ORDER);
        }

        // 删除 client
        ClientQuery clientQuery = new ClientQuery();
        clientQuery.or().andClientIdIn(idList);
        clientDAO.deleteByExample(clientQuery);
        // 添加日志
        addLog(LogType.CLIENT, Operate.REMOVE, idList);
    }

    private static final String CLIENT_REFER_BY_ORDER = "客户被订单引用";
}
