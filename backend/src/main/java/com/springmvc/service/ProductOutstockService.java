package com.springmvc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ProductOutstockService")
@Transactional
public class ProductOutstockService extends BaseService {
}
