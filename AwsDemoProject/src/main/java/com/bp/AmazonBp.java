package com.bp;

import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;

@Component
public interface AmazonBp {
	public String saveJson() throws Exception, AmazonClientException;
		
	
}
