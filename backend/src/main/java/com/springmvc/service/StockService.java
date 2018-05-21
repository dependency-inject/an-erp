package com.springmvc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("StockService")
@Transactional
public class StockService extends BaseService {
}
