package com.springmvc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ClientService")
@Transactional
public class ClientService extends BaseService {
}
