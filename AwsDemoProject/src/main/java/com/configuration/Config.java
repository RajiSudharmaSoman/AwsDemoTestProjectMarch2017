package com.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
	 @Value("${application.environment}")
	    private String fileProperty;

	    @Value("${java.version}")
	    private String systemProperty;

	    @Override
	    public String toString() {
	        return "Config{" +
	                "fileProperty='" + fileProperty + '\'' +
	                ", systemProperty='" + systemProperty + '\'' +
	                '}';
	    }

}
