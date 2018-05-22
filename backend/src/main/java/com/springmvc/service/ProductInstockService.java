package com.springmvc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ProductInstockService")
@Transactional
public class ProductInstockService extends BaseService {
}
