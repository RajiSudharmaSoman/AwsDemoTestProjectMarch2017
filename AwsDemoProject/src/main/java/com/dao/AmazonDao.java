package com.dao;

import org.springframework.stereotype.Component;

@Component
public interface AmazonDao {
	public String saveJson(String s3Text);

}
