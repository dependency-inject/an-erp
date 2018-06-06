package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.Client;
import com.springmvc.dto.PageMode;
import com.springmvc.service.ClientService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Resource
    ClientService clientService;

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<Client> getList() {
        return clientService.getList();
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<Client> search(@RequestParam Integer current, @RequestParam Integer limit, String sortColumn, String sort, String searchKey) {
        return clientService.pageClient(current, limit, sortColumn, sort, searchKey);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.CLIENT_ADD)
    public Client addClient(@RequestParam String clientName, String contact, String contactPhone) {
        return clientService.addClient(clientName, contact, contactPhone);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.CLIENT_UPDATE)
    public Client updateClient(@RequestParam Integer clientId, @RequestParam String clientName, String contact, String contactPhone) {
        return clientService.updateClient(clientId, clientName, contact, contactPhone);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.CLIENT_REMOVE)
    public String deleteClient(@RequestParam String idList) {
        clientService.deleteClient(ParamUtils.toIntList(idList));
        return "success";
    }
}
