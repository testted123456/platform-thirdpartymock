package com.platform.apps.thirdpartymock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlatformThirdpartymockApplication {
	
	public static Logger logger = LoggerFactory.getLogger(PlatformThirdpartymockApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(PlatformThirdpartymockApplication.class, args);
	}
	
}
