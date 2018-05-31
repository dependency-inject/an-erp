package com.springmvc.controller;

import com.springmvc.dto.Client;
import com.springmvc.dto.PageMode;
import com.springmvc.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Resource
    ClientService clientService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<Client> search(@RequestParam Integer current, @RequestParam Integer limit, String sortColumn, String sort, String searchKey) {
        return clientService.pageClient(current, limit, sortColumn, sort, searchKey);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addClient(@RequestParam String clientName, @RequestParam String contactPhone) {
        clientService.addClient(clientName, contactPhone);
        return "success";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateClient(@RequestParam Integer clientId, String clientName, String contactPhone) {
        clientService.updateClient(clientId, clientName, contactPhone);
        return "success";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteClient(@RequestParam Integer clientId) {
        clientService.deleteClient(clientId);
        return "success";
    }
}
