package com.myimooc.spring.aop.guide.service;

import com.myimooc.spring.aop.guide.domain.Product;
import org.springframework.stereotype.Service;

import com.myimooc.spring.aop.guide.security.AdminOnly;

/**
 * @title 产�?�?务类
 * @describe 产�?相关业务�?务-AOP方�?实现�?��?校验
 * @author zc
 * @version 1.0 2017-09-03
 */
@Service
public class ProductServiceAop {
	
	@AdminOnly
	public void insert(Product product){
		System.out.println("insert product");
	}
	
	@AdminOnly
	public void delete(Long id){
		System.out.println("delete product");
	}
	
}
