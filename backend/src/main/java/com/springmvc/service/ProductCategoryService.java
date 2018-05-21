package com.springmvc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ProductCategoryService")
@Transactional
public class ProductCategoryService extends BaseService {
}
