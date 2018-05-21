package com.springmvc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ProductService")
@Transactional
public class ProductService extends BaseService {
}
